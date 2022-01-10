package org.example.mockitoapp.repository;

import java.util.List;

public interface PreguntaRepository {
    List<String> findQuestionsByExamenId(Long id);
    void saveList(List<String> questions);
}
