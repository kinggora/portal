package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
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

    private static final int BOARD_ID = 6;
    private final QnaRepository qnaRepository;
    private final CategoryRepository categoryRepository;

    public int saveQuestion(QnaPost post) {
        return qnaRepository.saveQuestion(post);
    }

    public int saveAnswer(QnaPost post) {
        return qnaRepository.saveAnswer(post);
    }

    public QnaPost findPostById(Integer id) {
        return qnaRepository.findPostById(id)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
    }

    public List<QnaPost> findPosts(SearchCriteria criteria) {
        int startRow = (criteria.getPage() - 1) * PageManager.PAGE_SIZE;
        return qnaRepository.findPosts(criteria, startRow, PageManager.PAGE_SIZE);
    }

    public void updateQuestion(QnaPost post) {
        if(qnaRepository.answerExists(post.getId())) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE, "답변이 존재하는 질문은 수정할 수 없습니다.");
        } else {
            qnaRepository.updatePost(post);
        }
    }

    public void deleteQuestion(Integer id) {
        if(qnaRepository.answerExists(id)) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE, "답변이 존재하는 질문은 삭제할 수 없습니다.");
        } else {
            qnaRepository.deletePost(id);
        }
    }

    public void hitUp(Integer id) {
        qnaRepository.hitUp(id);
    }

    public List<Category> findCategories() {
        return categoryRepository.findCategories(BOARD_ID);
    }

}
