package org.example.mockitoapp.services;

import org.example.mockitoapp.models.Examen;
import org.example.mockitoapp.repository.ExamenRepository;
import org.example.mockitoapp.repository.ExamenRepositoryImpl;
import org.example.mockitoapp.repository.PreguntaRepository;
import org.example.mockitoapp.repository.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
    @Mock
    private ExamenRepository examenRepository;

    @Mock
    private PreguntaRepositoryImpl preguntaRepository;

    // Crear instancia del service y que inyecte los objetos
    @InjectMocks
    private ExamenServiceImpl examenService;

    @Captor
    private ArgumentCaptor<Long> captorLong;

    @BeforeEach
    void setUp() {
//        this.examenRepository = mock(ExamenRepository.class);
//        this.preguntaRepository = mock(PreguntaRepository.class);
//        this.examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);
        // habilitar anotaciones
//        MockitoAnnotations.openMocks(this);
    }

    /**
     * Se puede hacer mock solo de metodos publicos o default
     */
    @Test
    void findExamByName() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        Optional<Examen> examen = examenService.findExamByName("Matemáticas");
        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matemáticas", examen.get().getNombre());
    }

    @Test
    void findExamByNameEmptyList() {
        // Incializar datos
        List<Examen> examenes = Collections.emptyList();
        // Mockear
        when(examenRepository.findAll()).thenReturn(examenes);
        // Llamar metodo
        Optional<Examen> examen = examenService.findExamByName("Matemáticas");
        // Ejecutar assert
        assertFalse(examen.isPresent());
    }

    @Test
    void testQuestions() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findQuestionsByExamenId(5L)).thenReturn(Datos.PREGUNTAS);
        Examen examen = examenService.findExamByNameWithQuestions("Matemáticas");
        assertEquals(4, examen.getPreguntas().size());
    }

    @Test
    void testQuestionsVerify() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findQuestionsByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = examenService.findExamByNameWithQuestions("Matemáticas");
        assertEquals(4, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("geometria"));
        verify(examenRepository).findAll();
        verify(preguntaRepository).findQuestionsByExamenId(anyLong());
    }

    @Test
    void testNoExistsQuestionsVerify() {
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findQuestionsByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = examenService.findExamByNameWithQuestions("Matemáticas");
        assertNull(examen);
        verify(examenRepository).findAll();
        verify(preguntaRepository).findQuestionsByExamenId(anyLong());
    }

    @Test
    void testSaveExam() {
        // Given
        Examen newExamen = Datos.EXAM;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        when(examenRepository.save(any(Examen.class))).then(new Answer<Examen>(){
            Long sequence = 8L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }
        });

        // When
        Examen examen = examenService.save(newExamen);

        // Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());
        verify(examenRepository).save(any(Examen.class));
        verify(preguntaRepository).saveList(anyList());
    }

    @Test
    void nameHandleException() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        when(preguntaRepository.findQuestionsByExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            examenService.findExamByNameWithQuestions("Matemáticas");
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());
        verify(examenRepository).findAll();
        verify(preguntaRepository).findQuestionsByExamenId(isNull());
    }

    @Test
    void testArgumentMatchers() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findQuestionsByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamByNameWithQuestions("Matemáticas");
        verify(examenRepository).findAll();
