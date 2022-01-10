package com.example.springbootapp.repositories;

import com.example.springbootapp.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco, Long> {
}
