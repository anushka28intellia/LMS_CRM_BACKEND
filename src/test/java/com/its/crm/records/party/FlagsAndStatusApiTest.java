package com.its.crm.records.party;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.its.crm.records.ApiTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Brief §3.8.2 (Hot, Handle with care) and §3.7 (relationship status): the rules the QA pass of 25 Nov 2026 found
 * missing.
 */
class FlagsAndStatusApiTest extends ApiTestBase {

    @Autowired
    private JdbcClient jdbc;

    private String createPerson(String first, String last, String extra) throws Exception {
        return read(postJson("/api/persons", personBody(first, last, extra)).andExpect(status().isCreated()), "$.id");
    }

    private ResultActions patchAs(String user, String url, String values) throws Exception {
        return mvc.perform(patch(url).header("X-Dev-User", user).header("If-Match", versionOf(url))
                .contentType(MediaType.APPLICATION_JSON).content("{\"values\":" + values + "}"));
    }

    private String search(String q, String flag) {
        return """
                {"q":"%s","page":0,"size":20%s}""".formatted(q, flag == null ? "" : ",\"flag\":\"" + flag + "\"");
    }

    @Test
    void hotMovesToTheTopAndExpiresAfterTheSetting() throws Exception {
        String u = unique();
        // Different names, so the duplicate check doesn't stop the second one; both carry u for the search.
        String hot = createPerson("Margaret", "Ashford" + u, "");
        String plain = createPerson("Bartholomew", "Whitlock" + u, "");

        // Newest first until one is Hot; then the Hot one leads the default order and is marked in the list.
        postJson("/api/persons/search", search(u, null)).andExpect(jsonPath("$.items[0].id").value(plain));
        putJson("/api/persons/" + hot + "/hot", "{\"hot\":true}").andExpect(status().isOk())
                .andExpect(jsonPath("$.flags.hotSince").isNotEmpty());
        postJson("/api/persons/search", search(u, null))
                .andExpect(jsonPath("$.items[0].id").value(hot))
                .andExpect(jsonPath("$.items[0].hot").value(true))
                .andExpect(jsonPath("$.items[1].hot").value(false));
        // A sort the user picks is kept as chosen: Whitlock before Ashford, Hot or not.
        postJson("/api/persons/search", """
                {"q":"%s","page":0,"size":20,"sort":[{"field":"lastName","dir":"DESC"}]}""".formatted(u))
                .andExpect(jsonPath("$.items[0].id").value(plain));

        // Set 31 days ago: past the 30-day setting, it reads as not Hot everywhere.
        jdbc.sql("UPDATE person SET hot_since = now() - interval '31 days' WHERE id = :id").param("id", UUID.fromString(hot)).update();
        getJson("/api/persons/" + hot).andExpect(jsonPath("$.flags.hotSince").doesNotExist());
        postJson("/api/persons/search", search(u, null))
                .andExpect(jsonPath("$.items[0].id").value(plain))
                .andExpect(jsonPath("$.items[1].hot").value(false));
        postJson("/api/persons/search", search(u, "HOT")).andExpect(jsonPath("$.totalElements").value(0));

        // A longer setting applies at once (brief §2.2), and setting Hot again renews it from today.
        try {
            putJson("/api/metadata/settings", "{\"hotExpiryDays\":60}").andExpect(status().isNoContent());
            getJson("/api/persons/" + hot).andExpect(jsonPath("$.flags.hotSince").isNotEmpty());
            postJson("/api/persons/search", search(u, "HOT")).andExpect(jsonPath("$.items[0].id").value(hot));
        } finally {
            putJson("/api/metadata/settings", "{\"hotExpiryDays\":30}").andExpect(status().isNoContent());
        }
        putJson("/api/persons/" + hot + "/hot", "{\"hot\":true}").andExpect(status().isOk());
        getJson("/api/persons/" + hot).andExpect(jsonPath("$.flags.hotSince").isNotEmpty());
        putJson("/api/persons/" + hot + "/hot", "{\"hot\":false}").andExpect(status().isOk())
                .andExpect(jsonPath("$.flags.hotSince").doesNotExist());
    }

    @Test
    void activeStatusIsSetByTheCrmNotByAUser() throws Exception {
        String u = unique();
        postJson("/api/persons", personBody("Status", "Active" + u, ",\"creditStatus\":\"ACTIVE\""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.creditStatus", containsString("can't be set by hand")));
        postJson("/api/persons", personBody("Status", "Capital" + u, ",\"capitalStatus\":\"ACTIVE\""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.capitalStatus").exists());
        String person = createPerson("Status", "Prospect" + u, ",\"creditStatus\":\"PROSPECT\"");
        getJson("/api/persons/" + person).andExpect(jsonPath("$.values.creditStatus").value("PROSPECT"));

        String entity = read(postJson("/api/entities", entityBody("Status Entity " + u, validAcn(), "BROKER_FIRM", null))
                .andExpect(status().isCreated()), "$.id");
        patchAs("u1", "/api/entities/" + entity, "{\"creditStatus\":\"ACTIVE\"}")
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors.creditStatus").exists());
        patchAs("u1", "/api/entities/" + entity, "{\"creditStatus\":\"PROSPECT\"}").andExpect(status().isOk());

        // A party the CRM made Active keeps the value, and its other fields still save.
        jdbc.sql("UPDATE entity SET custom_fields = custom_fields || '{\"creditStatus\":\"ACTIVE\"}'::jsonb WHERE id = :id")
                .param("id", UUID.fromString(entity)).update();
        patchAs("u1", "/api/entities/" + entity, "{\"website\":\"https://status.example.test\"}").andExpect(status().isOk())
                .andExpect(jsonPath("$.values.creditStatus").value("ACTIVE"));
    }

    @Test
    void handleWithCareReasonIsSeenOnlyWithPermission() throws Exception {
        getJson("/api/metadata/objects/person/fields")
                .andExpect(jsonPath("$[?(@.apiName == 'handleWithCare')].sensitive", hasItem(true)));
        getJson("/api/metadata/objects/entity/fields")
                .andExpect(jsonPath("$[?(@.apiName == 'handleWithCare')].sensitive", hasItem(true)));

        String person = createPerson("Care", "Full" + unique(), "");
        patchAs("u1", "/api/persons/" + person, "{\"handleWithCare\":\"Dispute over fees in 2025\"}").andExpect(status().isOk());
        getJson("/api/persons/" + person).andExpect(jsonPath("$.values.handleWithCare").value("Dispute over fees in 2025"));
        getAs("u3", "/api/persons/" + person).andExpect(jsonPath("$.values.handleWithCare").doesNotExist())
                .andExpect(jsonPath("$.values.firstName").value("Care"));
        patchAs("u3", "/api/persons/" + person, "{\"handleWithCare\":\"overwritten\"}").andExpect(status().isForbidden());
        getJson("/api/persons/" + person).andExpect(jsonPath("$.values.handleWithCare", not(containsString("overwritten"))));
    }
}
