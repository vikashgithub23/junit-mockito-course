package org.example.mockitoapp.repository;

import org.example.mockitoapp.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
    Examen save(Examen examen);
}
