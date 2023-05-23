package kinggora.portal.repository;

import kinggora.portal.domain.FreePost;
import kinggora.portal.domain.QnaPost;
import kinggora.portal.domain.dto.PostDto;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QnaRepository {

    private final QnaMapper mapper;

    public int saveQuestion(QnaPost post) {
        mapper.saveQuestion(post);
        return post.getId();
    }

    /**
     * 답변 게시글 저장
     * @param post 게시글 정보
     * @return 게시글 id
     */
    public int saveAnswer(QnaPost post) {
        mapper.saveAnswer(post);
        return post.getId();
    }

    /**
     * 게시글 단건 조회
     * @param id 게시글 id
     * @return 게시글 정보
     */
    public Optional<QnaPost> findPostById(Integer id) {
        return mapper.findPostById(id);
    }

    public List<QnaPost> findPosts(SearchCriteria criteria, int startRow, int pageSize) {
        return mapper.findPosts(criteria, startRow, pageSize);
    }

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    public void hitUp(Integer id) {
        if(mapper.hitUp(id) != 1) {
            log.error("fail QnaRepository.hitUp");
        }
    }

    /**
     * 게시글 수정
     * @param post 수정할 데이터
     */
    public void updatePost(QnaPost post) {
        if(mapper.updatePost(post) != 1) {
            log.error("fail QnaRepository.updatePost");
        }
    }

    /**
     * 게시글 삭제
     * @param id 게시글 id
     */
    public void deletePost(Integer id) {
        if(mapper.deletePost(id) != 1) {
            log.error("fail QnaRepository.deletePost");
        }
    }

    /**
     * 답변 존재 여부
     * @param id 게시글 id
     * @return 해당 게시글을 parent 로 하는 게시글 존재 여부
     */
    public boolean answerExists(Integer id) {
        return mapper.answerExists(id);
    }

}
