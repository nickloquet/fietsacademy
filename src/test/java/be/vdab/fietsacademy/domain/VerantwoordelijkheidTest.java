package be.vdab.fietsacademy.domain;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class VerantwoordelijkheidTest {
    private Verantwoordelijkheid verantwoordelijkheid;
    private Docent docent;
    private Campus campus;

    @Before public void before(){
        verantwoordelijkheid = new Verantwoordelijkheid("EHBO");
        campus = new Campus("test",new Adres("test","test","test","test"));
        docent = new Docent("test", "test", BigDecimal.TEN, "test@fietsacademy.be", Geslacht.MAN, campus);
    }

    @Test public void docentToevoegen(){
        assertThat(verantwoordelijkheid.add(docent)).isTrue();
        assertThat(verantwoordelijkheid.getDocenten()).hasSize(1);
        assertThat(verantwoordelijkheid.getDocenten()).contains(docent);
        assertThat(docent.getVerantwoordelijkheden()).hasSize(1);
        assertThat(docent.getVerantwoordelijkheden()).contains(verantwoordelijkheid);
    }
    @Test public void docentVerwijderen(){
        assertThat(verantwoordelijkheid.add(docent)).isTrue();
        assertThat(verantwoordelijkheid.remove(docent)).isTrue();
        assertThat(verantwoordelijkheid.getDocenten()).isEmpty();
        assertThat(docent.getVerantwoordelijkheden()).isEmpty();
    }
}
