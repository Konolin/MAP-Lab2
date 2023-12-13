package map.project.musiclibrary.data.dto;

import lombok.Data;

@Data
public class PodcastDTO {
    private String name;
    private String lengthStr;
    private String topic;
    private String releaseDateStr;
    private String hostIdStr;
}
