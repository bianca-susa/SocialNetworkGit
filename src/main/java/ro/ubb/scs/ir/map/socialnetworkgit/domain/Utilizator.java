package ro.ubb.scs.ir.map.socialnetworkgit.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Utilizator extends ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity<Long>
{
    private String first_name;
    private String last_name;

    private List<Utilizator> friends;

    private String password;

    public Utilizator(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
        friends = new ArrayList<Utilizator>();
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public List<Utilizator> getFriends() {
        return friends;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void addFriend(Utilizator u)
    {
        if(!this.friends.contains(u))
            friends.add(u);
    }

    public void removeFriend(Utilizator u)
    {
        friends.remove(u);
    }


    @Override
    public String toString() {
        return first_name + " " + last_name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilizator that = (Utilizator) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
