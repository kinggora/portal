package kinggora.portal.domain;

import kinggora.portal.domain.dto.FileInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonPost extends Post {
    private FileInfo fileInfo;
}
