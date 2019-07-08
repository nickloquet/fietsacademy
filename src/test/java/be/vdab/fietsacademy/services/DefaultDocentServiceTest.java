package be.vdab.fietsacademy.services;

import be.vdab.fietsacademy.domain.Adres;
import be.vdab.fietsacademy.domain.Campus;
import be.vdab.fietsacademy.domain.Docent;
import be.vdab.fietsacademy.domain.Geslacht;
import be.vdab.fietsacademy.exceptions.DocentNietGevondenException;
import be.vdab.fietsacademy.repositories.DocentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDocentServiceTest {
    private DefaultDocentService service;
    @Mock private DocentRepository repository;
    private Docent docent;
    private Campus campus;

    @Before public void before(){
        campus = new Campus("test", new Adres("test","test","test","test"));
        docent = new Docent("test", "test", BigDecimal.valueOf(100),
                "test@fietsacademy.be", Geslacht.MAN, campus);
        when(repository.findById(1)).thenReturn(Optional.of(docent));
        when(repository.findById(-1)).thenReturn(Optional.empty());
        service = new DefaultDocentService(repository);
    }

    @Test public void opslag(){
        service.opslag(1, BigDecimal.TEN);
        assertThat(docent.getWedde()).isEqualByComparingTo("110");
        verify(repository).findById(1);
    }
    @Test public void opslagOnbestaand(){
        assertThatExceptionOfType(DocentNietGevondenException.class).isThrownBy(
                ()->service.opslag(-1, BigDecimal.TEN));
        verify(repository).findById(-1);
    }
}
