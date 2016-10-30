package com.n26.repository;

import com.n26.domain.Model;
import com.n26.validator.ModelValidator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Folger on 10/29/2016.
 */
public class AbstractRepository<T extends Model> {

    protected Map<Long, T> models = new ConcurrentHashMap<>();

    private ModelValidator<T> modelValidator;

    public AbstractRepository() {
    }

    public void setModelValidator(ModelValidator<T> modelValidator) {
        this.modelValidator = modelValidator;
    }

    public T save(T object) {
        Objects.requireNonNull(object);
        modelValidator.validate(models, object);
        models.put(object.getId(), object);
        return object;
    }

    public Optional<T> findById(Long id) {
        Objects.requireNonNull(id);
        return Optional.ofNullable(models.get(id));
    }
}
