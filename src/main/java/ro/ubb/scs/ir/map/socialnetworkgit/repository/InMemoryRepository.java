package ro.ubb.scs.ir.map.socialnetworkgit.repository;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Page;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Pageable;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.PagingRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.ValidationException;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E>
{
    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    public void add_p(Long id1,Long id2)
    {
        Utilizator u1 = (Utilizator) entities.get(id1);
        Utilizator u2 = (Utilizator) entities.get(id2);

        u1.addFriend(u2);
        u2.addFriend(u1);
    }

    public void delete_p(Long id1,Long id2)
    {
        Utilizator u1 = (Utilizator) entities.get(id1);
        Utilizator u2 = (Utilizator) entities.get(id2);

        u1.removeFriend(u2);
        u2.removeFriend(u1);
    }

    @Override
    public Optional findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }


    @Override
    public Optional<E> save(E entity)
    {
        if(entity == null)
            throw new IllegalArgumentException("entity must not be null");
        try
        {
            validator.validate(entity);
        }
        catch(ValidationException exception)
        {
            System.out.println(exception);
        }

        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    public Optional<E> delete(ID id)
    {
        if(id == null)
            throw new IllegalArgumentException("id must not be null");
        return Optional.ofNullable(entities.remove(id));
    }
    @Override
    public Optional<E> update(E entity)
    {
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return Optional.empty();
        }
        return Optional.of(entity);

    }

}
