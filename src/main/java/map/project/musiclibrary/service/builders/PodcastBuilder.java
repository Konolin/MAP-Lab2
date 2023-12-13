package map.project.musiclibrary.service.builders;

import map.project.musiclibrary.data.model.audios.Podcast;
import map.project.musiclibrary.data.model.users.HostUser;
import map.project.musiclibrary.service.HostUserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class PodcastBuilder {
    private String name;
    private int length;
    private String topic;
    private Date releaseDate;
    private Long hostId;

    public PodcastBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PodcastBuilder setLength(String lengthStr) {
        try {
            int tempLength = Integer.parseInt(lengthStr);
            if (tempLength > 0) {
                this.length = tempLength;
            } else {
                throw new IllegalArgumentException("Invalid value. Please provide a positive value.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer format. Please provide a valid number.");
        }
        return this;
    }

    public PodcastBuilder setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public PodcastBuilder setReleaseDate(String releaseDateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.releaseDate = dateFormat.parse(releaseDateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid birthdate format. Please use yyyy-MM-dd.");
        }
        return this;
    }

    public PodcastBuilder setHostId(String hostIdStr) {
        try {
            this.hostId = Long.parseLong(hostIdStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer format. Please provide a valid number.");
        }
        return this;
    }

    public Podcast build(HostUserService hostUserService) {
        Podcast podcast = new Podcast();
        podcast.setName(name);
        podcast.setLength(length);
        podcast.setTopic(topic);
        podcast.setReleaseDate(releaseDate);

        Optional<HostUser> hostUserOptional = hostUserService.findById(hostId);
        if (hostUserOptional.isPresent()) {
            podcast.setHost(hostUserOptional.get());
            hostUserOptional.get().addPodcast(podcast);
        } else {
            throw new IllegalArgumentException("Error: A host with that ID does not exist");
        }

        return podcast;
    }
}

