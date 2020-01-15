package memory;


import domain.Entity;
import validator.ValidationException;
import validator.Validator;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements CrudRepository<ID,E> {
    private Map<ID, E> all;
    private Validator<E> valid;

    public InMemoryRepository(Validator<E> validator) {
        this.valid = validator;
        all = new HashMap<>();
    }

    @Override
    public E findOne(ID id) {
        if(id == null)
            throw new IllegalArgumentException();
        return all.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return all.values();
    }

    @Override
    public E save(E entity) throws ValidationException {
        if (entity == null)
            throw new IllegalArgumentException("Entity is null!\n");
        if (all.containsKey(entity.getId())) {
            return entity;
        }
        valid.validate(entity);
        all.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if (id == null)
            throw new IllegalArgumentException("ID is null!\n");
        return all.remove(id);
    }

    @Override
    public E update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity is null!\n");
        if(!all.containsKey(entity.getId()))
            return entity;
        valid.validate(entity);
        all.replace(entity.getId(), entity);
        return null;
    }
}
