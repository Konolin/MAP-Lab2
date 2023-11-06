package map.project.musiclibrary.data.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "LoginCredentials")
@Data
public class LoginCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToOne(mappedBy = "loginCredentials", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NormalUser user;
}