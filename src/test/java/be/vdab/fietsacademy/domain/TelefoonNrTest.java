package be.vdab.fietsacademy.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TelefoonNrTest {
    private TelefoonNr nummer1, nogEensNummer1, nummer2;
    @Before public void before(){
        nummer1 = new TelefoonNr("1", false, "");
        nogEensNummer1 = new TelefoonNr("1", false, "");
        nummer2 = new TelefoonNr("2", false, "");
    }

    @Test public void telefoonNrsZijnGelijkAlsHunNummerGelijkZijn(){
        assertThat(nummer1).isEqualTo(nogEensNummer1);
    }
    @Test public void telefoonNrsZijnVerschillendAlsNummerVerschilt(){
        assertThat(nummer1).isNotEqualTo(nummer2);
    }
    @Test public void eenTelNrVerschiltVanNull(){
        assertThat(nummer1).isNotEqualTo(null);
    }
    @Test public void eenTelNrVerschiltVanEenAnderTypeObject(){
        assertThat(nummer1).isNotEqualTo("");
    }
    @Test public void gelijkeTelNrsGevenDezelfdeHashCode(){
        assertThat(nummer1).hasSameHashCodeAs(nogEensNummer1);
    }
}
