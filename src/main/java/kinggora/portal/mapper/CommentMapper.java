package kinggora.portal.mapper;

import kinggora.portal.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMapper {

    /**
     * @param comment 댓글 정보
     * @return 댓글 id
     */
    int saveComment(Comment comment);

    /**
     * @param parent 부모 댓글 id
     * @return
     */
    int findRefOrderOfChild(Comment parent);

    /**
     * @return ref 컬럼 최댓값
     */
    int findMaxRef();

    /**
     * @param comment 댓글 정보
     * @return true: comment.depth 보다 작거나 같은 값 존재X, false: comment.depth 보다 작거나 같은 값 존재o (WHERE refOrder > comment.refOrder)
     */
    boolean isOnlyMinimumDepth(Comment comment);

    /**
     * @param ref 댓글 그룹
     * @return 그룹 내 refOrder 최댓값
     */
    int findMaxRefOrder(int ref);

    /**
     * @param ref      댓글 그룹
     * @param refOrder 댓글 순서
     * @return 수정된 row 수
     */
    int updateRefOrder(@Param("ref") Integer ref, @Param("refOrder") Integer refOrder);

    /**
     * 댓글 단건 조회
     *
     * @param id 댓글 id
     * @return 댓글 정보
     */
    Optional<Comment> findCommentById(Integer id);

    /**
     * @param comment 수정할 데이터
     * @return 수정된 row 수
     */
    int updateComment(Comment comment);

    /**
     * @param id 댓글 id
     * @return 변경된 row 개수
     */
    int hideComment(Integer id);

    /**
     * @param postId 게시글 id
     * @return 댓글 리스트
     */
    List<Comment> findComments(Integer postId);

    /**
     * @param id 댓글 id
     * @return 변경된 row 개수
     */
    int deleteComment(Integer id);

    /**
     * @param id 댓글 id
     * @return true: 대댓글 존재o, false: 대댓글 존재x
     */
    boolean childExists(Integer id);
}

