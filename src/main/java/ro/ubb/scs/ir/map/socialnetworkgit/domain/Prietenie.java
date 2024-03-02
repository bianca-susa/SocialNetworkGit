package ro.ubb.scs.ir.map.socialnetworkgit.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
public class Prietenie extends ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity<Tuple<Utilizator,Utilizator>>
{
    LocalDate friendsFrom;

    public Prietenie(LocalDate friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public LocalDate getDate() {return friendsFrom;}

    public void setDate(LocalDate date) {
        this.friendsFrom = date;
    }

    public String getU1_name()
    {
        return this.getId().getLeft().getFirst_name();
    }

    public String getU2_name()
    {
        return this.getId().getRight().getFirst_name();
    }
}
