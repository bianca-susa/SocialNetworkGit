package ro.ubb.scs.ir.map.socialnetworkgit.domain;

import java.util.Objects;

public class Cerere extends Entity<Tuple<Utilizator,Utilizator>>{

    private String status;

    public Cerere() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Cerere{" +
                "u1=" + this.getId().getLeft() +
                ", u2=" + this.getId().getRight() +
                ", status='" + status + '\'' +
                '}';
    }

}
