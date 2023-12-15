package map.project.musiclibrary.data.dto;

import lombok.Data;

@Data
public class SongDTO {
    private String name;
    private String genre;
    private String lengthStr;
    private String releaseDateStr;
    private String artistIdStr;
}
