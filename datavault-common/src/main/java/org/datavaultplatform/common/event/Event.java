package org.datavaultplatform.common.event;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;
import org.datavaultplatform.common.model.*;
import org.datavaultplatform.common.response.EventInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="Events")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="eventType", discriminatorType = DiscriminatorType.STRING)
public class Event {
    
    // Event Identifier
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true, length = 36)
    private String id;
    
    // Event properties
    @Column(name = "message", columnDefinition = "LONGTEXT", length = 4000)
    public String message;
    public String retrieveId;
    public String eventClass;
    
    @Transient
    public Integer nextState = null;
    
    // Serialise date in ISO 8601 format
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    public int sequence;

    // By default events are persisted
    @Transient
    public Boolean persistent = true;
    
    // Related deposit
    @JsonIgnore
    @ManyToOne
    private Deposit deposit;
    @Transient
    public String depositId;
    
    // Related vault
    @JsonIgnore
    @ManyToOne
    private Vault vault;
    @Transient
    public String vaultId;
    
    // Related job
    @JsonIgnore
    @ManyToOne
    private Job job;
    @Transient
    public String jobId;
    
    // Related user
    @JsonIgnore
    @ManyToOne
    private User user;
    @Transient
    public String userId;
    
    // Event agent
    private String agent;
    
    // Web request properties
    public String remoteAddress;
    public String userAgent;
    
    @Enumerated(EnumType.STRING)
    private Agent.AgentType agentType;

    // For Audit

    // Related audit
    @JsonIgnore
    @ManyToOne
    private Audit audit;
    @Transient
    public String auditId;

    // Related archive
    @JsonIgnore
    @ManyToOne
    private Archive archive;
    @Transient
    public String archiveId;

    private String location;

    // Related deposit chunk
    @JsonIgnore
    @ManyToOne
    private DepositChunk chunk;
    @Transient
    public String chunkId;

    // Related Role assignee
    @JsonIgnore
    @ManyToOne
    private User assignee;
    @Transient
    public String assigneeId;

    // Related Role school target
    @JsonIgnore
    @ManyToOne
    private Group school;
    @Transient
    public String schoolId;

    // Related Role target
    @JsonIgnore
    @ManyToOne
    private RoleModel role;
    @Transient
    public String roleId;
    
    public Event() {};
    public Event(String message) {
        this.eventClass = Event.class.getCanonicalName();
        this.message = message;
        this.timestamp = new Date();
        this.sequence = 0;
    }

    public Event(String jobId, String depositId, String retrieveId, String message) {
        this.eventClass = Event.class.getCanonicalName();
        this.jobId = jobId;
        this.depositId = depositId;
        this.retrieveId = retrieveId;
        this.auditId = null;
        this.message = message;
        this.timestamp = new Date();
        this.sequence = 0;
    }
    
    public Event(String jobId, String auditId, String chunkId, String archiveId, String location, String message) {
        this.eventClass = Event.class.getCanonicalName();
        this.jobId = jobId;
        this.auditId = auditId;
        this.chunkId = chunkId;
        this.archiveId = archiveId;
        this.location = location;
        this.message = message;
        this.timestamp = new Date();
        this.sequence = 0;
    }

    public String getID() { return id; }

    public String getEventClass() {
        return eventClass;
    }

    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getDepositId() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    public String getVaultId() {
        return vaultId;
    }

    public void setVaultId(String vaultId) {
        this.vaultId = vaultId;
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Agent.AgentType getAgentType() {
        return agentType;
    }

    public void setAgentType(Agent.AgentType agentType) {
        this.agentType = agentType;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getRetrieveId() {
        return retrieveId;
    }

    public void setRetrieveId(String retrieveId) {
        this.retrieveId = retrieveId;
    }

    public Boolean getPersistent() {
        return persistent;
    }

    public void setPersistent(Boolean persistent) {
        this.persistent = persistent;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
    
    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    public Vault getVault() {
        return vault;
    }

    public void setVault(Vault vault) {
        this.vault = vault;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public Event withNextState(Integer state) {
        this.nextState = state;
        return this;
    }
    
    public Event withUserId(String userId) {
        this.userId = userId;
        return this;
    }
    
    public EventInfo convertToResponse() {
        return new EventInfo(id,
                timestamp,
                message,
                user != null ? user.getID() : null,
                vault != null ? vault.getID() : null,
                eventClass,
                deposit != null ? deposit.getID() : null,
                agent,
                agentType != null ? agentType.toString() : null,
                remoteAddress,
                userAgent);
    }

    public String getAuditId() { return auditId; }

    public void setAuditId(String auditId) { this.auditId = auditId; }

    public String getChunkId() { return chunkId; }

    public void setChunkId(String chunkId) { this.chunkId = chunkId; }

    public String getArchiveId() { return archiveId; }

    public void setArchiveId(String archiveId) { this.archiveId = archiveId; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Group getSchool() {
        return school;
    }

    public void setSchool(Group school) {
        this.school = school;
    }

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }
}
