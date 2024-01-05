package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Category;
import kinggora.portal.domain.UploadFile;
import kinggora.portal.domain.dto.request.*;
import kinggora.portal.domain.dto.response.*;
import kinggora.portal.security.CustomUserDetails;
import kinggora.portal.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardsController {
    private final BoardsService boardsService;
    private final BoardInfoService boardInfoService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final FileService fileService;

    @GetMapping("/boards")
    public DataResponse<List<BoardInfo>> getBoardInfos() {
        List<BoardInfo> boardInfo = boardInfoService.findBoardInfos();
        return DataResponse.of(boardInfo);
    }

    @GetMapping("/boards/{id}")
    public DataResponse<BoardInfo> getBoardInfo(@PathVariable Id id) {
        BoardInfo boardInfo = boardInfoService.findBoardInfoById(id.getId());
        return DataResponse.of(boardInfo);
    }

    @GetMapping("/boards/{boardId}/categories")
    public DataResponse<List<Category>> getCategories(@PathVariable Id boardId) {
        List<Category> categories = categoryService.findCategories(boardId.getId());
        return DataResponse.of(categories);
    }

    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToBoard(#boardId, 'LIST')")
    @GetMapping("/boards/{boardId}/posts")
    public DataResponse<PagingDto<? extends BoardItem>> getPosts(@PathVariable Id boardId,
                                                                 @RequestParam(defaultValue = "L") String boardType,
                                                                 @ModelAttribute @Valid PagingCriteria pagingCriteria,
                                                                 @ModelAttribute @Valid SearchCriteria searchCriteria) {
        log.info("pagingCriteria={}, searchCriteria={}", pagingCriteria, searchCriteria);
        searchCriteria.injectBoardId(boardId.getId());
        PagingDto<? extends BoardItem> pagingDto;
        PageInfo pageInfo = boardsService.getPageInfo(pagingCriteria, searchCriteria);
        if (boardType.equals("Q")) {
            pagingDto = PagingDto.<QnaBoardItem>builder()
                    .data(boardsService.findQnaBoardItems(pagingCriteria, searchCriteria))
                    .pageInfo(pageInfo)
                    .build();
        } else {
            pagingDto = PagingDto.<CommonBoardItem>builder()
                    .data(boardsService.findCommonBoardItems(pagingCriteria, searchCriteria))
                    .pageInfo(pageInfo)
                    .build();
        }
        return DataResponse.of(pagingDto);
    }

    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'READ')")
    @GetMapping("/posts/{postId}")
    public DataResponse<BoardDetail> getPost(@PathVariable Id postId) {
        boardsService.hitUp(postId.getId());
        BoardDetail boardDetail = boardsService.findBoardDetail(postId.getId());
        return DataResponse.of(boardDetail);
    }

    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToBoard(#boardId, 'WRITE')")
    @PostMapping("/boards/{boardId}/posts")
    public DataResponse<Integer> createPost(@PathVariable Id boardId,
                                            @Valid PostDto dto,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer id = boardsService.savePost(boardId.getId(), userDetails.getId(), dto);
        return DataResponse.of(id);
    }

    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'REPLY-WRITE')")
    @PostMapping("/posts/{postId}/replies")
    public DataResponse<Integer> createChildPost(@PathVariable Id postId,
                                                 @Valid PostDto dto,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer id = boardsService.saveChildPost(postId.getId(), userDetails.getId(), dto);
        return DataResponse.of(id);
    }

    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'REPLY-READ')")
    @GetMapping("/posts/{postId}/replies")
    public DataResponse<List<BoardDetail>> getChildPosts(@PathVariable Id postId) {
        List<BoardDetail> childBoardDetails = boardsService.findChildBoardDetails(postId.getId());
        return DataResponse.of(childBoardDetails);
    }

    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToPost(#postId)")
    @PatchMapping("/posts/{postId}")
    public DataResponse<Void> updatePost(@PathVariable Id postId, @Valid PostDto dto) {
        boardsService.updatePost(postId.getId(), dto);
        return DataResponse.empty();
    }

    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToPost(#postId)")
    @DeleteMapping("/posts/{postId}")
    public DataResponse<Void> deletePost(@PathVariable Id postId) {
        boardsService.deletePostById(postId.getId());
        fileService.deleteFilesByPostId(postId.getId());
        return DataResponse.empty();
    }

    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'FILE')")
    @GetMapping("/posts/{postId}/files")
    public DataResponse<List<UploadFile>> getFiles(@PathVariable Id postId) {
        List<UploadFile> files = fileService.findFilesByPostId(postId.getId());
        return DataResponse.of(files);
    }

    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToPost(#postId)")
    @PostMapping(value = "/posts/{postId}/files", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public DataResponse<Void> saveFiles(@PathVariable Id postId, FileDto dto) {
        fileService.saveFiles(postId.getId(), dto);
        return DataResponse.empty();
    }

    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToFile(#fileId)")
    @DeleteMapping("/files/{fileId}")
    public DataResponse<Void> deleteFile(@PathVariable Id fileId) {
        fileService.deleteFile(fileId.getId());
        return DataResponse.empty();
    }

    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'READ')")
    @GetMapping("/posts/{postId}/comments")
    public DataResponse<List<CommentResponse>> getComments(@PathVariable Id postId) {
        List<CommentResponse> comments = commentService.findComments(postId.getId());
        return DataResponse.of(comments);
    }

    @PreAuthorize("@accessPermissionEvaluator.hasPermissionToPost(#postId, 'COMMENT')")
    @PostMapping("/posts/{postId}/comments")
    public DataResponse<Integer> createComment(@PathVariable Id postId,
                                               @Valid CommentDto dto,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        dto.injectIds(postId.getId(), userDetails.getId());
        int id = commentService.saveRootComment(dto);
        return DataResponse.of(id);
    }

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

    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToComment(#commentId)")
    @PatchMapping("/comments/{commentId}")
    public DataResponse<Void> updateComment(@PathVariable Id commentId, @Valid CommentDto dto) {
        commentService.updateComment(commentId.getId(), dto);
        return DataResponse.empty();
    }

    @PreAuthorize("@ownerPermissionEvaluator.hasPermissionToComment(#commentId)")
    @DeleteMapping("/comments/{commentId}")
    public DataResponse<Void> deleteComment(@PathVariable Id commentId) {
        commentService.deleteComment(commentId.getId());
        return DataResponse.empty();
    }

}
