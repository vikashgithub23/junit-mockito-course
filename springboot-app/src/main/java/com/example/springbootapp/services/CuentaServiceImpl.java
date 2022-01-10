package com.example.springbootapp.services;

import com.example.springbootapp.models.Banco;
import com.example.springbootapp.models.Cuenta;
import com.example.springbootapp.repositories.BancoRepository;
import com.example.springbootapp.repositories.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.lang.model.element.PackageElement;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService{
    private final CuentaRepository cuentaRepository;
    private final BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        return this.cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return this.cuentaRepository.findAll();
    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
        return this.cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalTransactions(Long bancoId) {
        Banco banco = this.bancoRepository.findById(bancoId).orElseThrow();
        return   banco.getTotalTransactions();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAmount(Long cuentaId) {
        Cuenta cuenta = this.cuentaRepository.findById(cuentaId).orElseThrow();
        return cuenta.getAmount();
    }

    @Override
    @Transactional
    public void transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal amount,
                           Long bancoId) {
        Cuenta cuentaOrigen = this.cuentaRepository.findById(cuentaOrigenId).orElseThrow();
        cuentaOrigen.debito(amount);
        cuentaRepository.save(cuentaOrigen);

        Cuenta cuentaDestino = this.cuentaRepository.findById(cuentaDestinoId).orElseThrow();
        cuentaDestino.credito(amount);
        cuentaRepository.save(cuentaDestino);

        Banco banco = this.bancoRepository.findById(bancoId).orElseThrow();
        int numerTransference = banco.getTotalTransactions();
        banco.setTotalTransactions(++numerTransference);
        bancoRepository.save(banco);
    }
}
