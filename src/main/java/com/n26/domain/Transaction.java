package com.n26.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Folger on 10/29/2016.
 */
public class Transaction extends Model implements Identifiable {

    private Long parentId;

    @NotEmpty
    private String type;

    @NotNull
    private Double amount;

    private Set<Long> childrenTransactions = new HashSet<>();

    public Transaction(Long id, String type, Double amount) {
        super(id);
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

    public Set<Long> getChildrenTransactions() {
        return childrenTransactions;
    }

    public void setChildrenTransactions(Set<Long> childrenTransactions) {
        this.childrenTransactions = childrenTransactions;
    }
}
