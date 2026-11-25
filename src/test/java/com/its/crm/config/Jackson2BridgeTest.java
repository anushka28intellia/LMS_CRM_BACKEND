package com.its.crm.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.its.crm.dto.pipeline.PipelineDTO;
import com.its.crm.dto.task.TaskDTO;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guards the Spring Boot 4 bridges that keep the com.its.crm Jackson 2 JsonNode fields working: the JSONB mapper
 * used by Hypersistence and the HTTP converter that serves this module's types.
 */
class Jackson2BridgeTest {

    private final ObjectMapper jackson2 = new ObjectMapper();

    @Test
    void jsonbMapperStoresTheNodeContentAndReadsItBack() throws Exception {
        JsonNode steps = jackson2.readTree("[{\"name\":\"Enquiry\",\"order\":1,\"fields\":[{\"key\":\"amount\"}]}]");
        tools.jackson.databind.ObjectMapper mapper = new HypersistenceJsonMapperSupplier().get();

        String stored = mapper.writeValueAsString(steps);
        assertThat(jackson2.readTree(stored)).isEqualTo(steps);
        assertThat(stored).doesNotContain("nodeType");

        JsonNode read = mapper.readValue(stored, JsonNode.class);
        assertThat(read).isEqualTo(steps);
    }

    @Test
    void converterClaimsOnlyThisModulesTypesIncludingGenericLists() {
        Jackson2ConverterConfig.ModuleJackson2Converter converter =
                new Jackson2ConverterConfig.ModuleJackson2Converter(jackson2);
        var listOfTasks = new ParameterizedTypeReference<List<TaskDTO>>() { }.getType();
        var mapOfStrings = new ParameterizedTypeReference<Map<String, Object>>() { }.getType();

        assertThat(converter.canWrite(PipelineDTO.class, MediaType.APPLICATION_JSON)).isTrue();
        assertThat(converter.canWrite(listOfTasks, java.util.ArrayList.class, MediaType.APPLICATION_JSON)).isTrue();
        assertThat(converter.canRead(PipelineDTO.class, MediaType.APPLICATION_JSON)).isTrue();

        assertThat(converter.canWrite(mapOfStrings, java.util.LinkedHashMap.class, MediaType.APPLICATION_JSON)).isFalse();
        assertThat(converter.canWrite(String.class, MediaType.APPLICATION_JSON)).isFalse();
        assertThat(Jackson2ConverterConfig.belongsToModule(ResolvableType.forClass(
                com.its.crm.records.party.PartyDtos.class))).isFalse();
    }

    @Test
    void converterLeavesTheRecordsModuleToJackson3() {
        Jackson2ConverterConfig.ModuleJackson2Converter converter =
                new Jackson2ConverterConfig.ModuleJackson2Converter(jackson2);
        var listOfLinks = new ParameterizedTypeReference<List<com.its.crm.records.party.PartyDtos.PersonLink>>() { }.getType();

        assertThat(converter.canWrite(com.its.crm.records.record.RecordDtos.RecordDetail.class,
                MediaType.APPLICATION_JSON)).isFalse();
        assertThat(converter.canRead(com.its.crm.records.party.PartyDtos.LinkCreate.class,
                MediaType.APPLICATION_JSON)).isFalse();
        assertThat(converter.canWrite(listOfLinks, java.util.ArrayList.class, MediaType.APPLICATION_JSON)).isFalse();
    }
}
