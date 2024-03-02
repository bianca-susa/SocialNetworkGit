package ro.ubb.scs.ir.map.socialnetworkgit.repository;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Tuple;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.ValidationException;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.Validator;

import java.util.*;


public class PrieteniiRepository<E extends Entity<Tuple<Utilizator,Utilizator>>> implements RepositoryPrietenii<E>
{
    List<E> entities;
    private Validator<E> validator;

    public PrieteniiRepository(Validator<E> validator) {
        this.validator = validator;
        this.entities = new ArrayList<E>();
    }

    @Override
    public E findOne(Utilizator u1, Utilizator u2) {
        Tuple tuplu = new Tuple(u1,u2);
        Entity entity = new Entity<Tuple>();
        entity.setId(tuplu);
        if(entities.contains(entity))
            return (E) entity;
        return null;
    }

    @Override
    public Iterable<E> findAll() {
        return entities;
    }

    @Override
    public E save(E entity) {
        try
        {
            validator.validate(entity);
            if(this.findOne(entity.getId().getLeft(), entity.getId().getRight()) == null
                    && this.findOne(entity.getId().getRight(), entity.getId().getLeft()) == null)
            {
                entities.add(entity);
                return entity;
            }
            return null;
        }
        catch (ValidationException exception)
        {
            System.out.println(exception);
            return null;
        }
    }

    @Override
    //public E delete(Long id1, Long id2) {
    public E delete(E entity){
        //E entity = findOne(id1,id2);
        try
        {
            validator.validate(entity);
            E entity_delete = findOne(entity.getId().getLeft(), entity.getId().getRight());
            if(entity_delete != null)
            {
                entities.remove(entity_delete);
                return entity_delete;}
            return null;
        }
        catch(ValidationException exception)
        {
            System.out.println(exception);
        }
        return null;
    }
}
