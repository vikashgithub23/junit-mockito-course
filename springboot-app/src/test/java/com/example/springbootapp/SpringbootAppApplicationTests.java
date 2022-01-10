package com.example.springbootapp;

import com.example.springbootapp.exceptions.InsufficientAmountException;
import com.example.springbootapp.models.Banco;
import com.example.springbootapp.models.Cuenta;
import com.example.springbootapp.repositories.BancoRepository;
import com.example.springbootapp.repositories.CuentaRepository;
import com.example.springbootapp.services.CuentaService;
import com.example.springbootapp.services.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringbootAppApplicationTests {

	@MockBean // @Mock
	CuentaRepository cuentaRepository;

	@MockBean  // @Mock
	BancoRepository bancoRepository;

	@Autowired // @InjectMocks
	CuentaService cuentaService;

	@BeforeEach
	void setUp() {
//		this.cuentaRepository = mock(CuentaRepository.class);
//		this.bancoRepository = mock(BancoRepository.class);

//		this.cuentaService = new CuentaServiceImpl(cuentaRepository, bancoRepository);
	}

	@Test
	void contextLoads() {
		when(cuentaRepository.findById(1L)).thenReturn(Data.createCuenta01());
		when(cuentaRepository.findById(2L)).thenReturn(Data.createCuenta02());
		when(bancoRepository.findById(1L)).thenReturn(Data.createBanco01());

		BigDecimal initialAmout = cuentaService.getAmount(1L);
		BigDecimal saldoDestino = cuentaService.getAmount(2L);
		cuentaService.transferir(1L, 2L, new BigDecimal(100), 1L);

		assertEquals("120", initialAmout.toPlainString());
		assertEquals("140", saldoDestino.toPlainString());

		initialAmout = cuentaService.getAmount(1L);
		saldoDestino = cuentaService.getAmount(2L);

		assertEquals("20", initialAmout.toPlainString());
		assertEquals("240", saldoDestino.toPlainString());

		int total = cuentaService.getTotalTransactions(1L);
		assertEquals(1, total);

		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(3)).findById(2L);
		verify(cuentaRepository, times(2)).save(any(Cuenta.class));

		verify(bancoRepository, times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));

	}

	@Test
	void contextLoads2() {
		when(cuentaRepository.findById(1L)).thenReturn(Data.createCuenta01());
		when(cuentaRepository.findById(2L)).thenReturn(Data.createCuenta02());
		when(bancoRepository.findById(1L)).thenReturn(Data.createBanco01());

		BigDecimal initialAmout = cuentaService.getAmount(1L);
		BigDecimal saldoDestino = cuentaService.getAmount(2L);

		assertThrows(InsufficientAmountException.class, () -> {
			cuentaService.transferir(1L, 2L, new BigDecimal(1000), 1L);
		});

		assertEquals("120", initialAmout.toPlainString());
		assertEquals("140", saldoDestino.toPlainString());

		initialAmout = cuentaService.getAmount(1L);
		saldoDestino = cuentaService.getAmount(2L);

		assertEquals("120", initialAmout.toPlainString());
		assertEquals("140", saldoDestino.toPlainString());

		int total = cuentaService.getTotalTransactions(1L);
		assertEquals(0, total);

		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(2)).findById(2L);
		verify(cuentaRepository, never()).save(any(Cuenta.class));

		verify(bancoRepository, times(1)).findById(1L);
		verify(bancoRepository, never()).save(any(Banco.class));

	}

	// assertSame para validar referencias


	@Test
	void testFindAll() {
		List<Cuenta> data = Arrays.asList(Data.createCuenta01().orElseThrow(),
				Data.createCuenta02().orElseThrow());
		when(cuentaRepository.findAll()).thenReturn(data);

		List<Cuenta> cuentas = this.cuentaService.findAll();

		assertFalse(cuentas.isEmpty());
		assertEquals(2, cuentas.size());
		assertTrue(cuentas.contains(Data.createCuenta02().orElseThrow()));
		// contains compara usando el metodo equals, el cual fue sobreescrito
	}

	@Test
	void testFindById() {
		when(cuentaRepository.findById(1L)).thenReturn(Data.createCuenta01());

		Cuenta cuenta = this.cuentaService.findById(1L);

		assertNotNull(cuenta);
		assertEquals(1L, cuenta.getId());
		assertEquals("Andres", cuenta.getPerson());
		assertEquals(new BigDecimal(120), cuenta.getAmount());
	}

	@Test
	void testSave(){
		Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal(300));
		when(cuentaRepository.save(any(Cuenta.class))).then(invocation -> {
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});

		Cuenta cuenta = this.cuentaService.save(cuentaPepe);

		assertNotNull(cuenta);
		assertEquals(3L, cuenta.getId());
		assertEquals("Pepe", cuenta.getPerson());
		assertEquals(new BigDecimal(300), cuenta.getAmount());
	}
}
