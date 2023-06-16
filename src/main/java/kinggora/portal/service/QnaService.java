package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Category;
import kinggora.portal.domain.QnaPost;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.CategoryRepository;
import kinggora.portal.repository.QnaRepository;
import kinggora.portal.util.PageManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 질문 게시글 저장
     * @param post 게시글 정보 (parent == null)
     * @return 게시글 id
     */
    public int saveQuestion(QnaPost post) {
        return qnaRepository.saveQuestion(post);
    }

    /**
     * 답변 게시글 저장
     * @param post 게시글 정보 (parent != null)
     * @return 게시글 id
     */
    public int saveAnswer(QnaPost post) {
        return qnaRepository.saveAnswer(post);
    }

    /**
     * 게시글 단건 조회
     * @param id 게시글 id
     * @return 게시글 정보
     */
    public QnaPost findPostById(Integer id) {
        return qnaRepository.findPostById(id)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
    }

    /**
     * 검색 조건에 해당하는 게시글 조회 + 페이징 처리
     * @param criteria 검색 조건
     * @return 게시글 리스트
     */
    public List<QnaPost> findPosts(SearchCriteria criteria) {
        int startRow = (criteria.getPage() - 1) * PageManager.PAGE_SIZE;
        return qnaRepository.findPosts(criteria, startRow, PageManager.PAGE_SIZE);
    }

    /**
     * 질문 게시글 수정
     * 답변이 존재하면 수정 불가
     * @param post 수정할 데이터
     */
    public void updateQuestion(QnaPost post) {
        if(qnaRepository.answerExists(post.getId())) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE, "답변이 존재하는 질문은 수정할 수 없습니다.");
        } else {
            qnaRepository.updatePost(post);
        }
    }

    /**
     * 질문 게시글 수정
     * 답변이 존재하면 삭제 불가
     * @param id 게시글 id
     */
    public void deleteQuestion(Integer id) {
        if(qnaRepository.answerExists(id)) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE, "답변이 존재하는 질문은 삭제할 수 없습니다.");
        } else {
            qnaRepository.deletePost(id);
        }
    }

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    public void hitUp(Integer id) {
        qnaRepository.hitUp(id);
    }

    /**
     * QnA 게시판 카테고리 조회
     * @return 카테고리 정보
     */
//    public List<Category> findCategories() {
//        return categoryRepository.findCategories(BoardInfo.QNA);
//    }

}
