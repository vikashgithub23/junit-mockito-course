package org.example.mockitoapp.services;

import org.example.mockitoapp.models.Examen;

import java.util.Optional;

public interface ExamenService {
    Optional<Examen> findExamByName(String name);
    Examen findExamByNameWithQuestions(String name);
    Examen save(Examen examen);
}
