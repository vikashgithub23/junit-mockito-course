package org.example.mockitoapp.services;

import org.example.mockitoapp.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public static final List<Examen> EXAMENES = Arrays.asList(new Examen(5L, "Matemáticas"),
                new Examen(6L, "Lenguaje"),
                new Examen(7L, "Historia"));

    public static final List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null, "Matemáticas"),
            new Examen(null, "Lenguaje"),
            new Examen(null, "Historia"));

    public static final List<Examen> EXAMENES_ID_NEGATIVO = Arrays.asList(new Examen(-5L, "Matemáticas"),
            new Examen(-6L, "Lenguaje"),
            new Examen(-7L, "Historia"));

    public static final List<String> PREGUNTAS = Arrays.asList(
            "artimetica", "integrales", "trigonometrica", "geometria"
    );

    public static final Examen EXAM = new Examen(null, "Fisica");
}
