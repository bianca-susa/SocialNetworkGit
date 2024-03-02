package ro.ubb.scs.ir.map.socialnetworkgit.service;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Message;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType;
import ro.ubb.scs.ir.map.socialnetworkgit.events.MessageChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.events.UtilizatorChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.Observable;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.Observer;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.MessageDBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class MessageService <ID, E extends Entity<ID>> implements Service, Observable<MessageChangeEvent> {
    MessageDBRepository repo;

    public MessageService(MessageDBRepository repo) {
        this.repo = repo;
    }


    @Override
    public Optional find(Object o) {
        return Optional.empty();
    }

    public List<Message> getMessages(Utilizator u1, Utilizator u2)
    {
        return repo.getMessages(u1,u2);
    }

    @Override
    public Iterable getAll() {
        return repo.findAll();
    }

    @Override
    public Optional add(Entity entity) {
        Optional o = repo.save(entity);
        Message u = null;
        try {
            u = (Message) o.orElseThrow(()->new NoSuchElementException("No message!"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        if (!o.isEmpty())
            notifyObservers(new MessageChangeEvent(ChangeEventType.ADD, u));
        //return repo.save(entity);
        return o;
    }

    @Override
    public Optional delete(Object o) {
        return Optional.empty();
    }

    @Override
    public Optional update(Entity entity) {
        return Optional.empty();
    }

    private List<Observer<MessageChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<MessageChangeEvent> e)
    {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageChangeEvent> e)
    {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageChangeEvent t)
    {
        observers.stream().forEach(x -> x.update(t));
    }
}
