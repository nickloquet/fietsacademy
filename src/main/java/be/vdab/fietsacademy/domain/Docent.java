package be.vdab.fietsacademy.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "docenten")
public class Docent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id private long id;
    private String voornaam;
    private String familienaam;
    private BigDecimal wedde;
    private String emailAdres;
    @Enumerated(EnumType.STRING)
    private Geslacht geslacht;

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