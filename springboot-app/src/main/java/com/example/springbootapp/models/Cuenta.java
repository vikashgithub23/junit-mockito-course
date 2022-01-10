package com.example.springbootapp.models;

import com.example.springbootapp.exceptions.InsufficientAmountException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "cuentas")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String person;
    private BigDecimal amount;

    public Cuenta() {
    }

    public Cuenta(Long id, String person, BigDecimal amount) {
        this.id = id;
        this.person = person;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuenta cuenta = (Cuenta) o;
        return Objects.equals(id, cuenta.id) && Objects.equals(person, cuenta.person) && Objects.equals(amount, cuenta.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, amount);
    }

    public void debito(BigDecimal amount) {
        BigDecimal newAmount = this.amount.subtract(amount);
        if(newAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new InsufficientAmountException("Dinero insuficiente");
        }
        this.amount = newAmount;
    }

    public void credito(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }
}
