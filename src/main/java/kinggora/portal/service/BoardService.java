package kinggora.portal.service;

import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Pageable;
import kinggora.portal.domain.Post;
import kinggora.portal.domain.type.OrderDirection;
import kinggora.portal.exception.BizException;
import kinggora.portal.exception.ErrorCode;
import kinggora.portal.model.data.request.BoardCriteria;
import kinggora.portal.model.data.request.PagingCriteria;
import kinggora.portal.model.data.request.PostDto;
import kinggora.portal.model.data.response.BoardDetail;
import kinggora.portal.model.data.response.BoardItem;
import kinggora.portal.model.data.response.CommonBoardItem;
import kinggora.portal.model.data.response.QnaBoardItem;
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
    private final BoardInfoService boardInfoService;
    private final CategoryService categoryService;

    /**
     * 루트 게시글 저장
     * parent = null
     *
     * @param boardId  게시판 id
     * @param memberId 작성자 id
     * @param dto      사용자 입력 데이터
     * @return 저장 게시글 id
     */
    public int savePost(int boardId, int memberId, PostDto dto) {
        Post post = dto.toRootPost(boardId, memberId);
        validatePost(post);
        return boardRepository.save(dto.toRootPost(boardId, memberId));
    }

    /**
     * 자식 게시글 저장
     * parent != null
     *
     * @param parentId 부모 게시글 id
     * @param memberId 작성자 id
     * @param dto      사용자 입력 데이터
     * @return 저장 게시글 id
     * @throws BizException 부모에게 이미 자식 게시글이 존재하는 경우 발생
     */
    public int saveChildPost(int parentId, int memberId, PostDto dto) {
        if (boardRepository.hasChild(parentId)) {
            throw new BizException(ErrorCode.ANSWER_ALREADY_EXISTS);
        }
        Post parent = findPostById(parentId);
        Post childPost = dto.toChildPost(parent, memberId);
        validatePost(childPost);
        return boardRepository.save(childPost);
    }

    /**
     * 게시글 수정
     *
     * @param post 수정할 원 게시글 객체
     * @param dto  수정할 데이터
     */
    public void updatePost(Post post, PostDto dto) {
        Post updatePost = dto.toUpdatePost(post);
        validatePost(updatePost);
        boardRepository.update(updatePost);
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
     * 파일이 포함된 게시물 조회
     *
     * @param fileId 파일 id
     * @return 게시글 정보
     * @throws BizException 게시글이 존재하지 않는 경우 발생
     */
    public Post findByFileId(int fileId) {
        return boardRepository.findByFileId(fileId)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
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
     * @param boardCriteria  필터링 조건
     * @return 게시글 목록 리스트
     * @throws BizException 정의되지 않은 boardType 인 경우 발생
     */
    public List<? extends BoardItem> findBoardItems(String boardType, PagingCriteria pagingCriteria, BoardCriteria boardCriteria) {
        switch (boardType) {
            case "L":
            case "I":
                return findCommonBoardItems(pagingCriteria, boardCriteria);
            case "Q":
                return findQnaBoardItems(pagingCriteria, boardCriteria);
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
     * 검색 조건에 해당하는 일반 게시글 조회
     * 생성 날짜(regDate) 내림차 순 정렬로 페이징 처리
     *
     * @param pagingCriteria 페이징 조건
     * @param boardCriteria  필터링 조건
     * @return 게시글 리스트
     */
    public List<CommonBoardItem> findCommonBoardItems(PagingCriteria pagingCriteria, BoardCriteria boardCriteria) {
        Pageable.Order regOrder = new Pageable.Order("reg_date", OrderDirection.DESC);
        Pageable pageable = new Pageable(pagingCriteria.getPage(), pagingCriteria.getSize(), List.of(regOrder));
        return boardRepository.findCommonBoardItems(pageable, boardCriteria);
    }

    /**
     * 검색 조건에 해당하는 QnaBoardItem 조회
     * 생성 날짜(regDate) 내림차 순 정렬로 페이징 처리
     *
     * @param pagingCriteria 페이징 조건
     * @param boardCriteria  필터링 조건
     * @return QnaBoardItem 리스트
     */
    public List<QnaBoardItem> findQnaBoardItems(PagingCriteria pagingCriteria, BoardCriteria boardCriteria) {
        Pageable.Order regOrder = new Pageable.Order("reg_date", OrderDirection.DESC);
        Pageable pageable = new Pageable(pagingCriteria.getPage(), pagingCriteria.getSize(), List.of(regOrder));
        return boardRepository.findQnaBoardItems(pageable, boardCriteria);
    }

    /**
     * 검색 조건에 해당하는 CommonBoardItem 조회
     * 조회수(hit) 내림차, 생성 날짜(reg_date) 내림차 순 정렬로 페이징 처리
     *
     * @param pagingCriteria 페이징 조건
     * @param boardCriteria  필터링 조건
     * @return CommonBoardItem 리스트
     */
    public List<CommonBoardItem> findHitBoardItems(PagingCriteria pagingCriteria, BoardCriteria boardCriteria) {
        Pageable.Order hitOrder = new Pageable.Order("hit", OrderDirection.DESC);
        Pageable.Order regOrder = new Pageable.Order("reg_date", OrderDirection.DESC);
        Pageable pageable = new Pageable(pagingCriteria.getPage(), pagingCriteria.getSize(), List.of(hitOrder, regOrder));
        return boardRepository.findCommonBoardItems(pageable, boardCriteria);
    }

    /**
     * 필터링 조건에 대한 게시글 수 조회
     *
     * @param boardCriteria 필터링 조건
     * @return 게시글 수
     */
    public int findPostsCount(BoardCriteria boardCriteria) {
        return boardRepository.findPostsCount(boardCriteria);
    }

    /**
     * reader가 작성자가 아닌 게시글에 대하여 조회수 1 증가
     *
     * @param id 게시글 id
     */
    public void hitUp(int id, Integer readerId) {
        boardRepository.hitUp(id, readerId);
    }

    /**
     * 게시글 도메인 검증
     *
     * @param post 검증할 도메인
     */
    private void validatePost(Post post) {
        BoardInfo boardInfo = boardInfoService.findBoardInfoById(post.getBoardId());
        if (!categoryService.isCategoryOf(post.getCategoryId(), boardInfo.getId())) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE, "유효하지 않은 카테고리입니다.");
        }
        if (!boardInfo.isAllowSecret() && post.getSecret()) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE, "비밀글을 허용하지 않는 게시판입니다.");
        }
    }
}
