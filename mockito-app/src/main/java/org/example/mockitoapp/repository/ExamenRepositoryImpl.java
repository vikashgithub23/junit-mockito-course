package org.example.mockitoapp.repository;

import org.example.mockitoapp.models.Examen;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository {

    @Override
    public List<Examen> findAll() {
        ///return Arrays.asList(new Examen(6L, "Lima"));
        return Collections.emptyList();
    }

    @Override
    public Examen save(Examen examen) {
        return null;
    }
}
