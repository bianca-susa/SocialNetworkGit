package ro.ubb.scs.ir.map.socialnetworkgit.service;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.AbstractFileRepository;

import java.util.Optional;

public interface Service<ID, E extends Entity<ID>>
{
    /**
     *
     * @param id - id-ul entitatii de cautat
     * @return - entitatea cautata, daca gaseste
     * or null altfel
     */
    Optional<E> find(ID id);
    //Optional<E> find(ID id);

    /**
     *
     * @return lista de entitati
     */
    Iterable<E> getAll();

    /**
     *
     * @param entity entitatea de adaugat in lista
     * @return entitatea, daca a fost adaugata
     * or null daca exista deja in lista
     */
    //E add(E entity);
    Optional<E> add(E entity);

    /**
     *
     * @param id id-ul entitatii de sters din lista
     * @return entitatea, daca a fost stearsa cu succes
     * or null daca nu exista in lista initiala
     */
    //E delete(ID id);
    Optional<E> delete(ID id);

    Optional<E> update(E entity);

}
