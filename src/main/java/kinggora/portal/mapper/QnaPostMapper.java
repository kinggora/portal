package kinggora.portal.mapper;

import kinggora.portal.domain.QnaPost;
import kinggora.portal.domain.dto.SearchCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface QnaPostMapper {

    /**
     * 질문 게시글 저장
     *
     * @param post 게시글 정보
     * @return 게시글 id
     */
    int saveQuestion(QnaPost post);

    /**
     * 답변 게시글 저장
     *
     * @param post 게시글 정보
     * @return 게시글 id
     */
    int saveAnswer(QnaPost post);

    /**
     * 게시글 단건 조회
     *
     * @param id 게시글 id
     * @return 게시글 정보
     */
    Optional<QnaPost> findPostById(Integer id);

    /**
     * 검색 조건에 해당하는 게시글 조회 + 페이징 처리
     *
     * @param criteria 검색 조건
     * @param limit    페이징
     * @param offset   페이징
     * @return 게시글 리스트
     */
    List<QnaPost> findPosts(@Param("criteria") SearchCriteria criteria, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 게시글 조회수 1 증가
     *
     * @param id 게시글 id
     */
    int hitUp(Integer id);

    /**
     * 게시글 수정
     *
     * @param post 수정할 데이터
     * @return 수정된 게시글 개수
     */
    int updatePost(QnaPost post);

    /**
     * 게시글 삭제
     *
     * @param id 게시글 id
     * @return 삭제된 게시글 개수
     */
    int deletePost(Integer id);

    /**
     * 답변 존재 여부
     *
     * @param id 게시글 id
     * @return 해당 게시글을 parent 로 하는 게시글 존재 여부
     */
    boolean answerExists(Integer id);

}
