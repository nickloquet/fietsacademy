package be.vdab.fietsacademy.repositories;

import be.vdab.fietsacademy.domain.Docent;
import be.vdab.fietsacademy.queryresult.AantalDocentenPerWedde;
import be.vdab.fietsacademy.queryresult.IdEnEmailAdres;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
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
    @Override public void delete(long id){
        findById(id).ifPresent(docent -> manager.remove(docent));
    }
    @Override public List<Docent> findAll(){
        return manager.createQuery("select d from Docent d order by d.wedde", Docent.class).getResultList();
    }
    @Override public List<Docent> findByWeddeBetween(BigDecimal van, BigDecimal tot){
//        return manager.createQuery(
//                "select d from Docent d where d.wedde between :van and :tot", Docent.class)
          return manager.createNamedQuery("Docent.findByWeddeBetween", Docent.class)        //met named query
                .setParameter("van", van)
                .setParameter("tot", tot)
                .getResultList();
    }
    @Override public List<String> findEmailAdressen(){
        return manager.createQuery(
                "select d.emailAdres from Docent d", String.class).getResultList();
    }
    @Override public List<IdEnEmailAdres> findIdsEnEmail(){
        return manager.createQuery(
                "select new be.vdab.fietsacademy.queryresult.IdEnEmailAdres(d.id, d.emailAdres)" +
                        " from Docent d", IdEnEmailAdres.class).getResultList();
    }
    @Override public BigDecimal findGrootsteWedde(){
        return manager.createQuery(
                "select max(d.wedde) from Docent d", BigDecimal.class).getSingleResult();
    }
    @Override public List<AantalDocentenPerWedde> findAantalDocentenPerWedde(){
        return manager.createQuery(
                "select new be.vdab.fietsacademy.queryresult.AantalDocentenPerWedde(" +
                "d.wedde, count(d)) from Docent d group by d.wedde",
                AantalDocentenPerWedde.class).getResultList();
    }
}
