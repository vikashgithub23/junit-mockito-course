package com.example.springbootapp;

import com.example.springbootapp.models.Banco;
import com.example.springbootapp.models.Cuenta;

import java.math.BigDecimal;
import java.util.Optional;

public class Data {

    public static Optional<Cuenta> createCuenta01() {
        return Optional.of(new Cuenta(1L, "Andres", new BigDecimal(120)));
    }

    public static Optional<Cuenta> createCuenta02() {

        return Optional.of(new Cuenta(1L, "Andrea", new BigDecimal(140)));
    }

    public static Optional<Banco> createBanco01() {
        return Optional.of(new Banco(2L, "ABC",0));
    }
}
