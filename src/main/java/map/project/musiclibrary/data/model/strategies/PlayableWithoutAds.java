package map.project.musiclibrary.data.model.strategies;

public class PlayableWithoutAds implements Playable {
    @Override
    public String play(String name, String creatorUserName) {
        return "Playing \"" + name + "\" by " + creatorUserName;
    }
}
