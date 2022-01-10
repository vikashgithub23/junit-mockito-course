package org.example.junitapp.models;

import org.example.junitapp.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Durante las pruebas se puede seguir lo siguiente:
 * 1. Definir la data
 * 2. Definir los mocks
 * 3. Invocar a la logica
 * 4. Verificar outputs
 * 5. Verificar mocks - double test
 *
 * Ciclo de vida
 * Before All (estático)
 * Before Each
 * Ejecucion del test
 * After Each
 * After All (estático)
 */
// Una instancia por clase, pero no es recomendable
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {
    Cuenta cuenta;

    /**
     * Esta asociado a la clase porque la instancia aún no se ha creado
     */
    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }

    /**
     * Se puede agregar TestInfo y TestReporter para obtener informacion del metodo
     * displayName y tags; y por medio de TestReporter agregar una entrada en el
     * log de JUnit (agrega mas datos a usar una impresión de consola ej. timestamp)
     */
    @BeforeEach
    void initMetodoTest() {
        this.cuenta = new Cuenta("Andrés", new BigDecimal("1000.12345"));
        System.out.println("Iniciando el método de prueba");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el método de prueba");
    }

    /**
     * Se puede usar @Disabled para saltarse la prueba
     * o el método estático fail() para hacer que falle
     *
     * si se usa RepeteadTest ya no se usa Test, esto puede usarse cuando existe aleatoriedad
     */
    //@Test
    @RepeatedTest(value = 3, name = "Repetición {currentRepetition} de {totalRepetitions}")
    @DisplayName("Probando nombre de la cuenta")
    void testNombreCuenta(RepetitionInfo repetitionInfo) {
        if(repetitionInfo.getCurrentRepetition() == 1) {
            System.out.println("Repetición 1");
        }
        String esperado = "Andrés";
        String real = cuenta.getPersona();
        // Usando lambda, Si la prueba falla los objetos (mensajes) no se crean
        assertNotNull(real, () -> "La cuenta no puede ser nula");
        assertEquals(esperado, real, () -> "El nombre de la cuenta no es el que se esperaba");
    }

    @Test
    @DisplayName("Probando salario")
    void testSaldoCuenta() {
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    @DisplayName("Probando la operación débito")
    void testDebito() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.1245", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Probando la operación crédito")
    void creadito() {
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.1245", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Probando excepción dinero insuficiente")
    void testDineroInsuficienteException() {
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(2000));
        });
        String expectedMessage = "Dinero insuficiente";
        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    @DisplayName("Probando la operación transferir")
    void testTransferir() {
        Cuenta cuenta1 = new Cuenta("John", new BigDecimal("2000.5"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("2000.5"));
        Banco banco = new Banco();
        banco.setNombre("Banco 1");
        banco.transferir(cuenta1, cuenta2, new BigDecimal(500));
        assertEquals("1500.5", cuenta1.getSaldo().toPlainString());
        assertEquals("2500.5", cuenta2.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Probando la relación entre banco y cuenta")
    void testRelacionBancoCuenta() {
        Cuenta cuenta1 = new Cuenta("John", new BigDecimal("2000.5"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("2000.5"));
        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);
        banco.setNombre("Banco del estado");
        banco.transferir(cuenta1, cuenta2, new BigDecimal(500));
        assertEquals("1500.5", cuenta1.getSaldo().toPlainString());
        assertEquals("2500.51", cuenta2.getSaldo().toPlainString());

        assertEquals(2, banco.getCuentas().size());
        assertEquals("Banco del estado.", cuenta1.getBanco().getNombre());

        assertEquals("Andres", banco.getCuentas().stream()
                .filter(cuenta -> cuenta.getPersona().equals("Andres"))
                .findFirst()
                .get()
                .getPersona());

        assertTrue(banco.getCuentas().stream()
                .filter(cuenta -> cuenta.getPersona().equals("Andres"))
                .findFirst()
                .isPresent());

        assertTrue(banco.getCuentas().stream()
                .anyMatch(cuenta -> cuenta.getPersona().equals("Andres")));
    }

    @Test
    @DisplayName("Probando la relación entre banco y cuenta usando AsesertAll")
    void testRelacionBancoCuentaAssertAll() {
        Cuenta cuenta1 = new Cuenta("John", new BigDecimal("2000.5"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("2000.5"));
        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);
        banco.setNombre("Banco del estado");
        banco.transferir(cuenta1, cuenta2, new BigDecimal(500));

        assertAll(
                () -> assertEquals("1500.5", cuenta1.getSaldo().toPlainString(),
                        () -> "El valor de la cuenta 1 no es el esperado"),
                () -> assertEquals("2500.51", cuenta2.getSaldo().toPlainString(),
                        () -> "El valor de la cuenta 2 no es el esperado"),
                () -> assertEquals(2, banco.getCuentas().size(),
                        () -> "El número de cuentas no es el esperado"),
                () -> assertEquals("Banco del estado.", cuenta1.getBanco().getNombre(),
                        () -> "El nombre del banco no es el esperado"),
                () -> assertEquals("Andres", banco.getCuentas().stream()
                        .filter(cuenta -> cuenta.getPersona().equals("Andres"))
                        .findFirst()
                        .get()
                        .getPersona(),
                        () -> "La persona no se encuentra en el banco "),
                () -> assertTrue(banco.getCuentas().stream()
                        .filter(cuenta -> cuenta.getPersona().equals("Andres"))
                        .findFirst()
                        .isPresent(),
                        () -> "La persona no se encuentra en el banco"),
                () -> assertTrue(banco.getCuentas().stream()
                        .anyMatch(cuenta -> cuenta.getPersona().equals("Andres")),
                        () -> "La persona no se encuentra en el banco")
        );
    }

    // Nested para agrupar tests
    // al ser yna clase anidada aplica para todos los métodos, puedo tener mas de un Tag, estan asociadas a la ejecucion no a la estructura
    @Tag("so")
    @Nested
    @DisplayName("Probando por sistema operativo")
    class SistemaOperativoTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testWindows(){

        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testMacAndLinux(){

        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows(){

        }
    }

    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_11)
        void testJre11(){

        }
    }

    class SystemPropertiesTest {
        @Test
        void imprimirProperties(){
            Properties properties = System.getProperties();
            properties.forEach((k, v) -> System.out.println(k + ": " + v));
        }

        /**
         * En matches se puede colocar una expresion regular
         */
        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "15.0.2")
        void testJavaVersion(){

        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void solo64(){

        }

        /**
         * -DENV=dev
         */
        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev(){

        }
    }

    class EnvironmnetVariablesTest {
        /**
         * Variables de ambiente del sistema operativo
         */
        @Test
        void printEnvironmentVariables(){
            Map<String, String> env = System.getenv();
            env.forEach((k, v) -> System.out.println(k + ": " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-8.0.282.8-hotspot.*")
        void testJavaHome() {

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "16")
        void testProcesadores(){

        }

        /**
         *
         */
        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv(){

        }
    }

    @Test
    @DisplayName("Probando salario")
    void testSaldoCuentaDev() {
        boolean isDev = "DEV".equals(System.getProperty("ENV"));
        assumeTrue(isDev); // si es false, se deshabilita
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    @DisplayName("Probando salario 2")
    void testSaldoCuentaDev2() {
        boolean isDev = "DEV".equals(System.getProperty("ENV"));
        assumingThat(isDev, () -> {
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        });
        // si se va a ejecutar
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    //@Test
    @ParameterizedTest(name = "Repetición {index} con valor {0}")
    // se pueden tener otros tipos de fuentes csv, metodo
    @ValueSource(strings = {"100", "200", "300", "500", "700", "1000", "1000.12345"})
    @DisplayName("Probando la operación débito ValueSource")
    void testDebitoParametrizedValueSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        //assertEquals(900, cuenta.getSaldo().intValue());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    //@Test
    @ParameterizedTest(name = "Repetición {index} con valor {0} y {1}")
    // se pueden tener otros tipos de fuentes csv, metodo
    @CsvSource({"900,100", "800,200", "1000,300", "600,500", "1100,700", "2000,1000", "900,1000.12345"})
    @DisplayName("Probando la operación débito ValueSource")
    void testDebitoParametrizedValueSource2(String saldo, String monto) {
        cuenta.setSaldo(new BigDecimal(saldo));
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        //assertEquals(900, cuenta.getSaldo().intValue());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Nested
    @Tag("timeout")
    class TimeoutTest {
        @Test
        @Timeout(5) // tiempo que espera para que se ejecute la prueba, si pasa ese tiempo falla
        void testTimeout() throws InterruptedException {
            TimeUnit.SECONDS.sleep(6);
        }

        @Test
        @Timeout(value = 500, unit = TimeUnit.MILLISECONDS) // tiempo que espera para que se ejecute la prueba, si pasa ese tiempo falla
        void testTimeoutTimeUnit() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(1000);
        }

        @Test
        void testTimeoutTimeUnitAssertion() throws InterruptedException {
            assertTimeout(Duration.ofSeconds(5), () -> {
                TimeUnit.MILLISECONDS.sleep(4600);
            });
            assertEquals("10", "10");
        }
    }

}