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
     * @return
     */
    int findMaxRef();

    boolean isOnlyMinimumDepth(Comment comment);

    int findMaxRefOrder(int ref);

    /**
     * @param ref
     * @param refOrder
     * @return
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
    int hideCommentById(Integer id);

    /**
     * @param postId 댓글 id
     * @return 변경된 row 개수
     */
    int hideComments(Integer postId);

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

