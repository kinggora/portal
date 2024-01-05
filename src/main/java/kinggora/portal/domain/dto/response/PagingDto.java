package kinggora.portal.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PagingDto<T> {
    private final List<T> data;
    private final PageInfo pageInfo;

}
