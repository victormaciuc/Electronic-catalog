package business;

import domain.Entity;
import memory.InMemoryRepository;

public class ServiceEntity<ID, E extends Entity<ID>> {

    private InMemoryRepository<ID, E> repo;

    public ServiceEntity(InMemoryRepository<ID, E> repo) {
        this.repo = repo;
    }

    public InMemoryRepository<ID, E> getRepo() {
        return repo;
    }

    public Iterable<E> getAll() {
        return repo.findAll();
    }

}
