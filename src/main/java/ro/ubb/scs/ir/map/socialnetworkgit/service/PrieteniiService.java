package ro.ubb.scs.ir.map.socialnetworkgit.service;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.* ;
import ro.ubb.scs.ir.map.socialnetworkgit.events.CerereChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType;
import ro.ubb.scs.ir.map.socialnetworkgit.events.PrietenieChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.Observable;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.Observer;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.AbstractFileRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.InMemoryRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.PrieteniiDBRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.PrieteniiRepository;

import java.util.ArrayList;
import java.util.List;

public class PrieteniiService<ID, E extends ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity<ro.ubb.scs.ir.map.socialnetworkgit.domain.Tuple<Long,Long>>> implements Observable<PrietenieChangeEvent>
{
    PrieteniiRepository repo;

    public PrieteniiService(PrieteniiRepository repo) {
        this.repo = repo;
    }


    public ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity find(ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator id1, ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator id2)
    {
        return repo.findOne(id1,id2);
    }

    public Iterable getAll() {
        return repo.findAll();
    }

    public ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity add(ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity entity) {
        ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity o =repo.save(entity);

        if (o==null)
            notifyObservers(new PrietenieChangeEvent(ChangeEventType.ADD, (ro.ubb.scs.ir.map.socialnetworkgit.domain.Prietenie) entity));
        return o;
    }

    public ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity delete(E entity)
    {
        return repo.delete(entity);
    }

    private List<Observer<PrietenieChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<PrietenieChangeEvent> e)
    {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<PrietenieChangeEvent> e)
    {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(PrietenieChangeEvent t)
    {
        observers.stream().forEach(x -> x.update(t));
    }

}
