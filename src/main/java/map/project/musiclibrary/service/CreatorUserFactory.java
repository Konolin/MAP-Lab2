package map.project.musiclibrary.service;

import map.project.musiclibrary.data.model.users.ArtistUser;
import map.project.musiclibrary.data.model.users.HostUser;
import map.project.musiclibrary.data.model.users.User;

import java.util.ArrayList;
import java.util.Date;

public class CreatorUserFactory {

    public static User createCreatorUser(String userType, String name, Date birthDate) {
        if ("artist".equalsIgnoreCase(userType)) {
            ArtistUser artist = new ArtistUser();
            artist.setName(name);
            artist.setBirthdate(birthDate);
            artist.setSongs(new ArrayList<>());
            return artist;
        } else if ("host".equalsIgnoreCase(userType)) {
            HostUser host = new HostUser();
            host.setName(name);
            host.setBirthdate(birthDate);
            host.setPodcasts(new ArrayList<>());
            return host;
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
}
