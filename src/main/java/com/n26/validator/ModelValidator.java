package com.n26.validator;

import com.n26.domain.Model;

import java.util.Map;

/**
 * Created by Folger on 10/29/2016.
 */
public interface ModelValidator<T extends Model> {

    public void validate(Map<Long, T> models, T model);

}