//        verify(preguntaRepository).findQuestionsByExamenId(argThat(arg -> arg != null && arg.equals(5L)));
        verify(preguntaRepository).findQuestionsByExamenId(argThat(arg -> arg != null && arg >= 5L));
        verify(preguntaRepository.findQuestionsByExamenId(eq(5L)));
    }

    @Test
    void testArgumentMatchers2() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVO);
        when(preguntaRepository.findQuestionsByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamByNameWithQuestions("Matemáticas");
        verify(examenRepository).findAll();
//        verify(preguntaRepository).findQuestionsByExamenId(argThat(arg -> arg != null && arg.equals(5L)));
        verify(preguntaRepository).findQuestionsByExamenId(argThat(new MyArgsMatchers()));
    }

    @Test
    void testArgumentMatchers3() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVO);
        when(preguntaRepository.findQuestionsByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamByNameWithQuestions("Matemáticas");
        verify(examenRepository).findAll();
//        verify(preguntaRepository).findQuestionsByExamenId(argThat(arg -> arg != null && arg.equals(5L)));
        verify(preguntaRepository).findQuestionsByExamenId(argThat(aLong -> aLong != null && aLong > 0));
    }

    public static class MyArgsMatchers implements ArgumentMatcher<Long> {
        private Long argument;

        @Override
        public boolean matches(Long aLong) {
            this.argument = aLong;
            return aLong != null && aLong > 0;
        }

        @Override
        public String toString() {
            return "Mensaje personalizado de error, cuando falla el test " +
                    + this.argument + " debe ser un entero positivo";
        }
    }

    @Test
    void testArgumentCaptor() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findQuestionsByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamByNameWithQuestions("Matemáticas");

//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntaRepository).findQuestionsByExamenId(captorLong.capture());

        assertEquals(5L, captorLong.getValue());
    }

    @Test
    void testDoThrow() {
        Examen examen = Datos.EXAM;
        examen.setPreguntas(Datos.PREGUNTAS);

        doThrow(IllegalArgumentException.class).when(preguntaRepository).saveList(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
           examenService.save(examen);
        });
    }

    @Test
    void testDoAnswer(){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findQuestionsByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        doAnswer(invocation -> {
           Long id = invocation.getArgument(0);
           return id == 5L ? Datos.PREGUNTAS : Collections.emptyList();
        }).when(preguntaRepository).findQuestionsByExamenId(anyLong());

        Examen exam = examenService.findExamByNameWithQuestions("Matemáticas");

        assertEquals(5, exam.getPreguntas().size());
        assertTrue(exam.getPreguntas().contains("geometria"));
        assertEquals(5L, exam.getId());
        assertEquals("Matemáticas", exam.getNombre());
    }

    @Test
    void testSaveExamDoAnswer() {
        // Given
        Examen newExamen = Datos.EXAM;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        /*when(examenRepository.save(any(Examen.class))).then(new Answer<Examen>(){
            Long sequence = 8L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }
        });*/
        doAnswer(new Answer<Examen>(){
            Long sequence = 8L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }
        }).when(examenRepository).save(any(Examen.class));

        // When
        Examen examen = examenService.save(newExamen);

        // Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());
        verify(examenRepository).save(any(Examen.class));
        verify(preguntaRepository).saveList(anyList());
    }

    @Test
    void testDoCallRealMethod() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findQuestionsByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        // se debe definir el mock usando la clase concreta y no la interface
        doCallRealMethod().when(preguntaRepository).findQuestionsByExamenId(anyLong());

        Examen examen = examenService.findExamByNameWithQuestions("Matemáticas");

        assertEquals(5L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
    }

    @Test
    void testInvocationsOrder() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        examenService.findExamByNameWithQuestions("Matemáticas");
        examenService.findExamByNameWithQuestions("Lenguaje");

        // Verificar orden de invocacion
        InOrder inOrder = inOrder(examenRepository, preguntaRepository);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntaRepository).findQuestionsByExamenId(5L);
        
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntaRepository).findQuestionsByExamenId(6L);

    }

    @Test
    void testVerifyNumberInvocations() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        examenService.findExamByNameWithQuestions("Matemáticas");

        // por defecto es 1, si no se coloca es 1
        // atLeast(1), atleastOnce(), atMost(1), atMostOnce()
        verify(preguntaRepository, times(1)).findQuestionsByExamenId(anyLong());

    }

    @Test
    void testVerifyNumberInvocations2() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        examenService.findExamByNameWithQuestions("Matemáticas");

        // por defecto es 1, si no se coloca es 1
        // never() - no hubo interaccion con ese metodo
        verify(preguntaRepository, never()).findQuestionsByExamenId(anyLong());

        // no hay una interaccion
        verifyNoInteractions(preguntaRepository);

        verify(examenRepository).findAll();
        verify(examenRepository, times(1)).findAll();
    }
}