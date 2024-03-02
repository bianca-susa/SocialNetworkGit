package ro.ubb.scs.ir.map.socialnetworkgit.observer;

import ro.ubb.scs.ir.map.socialnetworkgit.events.*;

public interface Observable<E extends ro.ubb.scs.ir.map.socialnetworkgit.events.Event>
{
    void addObserver(Observer<E> e);

    void removeObserver(Observer<E> e);

    void notifyObservers(E t);
}
