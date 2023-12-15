package map.project.musiclibrary.data.dto;

import lombok.Data;

@Data
public class NormalUserDTO {
    private String name;
    private String email;
    private String password;
    private String isPremiumStr;
    private String birthdateStr;
}
