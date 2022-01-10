package com.example.springbootapp.controllers;

import com.example.springbootapp.Data;
import com.example.springbootapp.models.Cuenta;
import com.example.springbootapp.models.TransactionDto;
import com.example.springbootapp.services.CuentaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CuentaService cuentaService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void getDetail() throws Exception {
        // Given
        when(cuentaService.findById(1L)).thenReturn(Data.createCuenta01().orElseThrow());

        // When
        this.mockMvc.perform(
                get("/api/cuentas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Andres"))
                .andExpect(jsonPath("$.amount").value("120"));

        verify(cuentaService).findById(anyLong());
    }

    @Test
    void testTransferir() throws Exception {
        // Given
        TransactionDto dto = new TransactionDto();
        dto.setCuentaFromId(1L);
        dto.setCuentaToId(2L);
        dto.setBankId(1L);
        dto.setAmount(new BigDecimal(20));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "ok");
        response.put("message", "Transferencia realizada con éxito");
        response.put("transaction", dto);

        // when
        this.mockMvc.perform(
                post("/api/cuentas/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("Transferencia realizada con éxito"))
                .andExpect(jsonPath("$.transaction.cuentaFromId").value(1L))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testFindAll() throws Exception {
        List<Cuenta> cuentas = Arrays.asList(
                Data.createCuenta01().orElseThrow(),
                Data.createCuenta02().orElseThrow()
        );
        // Given
        when(cuentaService.findAll()).thenReturn(cuentas);

        // when
        this.mockMvc.perform(
                get("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].person").value("Andres"))
                .andExpect(jsonPath("$[0].amount").value("120"))
                .andExpect(jsonPath("$[1].person").value("Andrea"))
                .andExpect(jsonPath("$[1].amount").value("140"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(this.objectMapper.writeValueAsString(cuentas)));
        verify(cuentaService).findAll();
    }

    @Test
    void testSave() throws Exception {
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal(200));
        when(cuentaService.save(any(Cuenta.class))).then(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        this.mockMvc.perform(
                post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(cuenta))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.person", is("Pepe")))
                .andExpect(jsonPath("$.amount", is(200)));
        verify(cuentaService).save(any(Cuenta.class));
    }
}