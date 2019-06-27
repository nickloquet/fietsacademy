package be.vdab.fietsacademy.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "docenten")
public class Docent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String voornaam;
    private String familienaam;
    private BigDecimal wedde;
    private String emailAdres;
    @Enumerated(EnumType.STRING)
    private Geslacht geslacht;

    public Docent() { }
    public Docent(String voornaam, String familienaam, BigDecimal wedde, String emailAdres, Geslacht geslacht) {
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.wedde = wedde;
        this.emailAdres = emailAdres;
        this.geslacht = geslacht;
    }

    public long getId() {
        return id;
    }
    public String getVoornaam() {
        return voornaam;
    }
    public String getFamilienaam() {
        return familienaam;
    }
    public BigDecimal getWedde() {
        return wedde;
    }
    public String getEmailAdres() {
        return emailAdres;
    }
    public Geslacht getGeslacht() {
        return geslacht;
    }
}

//@Column(naam = "kolomnaam") => als variabele niet hetzelfde als de kolomnaar in de database
//@Transient => variabele die geen kolom in database heeft