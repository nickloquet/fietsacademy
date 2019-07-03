package be.vdab.fietsacademy.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "docenten")
//@NamedQuery(name = "Docent.findByWeddeBetween",
//        query = "select d from Docent d where d.wedde between :van and :tot order by d.wedde, d.id")
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
    @ElementCollection
    @CollectionTable(name = "docentenbijnamen", joinColumns = @JoinColumn(name = "docentid"))
    @Column(name = "bijnaam")
    private Set<String> bijnamen;

    protected Docent() {
    }
    public Docent(String voornaam, String familienaam, BigDecimal wedde, String emailAdres, Geslacht geslacht) {
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.wedde = wedde;
        this.emailAdres = emailAdres;
        this.geslacht = geslacht;
        this.bijnamen = new LinkedHashSet<>();
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
    public Set<String> getBijnamen() {
//      return bijnamen;
        return Collections.unmodifiableSet(bijnamen);
    }

    public void opslag(BigDecimal percentage){
        if(percentage.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException();
        }
        BigDecimal factor = BigDecimal.ONE.add(percentage.divide(BigDecimal.valueOf(100)));
        wedde = wedde.multiply(factor, new MathContext(2, RoundingMode.HALF_UP));
    }
    public boolean addBijnaam(String bijnaam){
        if(bijnaam.trim().isEmpty()){
            throw new IllegalArgumentException();
        }
        return bijnamen.add(bijnaam);
    }
    public boolean removeBijnaam(String bijnaam){
        return bijnamen.remove(bijnaam);
    }
}

//@Column(naam = "kolomnaam") => als variabele niet hetzelfde als de kolomnaar in de database
//@Transient => variabele die geen kolom in database heeft