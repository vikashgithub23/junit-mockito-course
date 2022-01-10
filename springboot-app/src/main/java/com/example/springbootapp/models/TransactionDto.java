package com.example.springbootapp.models;

import java.math.BigDecimal;

public class TransactionDto {
    private Long cuentaFromId;
    private Long cuentaToId;
    private BigDecimal amount;
    private Long bankId;

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getCuentaFromId() {
        return cuentaFromId;
    }

    public void setCuentaFromId(Long cuentaFromId) {
        this.cuentaFromId = cuentaFromId;
    }

    public Long getCuentaToId() {
        return cuentaToId;
    }

    public void setCuentaToId(Long cuentaToId) {
        this.cuentaToId = cuentaToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
