package com.n26.domain;

/**
 * Created by Folger on 10/29/2016.
 */
public class Model {

    protected Long Id;

    public Model(Long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
