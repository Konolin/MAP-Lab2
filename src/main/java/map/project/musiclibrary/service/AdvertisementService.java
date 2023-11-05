package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.Advertisement;
import map.project.musiclibrary.data.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    public Advertisement save(Advertisement advertisement) {
        return advertisementRepository.save(advertisement);
    }

    public Advertisement findByName(String name) {
        return advertisementRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<Advertisement> findAll() {
        return advertisementRepository.findAll();
    }
}
