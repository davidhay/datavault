package org.datavaultplatform.common.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="RetentionPolicies")
public class RetentionPolicy {
    // RetentionPolicy Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, length = 36)
    private int id;

    // Name of the policy
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    // Description of the policy
    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    // URL of the Funder's Policy
    @Column(name = "url", nullable = true, columnDefinition = "TEXT")
    private String url;

    // Minimum retention period in years
    @Column(name="minRetentionPeriod", nullable = false)
    private int minRetentionPeriod;

    // Extend the expiry date of a vault if a deposit is retrieved
    @Column(name="extendUponRetrieval")
    private boolean extendUponRetrieval;

    // Date policy in effect
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "inEffectDate", nullable = true)
    private Date inEffectDate;

    // Date policy in effect
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "endDate", nullable = true)
    private Date endDate;

    // Date policy in effect
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "dataGuidanceReviewed", nullable = true)
    private Date dataGuidanceReviewed;

    // A policy is related to a number of vaults
    @JsonIgnore
    @OneToMany(targetEntity=Vault.class, mappedBy="retentionPolicy", fetch=FetchType.LAZY)
    @OrderBy("creationTime")
    private List<Vault> vaults;


    /*  DEPRECATED */
    @Deprecated
    // Implementation class of the policy
    @Column(name = "engine", nullable = false, columnDefinition = "TEXT")
    private String engine;

    @Deprecated
    // Sort order of the policy
    @Column(name = "sort", nullable = false)
    private int sort;

    @Deprecated
    // Sort order of the policy
    @Column(name = "minDataRetentionPeriod", nullable = true, columnDefinition = "TEXT")
    private String minDataRetentionPeriod;



    public RetentionPolicy() {}
    public RetentionPolicy(String name) {
        this.name = name;
    }

    public int getID() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setDescription(String description) { this.description = description; }

    public String getDescription() { return description; }

    public void setEngine(String engine) { this.engine = engine; }

    public String getEngine() { return engine; }

    public void setSort(int sort) { this.sort = sort; }

    public int getSort() { return sort; }
    
    public String getUrl() { return this.url; }
    
    public void setUrl(String url) { this.url = url; }
    
    public String getMinDataRetentionPeriod() {
        return minDataRetentionPeriod;
    }
    public void setMinDataRetentionPeriod(String minDataRetentionPeriod) {
        this.minDataRetentionPeriod = minDataRetentionPeriod;
    }

    public int getMinRetentionPeriod() {
        return minRetentionPeriod;
    }

    public void setMinRetentionPeriod(int minRetentionPeriod) {
        this.minRetentionPeriod = minRetentionPeriod;
    }

    public boolean isExtendUponRetrieval() {
        return extendUponRetrieval;
    }

    public void setExtendUponRetrieval(boolean extendUponRetrieval) {
        this.extendUponRetrieval = extendUponRetrieval;
    }

    public Date getInEffectDate() {
        return inEffectDate;
    }
    
    public void setInEffectDate(Date inEffectDate) {
        this.inEffectDate = inEffectDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Date getDataGuidanceReviewed() {
        return dataGuidanceReviewed;
    }
    
    public void setDataGuidanceReviewed(Date dataGuidanceReviewed) {
        this.dataGuidanceReviewed = dataGuidanceReviewed;
    }

    public String getPolicyInfo() {
        String retVal = "";
        retVal = this.id + "-" + this.minRetentionPeriod;
        return retVal;
    }
}
