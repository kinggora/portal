package kinggora.portal.domain.dto.request;

import lombok.*;

import javax.validation.constraints.Positive;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingCriteria {

    @Positive
    private int page = 1;   // 요청 페이지
    @Positive
    private int size = 10;  // 요청 페이지 당 데이터 수
}
