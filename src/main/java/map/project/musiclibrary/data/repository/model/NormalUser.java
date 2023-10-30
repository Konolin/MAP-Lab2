package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class NormalUser extends User {
    @Column(name = "isPremium")
    private boolean isPremium;
}
