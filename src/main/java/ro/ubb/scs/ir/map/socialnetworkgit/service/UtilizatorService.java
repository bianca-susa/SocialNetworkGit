package ro.ubb.scs.ir.map.socialnetworkgit.service;

import ro.ubb.scs.ir.map.socialnetworkgit.PasswordHashing;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType;
import ro.ubb.scs.ir.map.socialnetworkgit.events.UtilizatorChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.InMemoryRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.UtilizatorDBRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.*;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Page;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Pageable;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.PagingRepository;
//import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Page;
//import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Pageable;
//import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.PagingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilizatorService<ID, E extends Entity<ID>> implements ro.ubb.scs.ir.map.socialnetworkgit.service.Service, ro.ubb.scs.ir.map.socialnetworkgit.observer.Observable<UtilizatorChangeEvent>
{
    static UtilizatorDBRepository repo;

    public UtilizatorService(UtilizatorDBRepository repo) {
        this.repo = repo;
    }

    public Page<Utilizator> findAllOnPage(Pageable pageable) {
        return repo.findAllOnPage(pageable);
    }

    public static void savePassword(String password, Utilizator utilizator)
    {
        repo.savePassword(password, utilizator);
    }

    public static boolean checkPassword(Utilizator u, String entered_password)
    {
        String hashedPassword = repo.getUserPassword(u);
        String userPassword = PasswordHashing.hashPassword(entered_password);

        return hashedPassword.equals(userPassword);
    }

    public String getUserPassword(Utilizator utilizator)
    {
        return repo.getUserPassword(utilizator);
    }
    @Override
    public Optional<E> find(Object o) {
        return (Optional<E>) repo.findOne(o);
    }


    @Override
    public Iterable<E> getAll() {
        return (Iterable<E>) repo.findAll();
    }


    @Override
    public Optional<E> add(Entity entity)
    {
        //return repo.save(entity);

        Optional o = repo.save(entity);
        Utilizator u = (Utilizator) o.orElse(new Utilizator("Default","User"));
        if (o.isEmpty())
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.ADD, u));
        return o;
    }

    public void add_p(Long id1, Long id2)
    {
        repo.add_p(id1,id2);
    }

    public void delete_p(Long id1, Long id2)
    {
        repo.delete_p(id1,id2);
    }


    @Override
    public Optional<E> delete(Object o)
    {
        //return repo.delete(o);
        Optional del = repo.delete(o);
        Utilizator u = (Utilizator) del.orElse(new Utilizator("Default","User"));

        if(del.isPresent())
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.DELETE, u));
        return del;
    }

    @Override
    public Optional update(Entity entity) {
        Optional o = repo.update(entity);
        Utilizator u = (Utilizator) o.orElse(new Utilizator("Default","User"));

        if(o.isEmpty())
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.UPDATE, u));
        return o;
    }
    private List<ro.ubb.scs.ir.map.socialnetworkgit.observer.Observer<UtilizatorChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(ro.ubb.scs.ir.map.socialnetworkgit.observer.Observer<UtilizatorChangeEvent> e)
    {
        observers.add(e);
    }

    @Override
    public void removeObserver(ro.ubb.scs.ir.map.socialnetworkgit.observer.Observer<UtilizatorChangeEvent> e)
    {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorChangeEvent t)
    {
        observers.stream().forEach(x -> x.update(t));
    }

}
