package ro.ubb.scs.ir.map.socialnetworkgit.events;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Prietenie;

public class PrietenieChangeEvent implements ro.ubb.scs.ir.map.socialnetworkgit.events.Event
{
    private ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType type;

    private Prietenie data, old;

    public PrietenieChangeEvent(ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType type, Prietenie data)
    {
        this.data = data;
        this.type = type;
    }

    public PrietenieChangeEvent(ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType type, Prietenie data, Prietenie old)
    {
        this.type = type;
        this.data = data;
        this.old = old;
    }

    public ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType getType() {
        return type;
    }

    public Prietenie getData()
    {
        return data;
    }

    public Prietenie getOldData()
    {
        return old;
    }
}

