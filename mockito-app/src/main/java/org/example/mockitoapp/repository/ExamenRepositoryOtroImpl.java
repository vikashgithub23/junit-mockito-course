package org.example.mockitoapp.repository;

import org.example.mockitoapp.models.Examen;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ExamenRepositoryOtroImpl implements ExamenRepository {
    @Override
    public List<Examen> findAll() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Examen save(Examen examen) {
        return null;
    }
}
