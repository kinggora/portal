package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.Post;
import kinggora.portal.domain.dto.request.PagingCriteria;
import kinggora.portal.domain.dto.request.PostDto;
import kinggora.portal.domain.dto.request.SearchCriteria;
import kinggora.portal.domain.dto.response.BoardDetail;
import kinggora.portal.domain.dto.response.CommonBoardItem;
import kinggora.portal.domain.dto.response.PageInfo;
import kinggora.portal.domain.dto.response.QnaBoardItem;
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
     * @param memberId 작성자 id
     * @param dto      사용자 입력 데이터
     * @return 저장 게시글 id
     */
    public int savePost(int boardId, int memberId, PostDto dto) {
        return boardsRepository.savePost(dto.toRootPost(boardId, memberId));
    }

    /**
     * 자식 게시글 저장
     *
     * @param parentId 부모 게시글 id
     * @param memberId 작성자 id
     * @param dto      사용자 입력 데이터
     * @return 저장 게시글 id
     */
    public int saveChildPost(int parentId, int memberId, PostDto dto) {
        Post parent = findPostById(parentId);
        return boardsRepository.savePost(dto.toChildPost(parent, memberId));
    }

    /**
     * 게시글 수정
     *
     * @param postId 수정할 게시글 id
     * @param dto    수정할 데이터
     */
    public void updatePost(int postId, PostDto dto) {
        if (boardsRepository.existById(postId)) {
            boardsRepository.updatePost(dto.toUpdatePost(postId));
        } else {
            throw new BizException(ErrorCode.POST_NOT_FOUND);
        }
    }

    /**
     * 게시글 삭제
     *
     * @param id 게시글 id
     */
    public void deletePostById(int id) {
        if (boardsRepository.existById(id)) {
            boardsRepository.deletePostById(id);
        } else {
            throw new BizException(ErrorCode.POST_NOT_FOUND);
        }
    }

    /**
     * 게시글 단건 조회
     *
     * @param id 게시글 id
     * @return 게시글 정보
     */
    public Post findPostById(int id) {
        Post post = boardsRepository.findPostById(id)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
        return post;
    }

    public BoardDetail findBoardDetail(int id) {
        BoardDetail boardDetail = boardsRepository.findBoardDetail(id)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
        if (boardDetail.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_POST);
        }
        return boardDetail;
    }

    /**
     * 답변 게시물(parent=parentId) 조회
     *
     * @param parentId 부모 게시글 id
     * @return
     */
    public List<BoardDetail> findChildBoardDetails(int parentId) {
        return boardsRepository.findChildBoardDetails(parentId);
    }

    /**
     * 해당 파일이 포함된 게시물 조회
     *
     * @param fileId 파일 id
     * @return 게시글 정보
     */
    public Post findPostByFileId(int fileId) {
        Post post = boardsRepository.findPostByFileId(fileId)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
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
    public List<CommonBoardItem> findCommonBoardItems(PagingCriteria pagingCriteria, SearchCriteria searchCriteria) {
        int startRow = (pagingCriteria.getPage() - 1) * pagingCriteria.getSize();
        return boardsRepository.findCommonBoardItems(searchCriteria, startRow, pagingCriteria.getSize());
    }

    /**
     * 검색 조건에 해당하는 질문 게시글 조회 + 페이징 처리
     * 질문 게시글: parent = null
     *
     * @param pagingCriteria 페이징 조건
     * @param searchCriteria 검색 조건
     * @return 게시글 리스트
     */
    public List<QnaBoardItem> findQnaBoardItems(PagingCriteria pagingCriteria, SearchCriteria searchCriteria) {
        int startRow = (pagingCriteria.getPage() - 1) * pagingCriteria.getSize();
        return boardsRepository.findQnaBoardItems(searchCriteria, startRow, pagingCriteria.getSize());
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
