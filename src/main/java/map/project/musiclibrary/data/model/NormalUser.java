package map.project.musiclibrary.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Users")
@Data
public class NormalUser extends User {
    @Column(name = "isPremium")
    private boolean isPremium;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "credentials_id", referencedColumnName = "id")
    private LoginCredentials loginCredentials;

    @Override
    public String toString() {
        return "NormalUser(" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                ", isPremium=" + isPremium +
                // TODO - temporar
                ", email=" + loginCredentials.getEmail() +
                ", password=" + loginCredentials.getPassword() +
                ')';
    }
}
