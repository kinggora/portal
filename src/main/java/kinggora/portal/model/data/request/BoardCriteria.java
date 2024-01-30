package kinggora.portal.model.data.request;

import lombok.Builder;
import lombok.Getter;

/**
 * 게시글 필터링 조건 정의
 */
@Getter
public class BoardCriteria {

    private Integer boardId;
    private Integer memberId;
    private Integer categoryId;
    private String searchWord;
    private String startDate;
    private String endDate;

    @Builder
    public BoardCriteria(BoardSearchParam boardSearchParam, Integer boardId, Integer memberId) {
        if (boardSearchParam != null) {
            this.categoryId = boardSearchParam.getCategoryId();
            this.searchWord = boardSearchParam.getSearchWord();
            this.startDate = boardSearchParam.getStartDate();
            this.endDate = boardSearchParam.getEndDate();
        }
        this.boardId = boardId;
        this.memberId = memberId;
    }

}
