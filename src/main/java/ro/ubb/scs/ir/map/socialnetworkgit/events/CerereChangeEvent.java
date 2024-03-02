package ro.ubb.scs.ir.map.socialnetworkgit.events;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Message;

public class CerereChangeEvent implements Event
{
    private ChangeEventType type;

    private Cerere data, old;

    public CerereChangeEvent(ChangeEventType type, Cerere data)
    {
        this.data = data;
        this.type = type;
    }

    public CerereChangeEvent(ChangeEventType type, Cerere data, Cerere old)
    {
        this.type = type;
        this.data = data;
        this.old = old;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Cerere getData()
    {
        return data;
    }

    public Cerere getOldData()
    {
        return old;
    }
}

