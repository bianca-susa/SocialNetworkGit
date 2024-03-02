package ro.ubb.scs.ir.map.socialnetworkgit.events;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;

public class UtilizatorChangeEvent implements ro.ubb.scs.ir.map.socialnetworkgit.events.Event
{
    private ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType type;

    private Utilizator data, old;

    public UtilizatorChangeEvent(ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType type, Utilizator data)
    {
        this.data = data;
        this.type = type;
    }

    public UtilizatorChangeEvent(ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType type, Utilizator data, Utilizator old)
    {
        this.type = type;
        this.data = data;
        this.old = old;
    }

    public ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType getType() {
        return type;
    }

    public Utilizator getData()
    {
        return data;
    }

    public Utilizator getOldData()
    {
        return old;
    }
}
