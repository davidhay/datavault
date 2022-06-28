package org.datavaultplatform.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "DepositChunk")
@Entity
@Table(name="DepositChunks")
@NamedEntityGraph(
    name=DepositChunk.EG_DEPOSIT_CHUNK,
    attributeNodes = @NamedAttributeNode(value = DepositChunk_.DEPOSIT, subgraph = "subDeposit"),
    subgraphs = @NamedSubgraph(
        name="subDeposit",
        attributeNodes = {
            @NamedAttributeNode(Deposit_.USER),
            @NamedAttributeNode(Deposit_.VAULT)
   })
)
public class DepositChunk {

    public static final String EG_DEPOSIT_CHUNK = "eg.DepositChunk.1";
    // Deposit Identifier
    @Id
    @ApiObjectField(description = "Universally Unique Identifier for the Deposit Path", name="Deposit Path")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true, length = 36)
    private String id;
    
    @JsonIgnore
    @ManyToOne
    private Deposit deposit;
    
    // Record the file path that the user selected for this deposit.
    @ApiObjectField(description = "Filepath of the origin deposit")
    @Column(columnDefinition = "INT")
    private int chunkNum;
    
    // Hash of the deposit chunk
    @Column(columnDefinition = "TEXT")
    private String archiveDigest;
    @Column(columnDefinition = "TEXT")
    private String archiveDigestAlgorithm;
    
    // Encryption
    @Column(columnDefinition = "BLOB")
    private byte[] encIV;
    @Column(columnDefinition = "TEXT")
    private String ecnArchiveDigest;

    public DepositChunk() {}
    public DepositChunk(Deposit deposit, int chunkNum, String archiveDigest, String archiveDigestAlgorithm) {
        this.deposit = deposit;
        this.chunkNum = chunkNum;
        this.archiveDigest = archiveDigest;
        this.archiveDigestAlgorithm = archiveDigestAlgorithm;
    }

    public String getID() { return id; }
    
    public int getChunkNum() {
        return chunkNum;
    }
    
    public void setChunkNum(int chunkNum) {
        this.chunkNum = chunkNum;
    }
    
    public String getArchiveDigest() {
        return archiveDigest;
    }
    
    public void setArchiveDigest(String archiveDigest) {
        this.archiveDigest = archiveDigest;
    }
    
    public String getArchiveDigestAlgorithm() {
        return archiveDigestAlgorithm;
    }
    
    public void setArchiveDigestAlgorithm(String archiveDigestAlgorithm) {
        this.archiveDigestAlgorithm = archiveDigestAlgorithm;
    }
    
    public byte[] getEncIV() {
        return encIV;
    }
    
    public void setEncIV(byte[] encIV) {
        this.encIV = encIV;
    }
    
    public String getEcnArchiveDigest() {
        return ecnArchiveDigest;
    }
    
    public void setEcnArchiveDigest(String ecnArchiveDigest) {
        this.ecnArchiveDigest = ecnArchiveDigest;
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit){
        this.deposit = deposit;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        DepositChunk rhs = (DepositChunk) obj;
        return new EqualsBuilder()
            .append(this.id, rhs.id).isEquals();
    }

    @Override
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(17, 37).
            append(id).toHashCode();
    }

}
