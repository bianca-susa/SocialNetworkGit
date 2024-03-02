package ro.ubb.scs.ir.map.socialnetworkgit.events;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Message;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;

public class MessageChangeEvent implements ro.ubb.scs.ir.map.socialnetworkgit.events.Event
{
    private ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType type;

    private Message data, old;

    public MessageChangeEvent(ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType type, Message data)
    {
        this.data = data;
        this.type = type;
    }

    public MessageChangeEvent(ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType type, Message data, Message old)
    {
        this.type = type;
        this.data = data;
        this.old = old;
    }

    public ro.ubb.scs.ir.map.socialnetworkgit.events.ChangeEventType getType() {
        return type;
    }

    public Message getData()
    {
        return data;
    }

    public Message getOldData()
    {
        return old;
    }
}

