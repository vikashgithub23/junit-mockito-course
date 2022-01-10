package com.example.springbootapp.services;

import com.example.springbootapp.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    Cuenta findById(Long id);
    List<Cuenta> findAll();
    Cuenta save(Cuenta cuenta);
    int getTotalTransactions(Long bancoId);
    BigDecimal getAmount(Long cuentaId);
    void transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal amount,
                    Long bancoId);
}
