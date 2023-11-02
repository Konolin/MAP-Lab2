package map.project.musiclibrary.service;

import map.project.musiclibrary.data.repository.LabelRepository;
import map.project.musiclibrary.data.model.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    private final LabelRepository labelRepository;

    @Autowired
    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public Label save(Label label) {
        return labelRepository.save(label);
    }

    public Label findByName(String name) {
        return labelRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<Label> findAll() {
        return labelRepository.findAll();
    }

}
