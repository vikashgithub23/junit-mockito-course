package org.example.junitapp.models;

import org.example.junitapp.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.util.Objects;

public class Cuenta {
    private String persona;
    private BigDecimal saldo;
    private Banco banco;

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public void debito(BigDecimal monto) {
        BigDecimal newAmount = this.saldo.subtract(monto);
        if(newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DineroInsuficienteException("Dinero insuficiente");
        }
        this.saldo = newAmount;
    }

    public void credito(BigDecimal monto) {
        this.saldo = this.saldo.add(monto);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Cuenta)){
            return false;
        }
        Cuenta cuenta = (Cuenta) o;
        if (this.persona == null || this.saldo == null) {
            return false;
        }
        return this.persona.equals(cuenta.getPersona()) && this.saldo.equals(cuenta.getSaldo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(persona, saldo);
    }
}
