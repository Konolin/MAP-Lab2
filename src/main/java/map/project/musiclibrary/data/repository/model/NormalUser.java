package map.project.musiclibrary.data.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Users")
@Data
public class NormalUser extends User {
    @Column(name = "isPremium")
    private boolean isPremium;
}
