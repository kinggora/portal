package kinggora.portal.domain.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordDto {
    private String currentPassword;
    private String newPassword;

}
