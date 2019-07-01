package be.vdab.fietsacademy.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)         //als meer classes in 1 tabel staan
@Inheritance(strategy = InheritanceType.JOINED)                 //als meer classes apart staan
@Table(name="cursussen")
//@DiscriminatorColumn(name = "soort")
public class Cursus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;

    protected Cursus() {
    }
    public Cursus(String naam) {
        this.naam = naam;
    }

    public long getId() {
        return id;
    }
    public String getNaam() {
        return naam;
    }
}
