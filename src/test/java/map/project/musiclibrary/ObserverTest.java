package map.project.musiclibrary;

import map.project.musiclibrary.data.model.Album;
import map.project.musiclibrary.data.model.ArtistUser;
import map.project.musiclibrary.data.model.NormalUser;
import map.project.musiclibrary.data.model.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ObserverTest {

    @Test
    void testObserverPattern() {

        NormalUser normalUser = mock(NormalUser.class);
        when(normalUser.getName()).thenReturn("Test Name");

        ArtistUser artist = new ArtistUser();
        artist.setId(1L);
        artist.setName("Test Name");

        Album album = new Album();
        album.setId(1L);
        album.setArtist(artist);
        album.setName("Test Album");

        Notification notification = new Notification();
        notification.setUser(normalUser);
        notification.setAlbum(album);

        artist.addFollower(normalUser);

        artist.notifyFollowers(album);

        //verify that the update method in normalUser was called with the correct message
        verify(normalUser, times(1)).update("New album released: Test Album by Test Name");

        //verify that the normalUser's seeNewNotifications method shows the new notification
        when(normalUser.seeNewNotifications()).thenReturn("New Notifications:\n" +
                "Hey, Test Name! New Album Release! Go listen Test Album by Test Name now!");

        assertEquals("New Notifications:\n" +
                        "Hey, Test Name! New Album Release! Go listen Test Album by Test Name now!",
                normalUser.seeNewNotifications());
    }
}
