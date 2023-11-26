package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.CommonPost;
import kinggora.portal.domain.Post;
import kinggora.portal.domain.QnaPost;
import kinggora.portal.domain.dto.PageInfo;
import kinggora.portal.domain.dto.PagingCriteria;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.BoardsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Post> optional = boardsRepository.findPostById(id);
        if (optional.isEmpty()) {
            throw new BizException(ErrorCode.POST_NOT_FOUND);
        }
        Post post = optional.get();
        if (post.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_POST);
        }
        return post;
    }

    /**
     * 검색 조건에 해당하는 게시글 조회 + 페이징 처리
     *
     * @param pagingCriteria 페이징 조건
     * @param searchCriteria 검색 조건
     * @return 게시글 리스트
     */
    public List<CommonPost> findPosts(PagingCriteria pagingCriteria, SearchCriteria searchCriteria) {
        int startRow = (pagingCriteria.getPage() - 1) * pagingCriteria.getSize();
        return boardsRepository.findPosts(searchCriteria, startRow, pagingCriteria.getSize());
    }

    /**
     * 검색 조건에 해당하는 질문 게시글 조회 + 페이징 처리
     * 질문 게시글: parent = null
     *
     * @param pagingCriteria 페이징 조건
     * @param searchCriteria 검색 조건
     * @return 게시글 리스트
     */
    public List<QnaPost> findQuestions(PagingCriteria pagingCriteria, SearchCriteria searchCriteria) {
        int startRow = (pagingCriteria.getPage() - 1) * pagingCriteria.getSize();
        return boardsRepository.findQuestions(searchCriteria, startRow, pagingCriteria.getSize());
    }

    /**
     * 답변 게시물(parent=parentId) 조회
     *
     * @param parentId
     * @return
     */
    public List<Post> findChildPosts(int parentId) {
        return boardsRepository.findChildPosts(parentId);
    }

    public PageInfo getPageInfo(PagingCriteria pagingCriteria, SearchCriteria searchCriteria) {
        int totalCount = boardsRepository.findPostsCount(searchCriteria);
        int totalPages = totalCount == 0 ? 1 : (totalCount - 1) / pagingCriteria.getSize() + 1;
        return PageInfo.builder()
                .pageNum(pagingCriteria.getPage())
                .pageSize(pagingCriteria.getSize())
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
