package org.datavaultplatform.common.event.audit;

import org.datavaultplatform.common.event.Event;

import javax.persistence.Entity;

@Entity
public class AuditStart extends Event {

    AuditStart() {};
    public AuditStart(String jobId, String auditId) {
        super(jobId, auditId, null,null, null, "Audit started");
        this.eventClass = AuditStart.class.getCanonicalName();
    }
}
