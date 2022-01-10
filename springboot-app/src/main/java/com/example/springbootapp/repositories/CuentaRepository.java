package com.example.springbootapp.repositories;

import com.example.springbootapp.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long > {

    @Query("select c from Cuenta c where c.person = ?1")
    Optional<Cuenta> findByPerson(String person);
}
