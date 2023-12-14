package map.project.musiclibrary.data.model.audios;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PodcastPlaybackSpeedDecorator extends Podcast {
    private final Podcast podcast;
    private final double playbackSpeed;

    public PodcastPlaybackSpeedDecorator(Podcast podcast, double playbackSpeed) {
        this.podcast = podcast;
        this.playbackSpeed = playbackSpeed;
    }

    @Override
    public String play() {
        return podcast.play() + "\nat " + playbackSpeed + "x speed";
    }
}
