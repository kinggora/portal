package kinggora.portal.repository;

import kinggora.portal.domain.Comment;
import kinggora.portal.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final CommentMapper mapper;

    /**
     * 댓글 저장
     * @param comment 댓글 정보
     * @return 댓글 id
     */
    public int saveComment(Comment comment) {
        if(mapper.saveComment(comment) != 1){
            log.error("fail CommentRepository.saveComment");
        }
        return comment.getId();
    }

    /**
     * 댓글 단건 조회
     * @param id 댓글 id
     * @return 댓글 정보
     */
    public Optional<Comment> findCommentById(Integer id) {
        return mapper.findCommentById(id);
    }

    /**
     * 게시글에 대한 댓글 조회
     * @param boardId 게시판 id
     * @param postId 게시글 id
     * @return 댓글 리스트
     */
    public List<Comment> findComments(int boardId, int postId) {
        return mapper.findComments(boardId, postId);
    }

    /**
     * 댓글 수정
     * @param comment 수정할 데이터
     */
    public void updateComment(Comment comment) {
        if(mapper.updateComment(comment) != 1) {
            log.error("fail CommentRepository.updateComment");
        }
    }

    /**
     * 댓글 숨기기 (hide=true)
     * @param id 댓글 id
     * @return 수정된 row 개수
     */
    public void hideComment(Integer id) {
        if(mapper.hideComment(id) != 1) {
            log.error("fail CommentRepository.updateComment");
        }
    }

    /**
     * 댓글 삭제
     * @param id 댓글 id
     */
    public void deleteComment(Integer id) {
        if(mapper.deleteComment(id) != 1) {
            log.error("fail CommentRepository.deletePost");
        }
    }

    /**
     * 댓글에 대한 자식(대댓글) 존재 여부
     * @param id 댓글 id
     * @return true: 대댓글 존재o, false: 대댓글 존재x
     */
    public boolean childExists(Integer id) {
        return mapper.childExists(id);
    }
}
