package com.n26.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Folger on 10/29/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {

    private Long parentId;

    @NotEmpty
    private String type;

    @NotNull
    private Double amount;

    public TransactionDTO() {
    }

    public TransactionDTO(Long parentId, String type, Double amount) {
        this.parentId = parentId;
        this.type = type;
        this.amount = amount;
    }

    public TransactionDTO(String type, Double amount) {
        this.type = type;
        this.amount = amount;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
