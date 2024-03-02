package ro.ubb.scs.ir.map.socialnetworkgit.repository;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.*;

import java.util.Optional;

public interface RepositoryPrietenii<E extends Entity<ro.ubb.scs.ir.map.socialnetworkgit.domain.Tuple<ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator, ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator>>>
{
    /**
     *
     * @param u1 -the id of the entity to be returned
     *           id must not be null
     * @param u2 -the id of the entity to be returned
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    //E findOne(Long id1, Long id2);
    E findOne(ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator u1, ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator u2);

    /**
     *
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     *
     * @param entity
     *         entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     * //@throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    E save(E entity);


    /**
     *  removes the entity with the specified id
     * @param entity
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    //E delete(Long id1, Long id2);
    E delete(E entity);

}
