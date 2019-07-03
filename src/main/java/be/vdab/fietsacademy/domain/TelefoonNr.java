package be.vdab.fietsacademy.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TelefoonNr implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nummer;
    private boolean fax;
    private String opmerking;

    protected TelefoonNr() {
    }
    public TelefoonNr(String nummer, boolean fax, String opmerking) {
        this.nummer = nummer;
        this.fax = fax;
        this.opmerking = opmerking;
    }

    public String getNummer() {
        return nummer;
    }
    public boolean isFax() {
        return fax;
    }
    public String getOpmerking() {
        return opmerking;
    }

    @Override public boolean equals(Object object){
        if(!(object instanceof TelefoonNr)){
            return false;
        }
        TelefoonNr tel = (TelefoonNr) object;
        return nummer.equalsIgnoreCase(tel.nummer);
    }
    @Override public int hashCode(){
        return nummer.toUpperCase().hashCode();
    }
}
