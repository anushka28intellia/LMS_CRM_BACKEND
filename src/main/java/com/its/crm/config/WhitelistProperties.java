package com.its.crm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
public class WhitelistProperties {

    /**
     * Comma-separated whitelist URLs or a YAML list bound as a List<String>.
     * Supports both: app.whitelist=/auth/**,/swagger-ui/**
     * and app.whitelist[0]=/auth/** style.
     */
    private List<String> whitelist = new ArrayList<>();

    /**
     * Raw string whitelist for comma-separated format (e.g. app.whitelist=/auth/**,/swagger-ui/**).
     * If the list is empty, this string is parsed.
     */
    private String whitelistStr;

    /**
     * Returns the combined whitelist paths from both the list and the string format.
     */
    public List<String> getWhitelistPaths() {
        List<String> paths = new ArrayList<>(whitelist);
        if (whitelistStr != null && !whitelistStr.isBlank()) {
            Arrays.stream(whitelistStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(paths::add);
        }
        return paths;
    }
}
