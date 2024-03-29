package be.vdab.fietsacademy.repositories;

import be.vdab.fietsacademy.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/insertCampus.sql")
@Sql("/insertVerantwoordelijkheid.sql")
@Sql("/insertDocent.sql")
@Sql("/insertDocentVerantwoordelijkheid.sql")
@Import(JpaDocentRepository.class)
public class JpaDocentRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired private JpaDocentRepository repository;
    private static final String DOCENTEN = "docenten";
    private Docent docent;
    private Campus campus;

    @Before public void before(){
        campus = new Campus("test", new Adres("test","test","test","test"));
        docent = new Docent("test", "test", BigDecimal.TEN, "test@fietsacademy.be", Geslacht.MAN, campus);
    }

    private long idVanTestMan(){
        return super.jdbcTemplate.queryForObject("select id from docenten where voornaam = 'testM'",
                Long.class);
    }
    private long idVanTestVrouw(){
        return super.jdbcTemplate.queryForObject("select id from docenten where voornaam='testV'",
                Long.class);
    }

    @Test public void findById(){
        assertThat(repository.findById(idVanTestMan()).get().getVoornaam()).isEqualTo("testM");
    }
    @Test public void findOnbestaandeId(){
        assertThat(repository.findById(-1)).isNotPresent();
    }
    @Test public void man(){
        assertThat(repository.findById(idVanTestMan()).get().getGeslacht()).isEqualTo(Geslacht.MAN);
    }
    @Test public void vrouw(){
        assertThat(repository.findById(idVanTestVrouw()).get().getGeslacht()).isEqualTo(Geslacht.VROUW);
    }
    @Test public void create(){
        manager.persist(campus);
        repository.create(docent);
        manager.flush();
        assertThat(docent.getId()).isPositive();
        assertThat(super.countRowsInTableWhere(DOCENTEN, "id=" + docent.getId())).isOne();
        assertThat(super.jdbcTemplate.queryForObject(
                "select campusid from docenten where id=?", Long.class, docent.getId()))
                .isEqualTo(campus.getId());
        assertThat(campus.getDocenten()).contains(docent);
    }

    @Autowired private EntityManager manager;
    @Test public void delete(){
        long id= idVanTestMan();
        repository.delete(id);
        manager.flush();
        assertThat(super.countRowsInTableWhere(DOCENTEN, "id=" + id)).isZero();
    }
    @Test public void findAll(){
        assertThat(repository.findAll()).hasSize(super.countRowsInTable(DOCENTEN))
        .extracting(docent -> docent.getWedde()).isSorted();
    }
    @Test public void findWeddeBetween(){
        BigDecimal duizend = BigDecimal.valueOf(1_000);
        BigDecimal tweeduizend = BigDecimal.valueOf(2_000);
        List<Docent> docenten = repository.findByWeddeBetween(duizend, tweeduizend);
        manager.clear();
        assertThat(docenten).hasSize(super.countRowsInTableWhere(
                DOCENTEN, "wedde between 1000 and 2000"))
                .allSatisfy(docent -> assertThat(docent.getWedde()).isBetween(duizend, tweeduizend));
        assertThat(docenten).extracting(docent -> docent.getCampus().getNaam());
    }
    @Test public void findEmailAdressen(){
        assertThat(repository.findEmailAdressen())
                .hasSize(super.jdbcTemplate.queryForObject(
                        "select count(distinct emailadres) from docenten", Integer.class))
                .allSatisfy(adres -> assertThat(adres).contains("@"));
    }
    @Test public void findIdsEnEmail(){
        assertThat(repository.findIdsEnEmail()).hasSize(super.countRowsInTable(DOCENTEN));
    }
    @Test public void findGrootsteWedde(){
        assertThat(repository.findGrootsteWedde()).isEqualByComparingTo(
                super.jdbcTemplate.queryForObject("select max(wedde) from docenten", BigDecimal.class));
    }
    @Test public void findAantalDocentenPerWedde(){
        BigDecimal duizend = BigDecimal.valueOf(1_000);
        assertThat(repository.findAantalDocentenPerWedde()).hasSize(super.jdbcTemplate.queryForObject(
                "select count(distinct wedde) from docenten", Integer.class))
                .filteredOn(aantalWedde -> aantalWedde.getWedde().compareTo(duizend) == 0)
                .allSatisfy(aantalWedde -> assertThat(aantalWedde.getAantal())
                .isEqualTo(super.countRowsInTableWhere(DOCENTEN, "wedde=1000")));
    }
    @Test public void algemeneOpslag(){
        assertThat(repository.algemeneOpslag(BigDecimal.TEN)).isEqualTo(super.countRowsInTable(DOCENTEN));
        assertThat(super.jdbcTemplate.queryForObject(
                "select wedde from docenten where id=?", BigDecimal.class, idVanTestMan()))
                .isEqualByComparingTo("1100");
    }
    @Test public void bijnamenLezen(){
        assertThat(repository.findById(idVanTestMan()).get().getBijnamen()).containsOnly("test");
    }
    @Test public void bijnaamToevoegen(){
        manager.persist(campus);
        repository.create(docent);
        docent.addBijnaam("test");
        manager.flush();
        assertThat(super.jdbcTemplate.queryForObject(
                "select bijnaam from docentenbijnamen where docentid=?",
                String.class, docent.getId())).isEqualTo("test");
    }
    @Test public void campusLazyLoaded(){
        Docent docent = repository.findById(idVanTestMan()).get();
        assertThat(docent.getCampus().getNaam()).isEqualTo("test");
    }
    @Test public void verantwoordelijkhedenLezen(){
        assertThat(repository.findById(idVanTestMan()).get().getVerantwoordelijkheden())
                .containsOnly(new Verantwoordelijkheid("test"));
    }
    @Test public void verantwoordelijkheidToevoegen(){
        Verantwoordelijkheid verantwoordelijkheid = new Verantwoordelijkheid("test2");
        manager.persist(verantwoordelijkheid);
        manager.persist(campus);
        repository.create(docent);
        docent.add(verantwoordelijkheid);
        manager.flush();
        assertThat(super.jdbcTemplate.queryForObject(
                "select verantwoordelijkheidid from docentenverantwoordelijkheden " +
                        "where docentid=?", Long.class, docent.getId()).longValue())
                        .isEqualTo(verantwoordelijkheid.getId());
    }
}
