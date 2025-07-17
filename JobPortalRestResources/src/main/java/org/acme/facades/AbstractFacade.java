package org.acme.facades;

import jakarta.persistence.EntityManager;

import java.util.List;

public abstract class AbstractFacade<T> {
    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // Subclass must provide the entity manager
    protected abstract EntityManager getEntityManager();

    // Create
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    // Update
    public void edit(T entity) {
    }

    // Delete
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    // Find by ID
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    // Find all
    public List<T> findAll() {
        return getEntityManager()
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }

    // Count
    public long count() {
        return getEntityManager()
                .createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class)
                .getSingleResult();
    }
}
