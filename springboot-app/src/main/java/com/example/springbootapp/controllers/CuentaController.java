package com.example.springbootapp.controllers;

import com.example.springbootapp.models.Cuenta;
import com.example.springbootapp.models.TransactionDto;
import com.example.springbootapp.services.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService){
        this.cuentaService = cuentaService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cuenta> findAll(){
        return this.cuentaService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cuenta add(@RequestBody Cuenta cuenta){
        return this.cuentaService.save(cuenta);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cuenta getDetail(@PathVariable Long id){
        return this.cuentaService.findById(id);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransactionDto transactionDto) {
        this.cuentaService.transferir(transactionDto.getCuentaFromId(), transactionDto.getCuentaToId(),
                transactionDto.getAmount(), transactionDto.getBankId());
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "ok");
        response.put("message", "Transferencia realizada con éxito");
        response.put("transaction", transactionDto);
        return ResponseEntity.ok(response);
    }

}
