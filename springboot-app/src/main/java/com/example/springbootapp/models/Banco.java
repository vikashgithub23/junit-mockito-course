package com.example.springbootapp.models;

import javax.persistence.*;

@Entity
@Table(name = "bancos")
public class Banco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Banco() {
    }

    @Column(name = "total_transactions")
    private int totalTransactions;

    public Banco(Long id, String name, int totalTransactions) {
        this.id = id;
        this.name = name;
        this.totalTransactions = totalTransactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
}
