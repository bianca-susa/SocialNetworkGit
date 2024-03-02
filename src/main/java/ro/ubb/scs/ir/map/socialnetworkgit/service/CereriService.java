package ro.ubb.scs.ir.map.socialnetworkgit.service;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Tuple;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.events.CerereChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType;
import ro.ubb.scs.ir.map.socialnetworkgit.events.MessageChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.events.UtilizatorChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.Observable;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.Observer;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.CerereDBRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.MessageDBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CereriService<ID, E extends Entity<Tuple<Long,Long>>> implements Observable<CerereChangeEvent> {
    CerereDBRepository repo;

    public CereriService(CerereDBRepository repo) {
        this.repo = repo;
    }


    public Iterable getAll() {
        return repo.findAll();
    }

    public E add(Entity entity) {
        E o = (E) repo.save(entity);
        if (o==null)
            notifyObservers(new CerereChangeEvent(ChangeEventType.ADD, (Cerere) entity));
        return o;
    }

    public E findOne(Utilizator u1, Utilizator u2)
    {
        return (E) repo.findOne(u1,u2);
    }

    public E updateRequest(Entity entity)
    {
        E cerere = (E) repo.updateRequest(entity);

        if(cerere != null)
            notifyObservers(new CerereChangeEvent(ChangeEventType.UPDATE, (Cerere) entity));

        return cerere;
    }

    private List<Observer<CerereChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<CerereChangeEvent> e)
    {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<CerereChangeEvent> e)
    {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(CerereChangeEvent t)
    {
        observers.stream().forEach(x -> x.update(t));
    }

}
