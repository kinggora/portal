package kinggora.portal.domain.dto;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingCriteria {
    private int page = 1;   // 요청 페이지
    private int size = 10;  // 요청 페이지 당 데이터 수
}
