package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.*;
import kinggora.portal.domain.dto.CommentDto;
import kinggora.portal.domain.dto.PostDto;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.exception.BizException;
import kinggora.portal.service.*;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardsController {
    private final CommonPostService commonPostService;
    private final MemberService memberService;
    private final BoardInfoService boardInfoService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    @GetMapping("/info")
    public DataResponse<List<BoardInfo>> boardInfo() {
        log.info("boardInfo");
        List<BoardInfo> boardInfo = boardInfoService.findBoardInfo();
        return DataResponse.of(boardInfo);
    }

    public DataResponse<BoardInfo> boardInfoById(@PathVariable Integer id) {
        BoardInfo boardInfo = boardInfoService.findBoardInfoById(id);
        return DataResponse.of(boardInfo);
    }

    @GetMapping("/info/{name}")
    public DataResponse<BoardInfo> boardInfoById(@PathVariable String name) {
        BoardInfo boardInfo = boardInfoService.findBoardInfoByName(name);
        return DataResponse.of(boardInfo);
    }

    @GetMapping(value = "/category/{boardId}")
    public DataResponse<List<Category>> category(@PathVariable Integer boardId) {
        List<Category> categories = categoryService.findCategories(boardId);
        return DataResponse.of(categories);
    }

    @GetMapping("/post/{id}")
    public DataResponse<CommonPost> getPost(@PathVariable Integer id) {
        commonPostService.hitUp(id);
        CommonPost post = commonPostService.findPostById(id);
        log.info("post={}", post);
        return DataResponse.of(post);
    }

    @GetMapping("/posts")
    public DataResponse<List<CommonPost>> getPosts(SearchCriteria criteria) {
        log.info("getPosts()={}", criteria);
        List<CommonPost> posts = commonPostService.findPosts(criteria);
        return DataResponse.of(posts);
    }

    @PostMapping("/post")
    public DataResponse<Integer> createPost(PostDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        log.info("writer={}", member.getUsername());
        dto.setMemberId(member.getId());
        Integer id = commonPostService.savePost(dto.toCommonPost());
        return DataResponse.of(id);
    }

    @PutMapping("/post")
    public DataResponse<Void> updatePost(PostDto dto) {
        authorizationPost(dto.getId());
        commonPostService.updatePost(dto.toCommonPost());
        return DataResponse.empty();
    }

    @DeleteMapping("/post/{id}")
    public DataResponse<Void> deletePost(@PathVariable Integer id) {
        authorizationPost(id);
        commonPostService.deletePost(id);
        return DataResponse.empty();
    }

    @GetMapping("/comments/{postId}")
    public DataResponse<List<Comment>> getComments(@PathVariable Integer postId) {
        List<Comment> comments = commentService.findComments(postId);
        return DataResponse.of(comments);
    }

    @GetMapping("/comment/{id}")
    public DataResponse<Comment> getComment(@PathVariable Integer id) {
        Comment comment = commentService.findCommentById(id);
        return DataResponse.of(comment);
    }

    @PostMapping("/comment")
    public DataResponse<Integer> createComment(CommentDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        dto.setMemberId(member.getId());
        int id = commentService.saveComment(dto);
        return DataResponse.of(id);

    }

    @PutMapping("/comment")
    public DataResponse<Void> updateComment(CommentDto dto) {
        authorizationComment(dto.getId());
        commentService.updateComment(dto);
        return DataResponse.empty();
    }

    @DeleteMapping("/comment/{id}")
    public DataResponse<Void> deleteComment(@PathVariable Integer id) {
        authorizationComment(id);
        commentService.deleteComment(id);
        return DataResponse.empty();
    }

    /**
     * 게시물의 작성자와 현재 로그인 한 사용자가 일치하는지 확인
     * 일치하지 않으면 UNAUTHORIZED ACCESS 예외 발생
     *
     * @param postId 게시물 id
     */
    private void authorizationPost(Integer postId) {
        Member writer = commonPostService.findPostById(postId).getMember();
        Member signInMember = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        if (!writer.getId().equals(signInMember.getId())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

    /**
     * 댓글 작성자와 현재 로그인 한 사용자가 일치하는지 확인
     * 일치하지 않으면 UNAUTHORIZED ACCESS 예외 발생
     *
     * @param commentId 댓글 id
     */
    private void authorizationComment(Integer commentId) {
        Member writer = commentService.findCommentById(commentId).getMember();
        Member signInMember = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        if (!writer.getId().equals(signInMember.getId())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

}
