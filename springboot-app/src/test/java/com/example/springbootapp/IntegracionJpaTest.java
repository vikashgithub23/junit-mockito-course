package com.example.springbootapp;

import com.example.springbootapp.models.Cuenta;
import com.example.springbootapp.repositories.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegracionJpaTest {

    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuentaOptional = this.cuentaRepository.findById(1L);
        assertTrue(cuentaOptional.isPresent());
        assertEquals("Andres", cuentaOptional.orElseThrow().getPerson());
    }

    @Test
    void testFindByPerson() {
        Optional<Cuenta> cuentaOptional = this.cuentaRepository.findByPerson("Andres");
        assertTrue(cuentaOptional.isPresent());
        assertEquals("Andres", cuentaOptional.orElseThrow().getPerson());
    }

    @Test
    void testFindByPersonThrowException() {
        Optional<Cuenta> cuentaOptional = this.cuentaRepository.findByPerson("Pedro");
        assertThrows(NoSuchElementException.class, cuentaOptional::orElseThrow);
        assertFalse(cuentaOptional.isPresent());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void testSave() {
        // Given
        Cuenta cuenta = new Cuenta(null, "Carla", new BigDecimal("2000"));
        Cuenta newCuenta = cuentaRepository.save(cuenta);

        // When
//        Cuenta cuentaCarla = cuentaRepository.findById(newCuenta.getId()).orElseThrow();

        // Then
        assertEquals("Carla", newCuenta.getPerson());
        assertEquals("2000", newCuenta.getAmount().toPlainString());

    }

    @Test
    void testUpdate() {
        // Given
        Cuenta cuenta = new Cuenta(null, "Carla", new BigDecimal("2000"));
        Cuenta newCuenta = cuentaRepository.save(cuenta);

        // When
//        Cuenta cuentaCarla = cuentaRepository.findById(newCuenta.getId()).orElseThrow();

        // Then
        assertEquals("Carla", newCuenta.getPerson());
        assertEquals("2000", newCuenta.getAmount().toPlainString());

        // When
        newCuenta.setAmount(new BigDecimal("500"));
        Cuenta updatedCuenta = cuentaRepository.save(newCuenta);

        // Then
        assertEquals("Carla", newCuenta.getPerson());
        assertEquals("500", updatedCuenta.getAmount().toPlainString());
    }

    @Test
    void testDelete() {
        Cuenta cuenta = cuentaRepository.findById(1L).orElseThrow();
        assertEquals("Andres", cuenta.getPerson());

        cuentaRepository.delete(cuenta);

        assertThrows(NoSuchElementException.class, () -> {
            cuentaRepository.findById(1L).orElseThrow();
        });
    }
}
