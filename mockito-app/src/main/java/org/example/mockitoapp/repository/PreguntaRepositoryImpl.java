package org.example.mockitoapp.repository;

import java.util.Collections;
import java.util.List;

public class PreguntaRepositoryImpl implements PreguntaRepository {

    @Override
    public List<String> findQuestionsByExamenId(Long id) {
        System.out.println("PreguntaRepositoryImpl.findQuestionsByExamenId");
        return Collections.emptyList();
    }

    @Override
    public void saveList(List<String> questions) {

    }
}
