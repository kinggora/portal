package kinggora.portal.service;

import kinggora.portal.domain.Pageable;
import kinggora.portal.domain.Post;
import kinggora.portal.domain.type.OrderDirection;
import kinggora.portal.exception.BizException;
import kinggora.portal.exception.ErrorCode;
import kinggora.portal.model.data.request.PagingCriteria;
import kinggora.portal.model.data.request.PostDto;
import kinggora.portal.model.data.request.SearchCriteria;
import kinggora.portal.model.data.response.*;
import kinggora.portal.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryService categoryService;

    /**
     * 게시글 저장
     *
     * @param boardId  게시판 id
     * @param memberId 작성자 id
     * @param dto      사용자 입력 데이터
     * @return 저장 게시글 id
     */
    public int savePost(int boardId, int memberId, PostDto dto) {
        validateCategory(dto.getCategoryId(), boardId);
        return boardRepository.save(dto.toRootPost(boardId, memberId));
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
        return boardRepository.save(dto.toChildPost(parent, memberId));
    }

    /**
     * 게시글 수정
     * 자식 게시글 존재 시 게시물 수정 불가
     *
     * @param postId 수정할 게시글 id
     * @param dto    수정할 데이터
     */
    public void updatePost(int postId, PostDto dto) {
        Post post = findPostById(postId);
        if (boardRepository.hasChild(post.getId())) {
            throw new BizException(ErrorCode.ANSWER_ALREADY_EXISTS);
        }
        validateCategory(dto.getCategoryId(), post.getBoardId());
        boardRepository.update(dto.toUpdatePost(postId));
    }

    /**
     * 게시글 삭제
     *
     * @param id 게시글 id
     */
    public void deletePostById(int id) {
        boardRepository.deleteById(id);
    }

    /**
     * 게시글 단건 조회
     *
     * @param id 게시글 id
     * @return 게시글 정보
     * @throws BizException 게시글이 존재하지 않거나 이미 삭제된 경우 발생
     */
    public Post findPostById(int id) {
        Post post = boardRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_POST);
        }
        return post;
    }

    /**
     * 게시글 상세 조회
     *
     * @param id 게시글 id
     * @return 게시글 상세
     */
    public BoardDetail findBoardDetail(int id) {
        return boardRepository.findBoardDetail(id)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
    }

    /**
     * 검색 조건에 해당하는 게시글 목록 조회 + 페이징 처리
     * boardType에 따라 다른 BoardItem 상속 객체 반환
     * boardType == L | I : CommonBoardItem
     * boardType == Q : QnaBoardItem
     *
     * @param boardType      게시판 타입
     * @param pagingCriteria 페이징 조건
     * @param searchCriteria 검색 조건
     * @return 게시글 목록 리스트
     * @throws BizException 정의되지 않은 boardType 인 경우 발생
     */
    public List<? extends BoardItem> findBoardItems(String boardType, PagingCriteria pagingCriteria, SearchCriteria searchCriteria) {
        switch (boardType) {
            case "L":
            case "I":
                return findCommonBoardItems(pagingCriteria, searchCriteria);
            case "Q":
                return findQnaBoardItems(pagingCriteria, searchCriteria);
            default:
                throw new BizException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    /**
     * 자식 게시글 상세 조회
     * 부모 id 로 자식 id를 찾아 게시글 상세 조회
     *
     * @param parentId 부모 게시글 id
     * @return 자식 게시글 상세
     * @throws BizException 자식이 존재하지 않는 경우 발생
     */
    public BoardDetail findChildBoardDetail(int parentId) {
        Optional<Integer> child = boardRepository.findChild(parentId);
        int id = child.orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
        return findBoardDetail(id);
    }

    /**
     * 파일이 포함된 게시물 조회
     *
     * @param fileId 파일 id
     * @return 게시글 정보
     * @throws BizException 게시글이 존재하지 않거나 이미 삭제된 경우 발생
     */
    public Post findPostByFileId(int fileId) {
        Post post = boardRepository.findByFileId(fileId)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_POST);
        }
        return post;
    }

    /**
     * 검색 조건에 해당하는 일반 게시글 조회
     * 생성 날짜(regDate) 내림차 순 정렬로 페이징 처리
     *
     * @param pagingCriteria 페이징 조건
     * @param searchCriteria 검색 조건
     * @return 게시글 리스트
     */
    private List<CommonBoardItem> findCommonBoardItems(PagingCriteria pagingCriteria, SearchCriteria searchCriteria) {
        Pageable.Order regOrder = new Pageable.Order("reg_date", OrderDirection.DESC);
        Pageable pageable = new Pageable(pagingCriteria.getPage(), pagingCriteria.getSize(), List.of(regOrder));
        return boardRepository.findCommonBoardItems(pageable, searchCriteria);
    }

    /**
     * 검색 조건에 해당하는 QnaBoardItem 조회
     * 생성 날짜(regDate) 내림차 순 정렬로 페이징 처리
     *
     * @param pagingCriteria 페이징 조건
     * @param searchCriteria 검색 조건
     * @return QnaBoardItem 리스트
     */
    private List<QnaBoardItem> findQnaBoardItems(PagingCriteria pagingCriteria, SearchCriteria searchCriteria) {
        Pageable.Order regOrder = new Pageable.Order("reg_date", OrderDirection.DESC);
        Pageable pageable = new Pageable(pagingCriteria.getPage(), pagingCriteria.getSize(), List.of(regOrder));
        return boardRepository.findQnaBoardItems(pageable, searchCriteria);
    }

    /**
     * 검색 조건에 해당하는 CommonBoardItem 조회
     * 조회수(hit) 내림차, 생성 날짜(reg_date) 내림차 순 정렬로 페이징 처리
     *
     * @param pagingCriteria 페이징 조건
     * @param searchCriteria 검색 조건
     * @return CommonBoardItem 리스트
     */
    public List<CommonBoardItem> findHitBoardItems(PagingCriteria pagingCriteria, SearchCriteria searchCriteria) {
        Pageable.Order hitOrder = new Pageable.Order("hit", OrderDirection.DESC);
        Pageable.Order regOrder = new Pageable.Order("reg_date", OrderDirection.DESC);
        Pageable pageable = new Pageable(pagingCriteria.getPage(), pagingCriteria.getSize(), List.of(hitOrder, regOrder));
        return boardRepository.findCommonBoardItems(pageable, searchCriteria);
    }

    /**
     * 페이징 메타 데이터(PageInfo) 생성
     * totalCount = 검색 조건으로 필터링한 row 수 조회
     * totalPage = totalCount가 0이라면 1, 아니라면 pageSize를 이용하여 계산
     *
     * @param pagingCriteria 페이징 조건
     * @param searchCriteria 검색 조건
     * @return PageInfo
     */
    public PageInfo getPageInfo(PagingCriteria pagingCriteria, SearchCriteria searchCriteria) {
        int totalCount = boardRepository.findPostsCount(searchCriteria);
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
    public void hitUp(int id) {
        boardRepository.hitUp(id);
    }

    private void validateCategory(int categoryId, int boardId) {
        if (!categoryService.isCategoryOf(categoryId, boardId)) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE, "유효하지 않은 카테고리입니다.");
        }
    }
}
