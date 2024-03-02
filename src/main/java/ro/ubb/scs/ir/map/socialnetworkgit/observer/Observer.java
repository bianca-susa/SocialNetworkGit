package ro.ubb.scs.ir.map.socialnetworkgit.observer;

import ro.ubb.scs.ir.map.socialnetworkgit.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
