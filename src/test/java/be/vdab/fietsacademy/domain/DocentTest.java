package be.vdab.fietsacademy.domain;

import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

public class DocentTest {
    private final static BigDecimal WEDDE = BigDecimal.valueOf(200);
    private Docent docent1;

    @Before public void before(){
        docent1 = new Docent("test", "test", WEDDE, "test@fietsacademy.be", Geslacht.MAN);
    }

    @Test public void opslag(){
        docent1.opslag(BigDecimal.TEN);
        assertThat(docent1.getWedde()).isEqualByComparingTo("220");
    }
    @Test public void opslagMetNullMislukt(){
        assertThatNullPointerException().isThrownBy(()->docent1.opslag(null));
    }
    @Test public void opslagMet0Mislukt(){
        assertThatIllegalArgumentException().isThrownBy(()->docent1.opslag(BigDecimal.ZERO));
    }
    @Test public void negatieveOpslagMislukt(){
        assertThatIllegalArgumentException().isThrownBy(()->docent1.opslag(BigDecimal.valueOf(-1)));
    }
    @Test public void nieuweDocentHeeftGeenBijnamen(){
        assertThat(docent1.getBijnamen()).isEmpty();
    }
    @Test public void bijnaamToevoegen(){
        assertThat(docent1.addBijnaam("test")).isTrue();
        assertThat(docent1.getBijnamen()).containsOnly("test");
    }
    @Test public void tweeDezelfdeBijnaamMislukt(){
        docent1.addBijnaam("test");
        assertThat(docent1.addBijnaam("test")).isFalse();
        assertThat(docent1.getBijnamen()).containsOnly("test");
    }
    @Test public void nullBijnaamMislukt(){
        assertThatNullPointerException().isThrownBy(()->docent1.addBijnaam(null));
    }
    @Test public void legeBijnaamMislukt(){
        assertThatIllegalArgumentException().isThrownBy(()->docent1.addBijnaam(""));
    }
    @Test public void enkelSpatiesMislukt(){
        assertThatIllegalArgumentException().isThrownBy(()->docent1.addBijnaam(" "));
    }
    @Test public void bijnaamVerwijderen(){
        docent1.addBijnaam("test");
        assertThat(docent1.removeBijnaam("test")).isTrue();
        assertThat(docent1.getBijnamen()).isEmpty();
    }
    @Test public void nietGevondenVerwijderenMislukt(){
        docent1.addBijnaam("test");
        assertThat(docent1.removeBijnaam("test2")).isFalse();
        assertThat(docent1.getBijnamen()).containsOnly("test");
    }
}
