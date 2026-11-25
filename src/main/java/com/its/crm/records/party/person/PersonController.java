package com.its.crm.records.party.person;

import com.its.crm.records.audit.AuditQueryService;
import com.its.crm.records.record.RecordController;
import com.its.crm.records.record.RecordService;
import com.its.crm.records.record.search.RecordSearchService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Persons (brief §3.2): every endpoint of a configurable record type. */
@RestController
@RequestMapping("/persons")
public class PersonController extends RecordController {

    public PersonController(RecordService service, RecordSearchService search, AuditQueryService audit) {
        super("person", service, search, audit);
    }
}
