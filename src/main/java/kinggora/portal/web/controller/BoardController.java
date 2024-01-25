package kinggora.portal.web.controller;

import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Category;
import kinggora.portal.domain.UploadFile;
import kinggora.portal.model.data.DataResponse;
import kinggora.portal.model.data.PagingResponse;
import kinggora.portal.model.data.request.*;
import kinggora.portal.model.data.response.*;
import kinggora.portal.security.user.CustomUserDetails;
import kinggora.portal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 게시판 API 컨트롤러
 * 게시판 정보, 게시글, 카테고리, 댓글, 파일 정보에 대한 API 요청의 응답을 JSON 형태로 반환
 * 응답 객체: DataResponse
 */
@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardInfoService boardInfoService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final FileService fileService;

    /**
     * 모든 게시판의 정보 조회 요청 처리
     *
     * @return 게시판 정보 리스트 API Response
     */
    @GetMapping("/boards")
    public DataResponse<List<BoardInfo>> getBoardInfos() {
        List<BoardInfo> boardInfo = boardInfoService.findBoardInfos();
        return DataResponse.of(boardInfo);
    }

    /**
     * 단일 게시판에 대한 정보 조회 요청 처리
     *
     * @param boardId 게시판 id
     * @return 게시판 정보 API Response
     */
    @GetMapping("/boards/{boardId}")
    public DataResponse<BoardInfo> getBoardInfo(@PathVariable Id boardId) {
        BoardInfo boardInfo = boardInfoService.findBoardInfoById(boardId.getId());
        return DataResponse.of(boardInfo);
    }

    /**
     * 단일 게시판에 대한 카테고리 리스트 조회 요청 처리
     *
     * @param boardId 게시판 id
     * @return DataResponse<List < Category>>
     */
    @GetMapping("/boards/{boardId}/categories")
    public DataResponse<List<Category>> getCategories(@PathVariable Id boardId) {
        List<Category> categories = categoryService.findCategories(boardId.getId());
        return DataResponse.of(categories);
    }

    /**
     * 게시글 목록 리스트 + 페이징 정보 조회 요청 처리
     * boardType에 따라 다른 BoardItem 조회
     * - boardType == "Q" : QnaBoardItem
     * - boardType == "L"(default) or else : CommonBoardItem
     *
     * @param boardId        게시판 id
     * @param boardType      반환할 BoardItem 종류
     * @param pagingCriteria 페이징 조건
     * @param searchCriteria 필터링 조건
     * @return 게시글 목록 리스트 & 페이징 정보 API Response
     */
    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToBoard(#boardId, 'LIST')")
    @GetMapping("/boards/{boardId}/posts")
    public PagingResponse<? extends BoardItem> getPosts(@PathVariable Id boardId,
                                                        @RequestParam(defaultValue = "L") String boardType,
                                                        @ModelAttribute @Valid PagingCriteria pagingCriteria,
                                                        @ModelAttribute @Valid SearchCriteria searchCriteria) {
        searchCriteria.injectBoardId(boardId.getId());
        List<? extends BoardItem> boardItems = boardService.findBoardItems(boardType, pagingCriteria, searchCriteria);
        PageInfo pageInfo = boardService.getPageInfo(pagingCriteria, searchCriteria);
        return new PagingResponse<>(boardItems, pageInfo);
    }

    /**
     * 조회수(hit)로 내림차 정렬한 게시글 목록 리스트 + 페이징 정보 조회 요청 처리
     *
     * @param pagingCriteria 페이징 조건
     * @param searchCriteria 필터링 조건
     * @return 게시글 목록 리스트 & 페이징 정보 API Response
     */
    @GetMapping("/boards/hit/posts")
    public PagingResponse<? extends BoardItem> getHitPosts(@ModelAttribute @Valid PagingCriteria pagingCriteria,
                                                           @ModelAttribute @Valid SearchCriteria searchCriteria) {
        List<CommonBoardItem> hitBoardItems = boardService.findHitBoardItems(pagingCriteria, searchCriteria);
        PageInfo pageInfo = boardService.getPageInfo(pagingCriteria, searchCriteria);
        return PagingResponse.<CommonBoardItem>builder()
                .data(hitBoardItems)
                .pageInfo(pageInfo)
                .build();
    }

    /**
     * 단일 게시글 상세 조회 요청 처리
     *
     * @param postId 게시글 id
     * @return 게시글 상세 API Response
     */
    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'READ')")
    @GetMapping("/posts/{postId}")
    public DataResponse<BoardDetail> getPost(@PathVariable Id postId) {
        boardService.hitUp(postId.getId());
        BoardDetail boardDetail = boardService.findBoardDetail(postId.getId());
        return DataResponse.of(boardDetail);
    }

    /**
     * 게시글 등록 요청 처리
     *
     * @param boardId     게시글을 등록할 게시판 id
     * @param dto         게시글 등록 데이터
     * @param userDetails 인증 객체
     * @return 등록된 게시글 id API Response
     */
    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToBoard(#boardId, 'WRITE')")
    @PostMapping("/boards/{boardId}/posts")
    public DataResponse<Integer> createPost(@PathVariable Id boardId,
                                            @Valid PostDto dto,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer id = boardService.savePost(boardId.getId(), userDetails.getId(), dto);
        return DataResponse.of(id);
    }

    /**
     * 자식 게시글 등록 요청 처리
     *
     * @param postId      부모 게시글 id
     * @param dto         게시글 등록 데이터
     * @param userDetails 인증 객체
     * @return 등록된 게시글 id API Response
     */
    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'REPLY-WRITE')")
    @PostMapping("/posts/{postId}/replies")
    public DataResponse<Integer> createChildPost(@PathVariable Id postId,
                                                 @Valid PostDto dto,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer id = boardService.saveChildPost(postId.getId(), userDetails.getId(), dto);
        return DataResponse.of(id);
    }

    /**
     * 자식 게시물 상세 조회 요청 처리
     *
     * @param postId 부모 게시글 id
     * @return 자식 게시글 리스트 API Response
     */
    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'REPLY-READ')")
    @GetMapping("/posts/{postId}/replies")
    public DataResponse<BoardDetail> getChildPost(@PathVariable Id postId) {
        BoardDetail boardDetail = boardService.findChildBoardDetail(postId.getId());
        return DataResponse.of(boardDetail);
    }

    /**
     * 게시글 수정 요청 처리
     *
     * @param postId 수정할 게시글 id
     * @param dto    게시글 수정 데이터
     * @return Empty API Response
     */
    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToPost(#postId)")
    @PatchMapping("/posts/{postId}")
    public DataResponse<Void> updatePost(@PathVariable Id postId, @Valid PostDto dto) {
        boardService.updatePost(postId.getId(), dto);
        return DataResponse.empty();
    }

    /**
     * 게시글 삭제 요청 처리
     * 게시글, 첨부 파일 삭제
     *
     * @param postId 삭제할 게시글 id
     * @return Empty API Response
     */
    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToPost(#postId)")
    @DeleteMapping("/posts/{postId}")
    public DataResponse<Void> deletePost(@PathVariable Id postId) {
        boardService.deletePostById(postId.getId());
        fileService.deleteFilesByPostId(postId.getId());
        return DataResponse.empty();
    }

    /**
     * 게시글 첨부파일 리스트 조회 요청 처리
     *
     * @param postId 게시글 id
     * @return 첨부파일 리스트 API Response
     */
    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'FILE')")
    @GetMapping("/posts/{postId}/files")
    public DataResponse<List<UploadFile>> getFiles(@PathVariable Id postId) {
        List<UploadFile> files = fileService.findFilesByPostId(postId.getId());
        return DataResponse.of(files);
    }

    /**
     * 게시글 첨부파일 저장 요청 처리
     *
     * @param postId 게시글 id
     * @param dto    파일 저장 데이터 (Multipart File)
     * @return Empty API Response
     */
    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToPost(#postId)")
    @PostMapping(value = "/posts/{postId}/files", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public DataResponse<Void> saveFiles(@PathVariable Id postId, FileDto dto) {
        fileService.saveFiles(postId.getId(), dto);
        return DataResponse.empty();
    }

    /**
     * 파일 삭제 요청 처리
     *
     * @param fileId 삭제할 파일 id
     * @return Empty API Response
     */
    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToFile(#fileId)")
    @DeleteMapping("/files/{fileId}")
    public DataResponse<Void> deleteFile(@PathVariable Id fileId) {
        fileService.deleteFile(fileId.getId());
        return DataResponse.empty();
    }

    /**
     * 댓글 리스트 조회 요청 처리
     *
     * @param postId 댓글을 조회할 게시글 id
     * @return 댓글 리스트 API Response
     */
    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'READ')")
    @GetMapping("/posts/{postId}/comments")
    public DataResponse<List<CommentResponse>> getComments(@PathVariable Id postId) {
        List<CommentResponse> comments = commentService.findComments(postId.getId());
        return DataResponse.of(comments);
    }

    /**
     * 댓글 등록 요청 처리
     *
     * @param postId      댓글을 등록할 게시글 id
     * @param dto         댓글 등록 데이터
     * @param userDetails 인증 객체
     * @return 등록된 댓글 id API Response
     */
    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'COMMENT')")
    @PostMapping("/posts/{postId}/comments")
    public DataResponse<Integer> createComment(@PathVariable Id postId,
                                               @Valid CommentDto dto,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        dto.injectIds(postId.getId(), userDetails.getId());
        int id = commentService.saveRootComment(dto);
        return DataResponse.of(id);
    }

    /**
     * 자식 댓글 등록 요청 처리
     *
     * @param postId      댓글을 등록할 게시글 id
     * @param commentId   부모 댓글 id
     * @param dto         댓글 등록 데이터
     * @param userDetails 인증 객체
     * @return 등록된 댓글 id API Response
     */
    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'COMMENT')")
    @PostMapping("/posts/{postId}/comments/{commentId}/replies")
    public DataResponse<Integer> createChildComment(@PathVariable Id postId,
                                                    @PathVariable Id commentId,
                                                    @Valid CommentDto dto,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        dto.injectIds(postId.getId(), userDetails.getId());
        int id = commentService.saveChildComment(commentId.getId(), dto);
        return DataResponse.of(id);
    }

    /**
     * 댓글 수정 요청 처리
     *
     * @param commentId 수정할 댓글 id
     * @param dto       댓글 수정 데이터
     * @return Empty API Response
     */
    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToComment(#commentId)")
    @PatchMapping("/comments/{commentId}")
    public DataResponse<Void> updateComment(@PathVariable Id commentId, @Valid CommentDto dto) {
        commentService.updateComment(commentId.getId(), dto);
        return DataResponse.empty();
    }

    /**
     * 댓글 삭제 요청 처리
     *
     * @param commentId 삭제할 댓글 id
     * @return Empty API Response
     */
    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToComment(#commentId)")
    @DeleteMapping("/comments/{commentId}")
    public DataResponse<Void> deleteComment(@PathVariable Id commentId) {
        commentService.deleteComment(commentId.getId());
        return DataResponse.empty();
    }

}
