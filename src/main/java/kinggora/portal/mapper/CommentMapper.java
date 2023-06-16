package kinggora.portal.mapper;

import kinggora.portal.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMapper {

    /**
     * 댓글 저장
     * @param comment 댓글 정보
     * @return 댓글 id
     */
    int saveComment(Comment comment);

    /**
     * 댓글 단건 조회
     * @param id 댓글 id
     * @return 댓글 정보
     */
    Optional<Comment> findCommentById(Integer id);

    /**
     * 댓글 수정
     * @param comment 수정할 데이터
     * @return 수정된 row 개수
     */
    int updateComment(Comment comment);

    /**
     * 댓글 숨기기 (hide=true)
     * @param id 댓글 id
     * @return 수정된 row 개수
     */
    int hideComment(Integer id);

    /**
     * 댓글 삭제
     * @param id 댓글 id
     * @return 삭제된 row 개수
     */
    int deleteComment(Integer id);

    /**
     * 게시글에 대한 댓글 조회
     * @param boardId 게시판 id
     * @param postId 게시글 id
     * @return 댓글 리스트
     */
    List<Comment> findComments(@Param("boardId") int boardId, @Param("postId") int postId);

    /**
     * 댓글에 대한 자식(대댓글) 존재 여부
     * @param id 댓글 id
     * @return true: 대댓글 존재o, false: 대댓글 존재x
     */
    boolean childExists(Integer id);
}
