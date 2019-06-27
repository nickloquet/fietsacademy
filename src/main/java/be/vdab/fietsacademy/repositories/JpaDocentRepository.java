package be.vdab.fietsacademy.repositories;

import be.vdab.fietsacademy.domain.Docent;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class JpaDocentRepository implements DocentRepository {
    private final EntityManager manager;
    public JpaDocentRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Override public Optional<Docent> findById(long id){
//      throw new UnsupportedOperationException();                    initiële waarde voor test
        return Optional.ofNullable(manager.find(Docent.class, id));
    }
    @Override public void create(Docent docent){
        manager.persist(docent);
    }
}
