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
@NamedEntityGraph(name = "Docent.metCampus", attributeNodes = @NamedAttributeNode("campus"))
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "campusid")
    private Campus campus;
    @ManyToMany(mappedBy = "docenten")
    private Set<Verantwoordelijkheid> verantwoordelijkheden = new LinkedHashSet<>();


    protected Docent() {
    }
    public Docent(String voornaam, String familienaam, BigDecimal wedde, String emailAdres, Geslacht geslacht, Campus campus) {
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.wedde = wedde;
        this.emailAdres = emailAdres;
        this.geslacht = geslacht;
        this.bijnamen = new LinkedHashSet<>();
        setCampus(campus);
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
    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        if(!campus.getDocenten().contains(this)){
            campus.add(this);
        }
        this.campus = campus;
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

    @Override public boolean equals(Object object){
        if(!(object instanceof Docent)){
            return false;
        }
        if(emailAdres == null){
            return false;
        }
        return emailAdres.equalsIgnoreCase(((Docent) object).emailAdres);
    }
    @Override public int hashCode(){
        return emailAdres == null ? 0 : emailAdres.toLowerCase().hashCode();
    }

    public boolean add(Verantwoordelijkheid verantwoordelijkheid){
        boolean toegevoegd = verantwoordelijkheden.add(verantwoordelijkheid);
        if(! verantwoordelijkheid.getDocenten().contains(this)){
            verantwoordelijkheid.add(this);
        }
        return toegevoegd;
    }
    public boolean remove(Verantwoordelijkheid verantwoordelijkheid){
        boolean verwijderd = verantwoordelijkheden.remove(verantwoordelijkheid);
        if(verantwoordelijkheid.getDocenten().contains(this)){
            verantwoordelijkheid.remove(this);
        }
        return verwijderd;
    }
    public Set<Verantwoordelijkheid> getVerantwoordelijkheden(){
        return Collections.unmodifiableSet(verantwoordelijkheden);
    }
}

//@Column(naam = "kolomnaam") => als variabele niet hetzelfde als de kolomnaam in de database
//@Transient => variabele die geen kolom in database heeft

// graph met meer geassocieerde entities
//@NamedEntityGraph(name = "Docent.metCampusEnVerantwoordelijkheden",
//        attributeNodes = { @NamedAttributeNode("campus"), @NamedAttributeNode("verantwoordelijkheden")})

// graph met geassocieerde entity van een geassocieerde entity
//@NamedEntityGraph(name = "Docent.metCampusEnManager",
//    attributeNodes = @NamedAttributeNode(value = "campus", subgraph = "metManager"),
//    subgraphs = @NamedSubgraph(name = "metManager", attributeNodes = @NamedAttributeNode("manager")))