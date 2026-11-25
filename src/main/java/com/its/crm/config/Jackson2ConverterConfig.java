package com.its.crm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Keeps the {@code com.its.crm} API on Jackson 2 after the move to Spring Boot 4.
 * <p>
 * Boot 4 serializes HTTP bodies with Jackson 3 ({@code tools.jackson}), but this module's DTOs and entities carry
 * Jackson 2 {@code com.fasterxml.jackson.databind.JsonNode} fields (pipeline steps, task form data), which Jackson 3
 * cannot read or write. This converter goes first and claims only request and response types that involve a
 * {@code com.its.crm} class outside {@code com.its.crm.records}, using this module's own {@link ObjectMapper} bean
 * ({@link JacksonConfig}), so those endpoints read and write exactly what they did on Boot 3. Every other type,
 * including the whole {@code com.its.crm.records} API (Persons, Entities, metadata, list views, audit), which is
 * written for Jackson 3, falls through to Boot's Jackson 3 converter.
 */
@Configuration
public class Jackson2ConverterConfig implements WebMvcConfigurer {

    private static final String PACKAGE_PREFIX = "com.its.crm.";

    /** The records module (Persons, Entities...) uses Jackson 3 and must never go through this converter. */
    private static final String RECORDS_PREFIX = "com.its.crm.records.";

    private final ObjectMapper objectMapper;

    public Jackson2ConverterConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new ModuleJackson2Converter(objectMapper));
    }

    /**
     * True when the type, its generics (e.g. {@code List<TaskDTO>}) or its array component is a com.its.crm class
     * outside the records module.
     */
    static boolean belongsToModule(ResolvableType type) {
        if (type == ResolvableType.NONE) {
            return false;
        }
        Class<?> raw = type.resolve();
        if (raw != null && raw.getName().startsWith(PACKAGE_PREFIX) && !raw.getName().startsWith(RECORDS_PREFIX)) {
            return true;
        }
        if (type.isArray() && belongsToModule(type.getComponentType())) {
            return true;
        }
        for (ResolvableType generic : type.getGenerics()) {
            if (belongsToModule(generic)) {
                return true;
            }
        }
        return false;
    }

    // MappingJackson2HttpMessageConverter is deprecated in Spring Framework 7 in favour of the Jackson 3 converter;
    // it is used on purpose here until this module's DTOs move to Jackson 3.
    @SuppressWarnings({"removal", "deprecation"})
    static final class ModuleJackson2Converter extends MappingJackson2HttpMessageConverter {

        ModuleJackson2Converter(ObjectMapper objectMapper) {
            super(objectMapper);
        }

        @Override
        public boolean canRead(Class<?> clazz, MediaType mediaType) {
            return belongsToModule(ResolvableType.forClass(clazz)) && super.canRead(clazz, mediaType);
        }

        @Override
        public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
            return belongsToModule(ResolvableType.forType(type)) && super.canRead(type, contextClass, mediaType);
        }

        @Override
        public boolean canWrite(Class<?> clazz, MediaType mediaType) {
            return belongsToModule(ResolvableType.forClass(clazz)) && super.canWrite(clazz, mediaType);
        }

        @Override
        public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
            ResolvableType target = type != null ? ResolvableType.forType(type) : ResolvableType.forClass(clazz);
            // super.canWrite(Type, ...) would delegate back to canWrite(Class, ...) above, which only sees the
            // runtime class (e.g. ArrayList for List<TaskDTO>) and refuses it; once the generic type matches, go
            // straight to the Jackson 2 converter's own check.
            return (belongsToModule(target) || belongsToModule(ResolvableType.forClass(clazz)))
                    && super.canWrite(clazz, mediaType);
        }
    }
}
