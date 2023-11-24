package map.project.musiclibrary.data.model.strategies;

import map.project.musiclibrary.data.model.strategies.Playable;

public class PlayableWithoutAds implements Playable {
    @Override
    public String play(String name, String creatorUserName) {
        return "Playing \"" + name + "\" by " + creatorUserName;
    }
}
