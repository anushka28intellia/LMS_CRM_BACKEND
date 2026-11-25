package com.its.crm.records.party.entity;

import com.its.crm.records.audit.AuditQueryService;
import com.its.crm.records.record.RecordController;
import com.its.crm.records.record.RecordService;
import com.its.crm.records.record.search.RecordSearchService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Entities (brief §3.3): every endpoint of a configurable record type. The ACN and ABN gate (G1), the
 * similar-name warning and the Entity type review queue are Entity rules, set in Setup → Entity rules.
 */
@RestController
@RequestMapping("/entities")
public class EntityController extends RecordController {

    public EntityController(RecordService service, RecordSearchService search, AuditQueryService audit) {
        super("entity", service, search, audit);
    }
}
