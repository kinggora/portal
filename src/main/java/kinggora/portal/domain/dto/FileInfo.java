package kinggora.portal.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FileInfo {

    private final boolean attached; // attach file 유무
    private final boolean imaged; // content image file 유무
    private final String thumbUrl; // thumbnail url
}
