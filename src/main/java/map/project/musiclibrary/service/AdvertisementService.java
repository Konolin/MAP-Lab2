package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.audios.Advertisement;
import map.project.musiclibrary.data.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    public Advertisement addAdvertisement(String name, String lengthStr, String type, String releaseDateStr) throws ParseException {
        Advertisement advertisement = new Advertisement();

        advertisement.setName(name);
        advertisement.setLength(Integer.parseInt(lengthStr));
        advertisement.setAdvertisementType(type);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = dateFormat.parse(releaseDateStr);
        advertisement.setReleaseDate(releaseDate);

        return advertisementRepository.save(advertisement);
    }

    public Advertisement findByName(String name) {
        return advertisementRepository.findByName(name).stream().findFirst().orElse(null);
    }

    public List<Advertisement> findAll() {
        return advertisementRepository.findAll();
    }
}
