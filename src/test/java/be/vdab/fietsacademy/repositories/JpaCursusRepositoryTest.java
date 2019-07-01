package be.vdab.fietsacademy.repositories;

import be.vdab.fietsacademy.domain.Cursus;
import be.vdab.fietsacademy.domain.GroepsCursus;
import be.vdab.fietsacademy.domain.IndividueleCursus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaCursusRepository.class)
@Sql("/insertCursus.sql")
public class JpaCursusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String CURSUSSEN = "cursussen";
    private static final LocalDate DATUM = LocalDate.of(2019,1,1);
    @Autowired private JpaCursusRepository repository;
    private static final String GROEP = "groepscursussen";
    private static final String INDIVIDUEEL = "individuelecursussen";

    private long idTestGroepCursus(){
        return super.jdbcTemplate.queryForObject(
                "select id from cursussen where naam='testGroep'", Long.class);
    }
    private long idTestIndividueelCursus(){
        return super.jdbcTemplate.queryForObject(
                "select id from cursussen where naam='testIndividueel'", Long.class);
    }

    @Test public void findGroepCursusById(){
        Optional<Cursus> optionalCursus = repository.findById(idTestGroepCursus());
        assertThat(optionalCursus.get().getNaam()).isEqualTo("testGroep");
    }
    @Test public void findIndividueelCursusById(){
        Optional<Cursus> optionalCursus = repository.findById(idTestIndividueelCursus());
        assertThat(optionalCursus.get().getNaam()).isEqualTo("testIndividueel");
    }
    @Test public void findByOnbestaandeId(){
        assertThat(repository.findById(-1)).isNotPresent();
    }
    @Test public void createGroep(){
        GroepsCursus cursus = new GroepsCursus("testGroep2", DATUM, DATUM);
        repository.create(cursus);
        assertThat(super.countRowsInTableWhere(CURSUSSEN, "id='"+ cursus.getId()+ "'")).isOne();
        assertThat(super.countRowsInTableWhere(GROEP, "id='"+cursus.getId()+"'")).isOne();
    }
    @Test public void createIndividueel(){
        IndividueleCursus cursus = new IndividueleCursus("testIndividueel2",7);
        repository.create(cursus);
        assertThat(super.countRowsInTableWhere(CURSUSSEN, "id='"+cursus.getId()+"'")).isOne();
        assertThat(super.countRowsInTableWhere(INDIVIDUEEL, "id='"+cursus.getId()+"'")).isOne();
    }
}
