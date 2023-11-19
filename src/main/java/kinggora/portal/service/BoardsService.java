package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.CommonPost;
import kinggora.portal.domain.Post;
import kinggora.portal.domain.QnaPost;
import kinggora.portal.domain.dto.PageInfo;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.BoardsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardsService {

    private final BoardsRepository boardsRepository;

    /**
     * 게시글 저장
     *
     * @param post
     * @return 게시글 id
     */
    public int savePost(Post post) {
        return boardsRepository.savePost(post);
    }

    /**
     * 게시글 수정
     *
     * @param post 수정할 데이터
     */
    public void updatePost(Post post) {
        boardsRepository.updatePost(post);
    }

    /**
     * 게시글 삭제
     *
     * @param id 게시글 id
     */
    public void deletePost(Integer id) {
        boardsRepository.deletePost(id);
    }

    /**
     * 게시글 단건 조회
     *
     * @param id 게시글 id
     * @return 게시글 정보
     */
    public Post findPostById(Integer id) {
        return boardsRepository.findPostById(id)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
    }

    /**
     * 검색 조건에 해당하는 게시글 조회 + 페이징 처리
     *
     * @param criteria 검색 조건
     * @return 게시글 리스트
     */
    public List<CommonPost> findPosts(SearchCriteria criteria) {
        int startRow = (criteria.getPage() - 1) * criteria.getPageSize();
        return boardsRepository.findPosts(criteria, startRow, criteria.getPageSize());
    }

    /**
     * 검색 조건에 해당하는 질문 게시글 조회 + 페이징 처리
     * 질문 게시글: parent = null
     *
     * @param criteria 검색 조건
     * @return 게시글 리스트
     */
    public List<QnaPost> findQuestions(SearchCriteria criteria) {
        int startRow = (criteria.getPage() - 1) * criteria.getPageSize();
        return boardsRepository.findQuestions(criteria, startRow, criteria.getPageSize());
    }

    /**
     * 질문(id=parentId) 게시글과 답변 게시물(parent=parentId)을 조회
     *
     * @param parentId
     * @return
     */
    public List<Post> findQnA(int parentId) {
        return boardsRepository.findQnA(parentId);
    }

    public PageInfo getPageInfo(SearchCriteria criteria) {
        int totalCount = boardsRepository.findPostsCount(criteria);
        int totalPages = totalCount == 0 ? 1 : (totalCount - 1) / criteria.getPageSize() + 1;
        return PageInfo.builder()
                .pageNum(criteria.getPage())
                .pageSize(criteria.getPageSize())
                .totalCount(totalCount)
                .totalPages(totalPages)
                .build();
    }

    /**
     * 게시글 조회수 1 증가
     *
     * @param id 게시글 id
     */
    public void hitUp(Integer id) {
        boardsRepository.hitUp(id);
    }

    /**
     * 질문(question) 수정
     * child가 존재 시 수정 불가
     *
     * @param post 수정할 데이터
     */
    public void updateQuestion(Post post) {
        if (boardsRepository.childExists(post.getId())) {
            throw new BizException(ErrorCode.ANSWER_ALREADY_EXISTS);
        } else {
            boardsRepository.updatePost(post);
        }
    }
}
