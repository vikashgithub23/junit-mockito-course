package org.example.mockitoapp.services;

import org.example.mockitoapp.models.Examen;
import org.example.mockitoapp.repository.ExamenRepository;
import org.example.mockitoapp.repository.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {
    private ExamenRepository examenRepository;
    private PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository,
                             PreguntaRepository preguntaRepository) {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }


    @Override
    public Optional<Examen> findExamByName(String name) {
        return examenRepository.findAll()
                .stream()
                .filter(examen -> examen.getNombre().contains(name))
                .findFirst();
    }

    @Override
    public Examen findExamByNameWithQuestions(String name) {
        Optional<Examen> examenOptional = findExamByName(name);
        Examen examen = null;
        if(examenOptional.isPresent()){
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntaRepository.findQuestionsByExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen save(Examen examen) {
        if(!examen.getPreguntas().isEmpty()) {
            preguntaRepository.saveList(examen.getPreguntas());
        }
        return examenRepository.save(examen);
    }
}
