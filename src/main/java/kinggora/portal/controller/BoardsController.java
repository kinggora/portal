package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.*;
import kinggora.portal.domain.dto.*;
import kinggora.portal.domain.type.MemberRole;
import kinggora.portal.exception.BizException;
import kinggora.portal.service.*;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardsController {
    private final BoardsService boardsService;
    private final MemberService memberService;
    private final BoardInfoService boardInfoService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    @GetMapping("/infos")
    public DataResponse<List<BoardInfo>> boardInfo() {
        log.info("boardInfo");
        List<BoardInfo> boardInfo = boardInfoService.findBoardInfo();
        return DataResponse.of(boardInfo);
    }

    public DataResponse<BoardInfo> boardInfoById(@PathVariable Integer id) {
        BoardInfo boardInfo = boardInfoService.findBoardInfoById(id);
        return DataResponse.of(boardInfo);
    }

    @GetMapping("/infos/{name}")
    public DataResponse<BoardInfo> boardInfoById(@PathVariable String name) {
        BoardInfo boardInfo = boardInfoService.findBoardInfoByName(name);
        return DataResponse.of(boardInfo);
    }

    @GetMapping(value = "/categories/{boardId}")
    public DataResponse<List<Category>> getCategories(@PathVariable Integer boardId) {
        List<Category> categories = categoryService.findCategories(boardId);
        return DataResponse.of(categories);
    }

    @GetMapping("/posts/{id}")
    public DataResponse<Post> getPost(@PathVariable Integer id) {
        boardsService.hitUp(id);
        Post post = boardsService.findPostById(id);
        return DataResponse.of(post);
    }

    @GetMapping("/posts")
    public DataResponse<PagingDto<CommonPost>> getPosts(@ModelAttribute PagingCriteria pagingCriteria, @ModelAttribute @Valid SearchCriteria searchCriteria) {
        log.info("pagingCriteria={}, searchCriteria={}", pagingCriteria, searchCriteria);
        List<CommonPost> posts = boardsService.findPosts(pagingCriteria, searchCriteria);
        PageInfo pageInfo = boardsService.getPageInfo(pagingCriteria, searchCriteria);
        PagingDto<CommonPost> pagingDto = PagingDto.<CommonPost>builder()
                .data(posts)
                .pageInfo(pageInfo)
                .build();
        return DataResponse.of(pagingDto);
    }

    @GetMapping("/posts/qna")
    public DataResponse<PagingDto<QnaPost>> getQuestions(@ModelAttribute PagingCriteria pagingCriteria, @ModelAttribute @Valid SearchCriteria searchCriteria) {
        log.info("pagingCriteria={}, searchCriteria={}", pagingCriteria, searchCriteria);
        List<QnaPost> questions = boardsService.findQuestions(pagingCriteria, searchCriteria);
        PageInfo pageInfo = boardsService.getPageInfo(pagingCriteria, searchCriteria);
        PagingDto<QnaPost> pagingDto = PagingDto.<QnaPost>builder()
                .data(questions)
                .pageInfo(pageInfo)
                .build();
        return DataResponse.of(pagingDto);
    }

    @GetMapping("/posts/qna/{id}")
    public DataResponse<List<Post>> getQnA(@PathVariable Integer id) {
        Post question = boardsService.findPostById(id);
        List<Post> qnaPosts = boardsService.findChildPosts(id);
        qnaPosts.add(question);
        return DataResponse.of(qnaPosts);
    }

    @PostMapping("/posts")
    public DataResponse<Integer> createPost(@Valid PostDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        Integer id = boardsService.savePost(dto.toPost(member.getId()));
        return DataResponse.of(id);
    }

    @PostMapping("/posts/qna")
    public DataResponse<Integer> createAnswer(@Valid PostDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        if (!member.getRole().contains(MemberRole.ADMIN)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        Integer id = boardsService.savePost(dto.toPost(member.getId()));
        return DataResponse.of(id);
    }

    @PutMapping("/posts/{id}")
    public DataResponse<Void> updatePost(@PathVariable Integer id, @Valid PostDto dto) {
        authorizationPost(id);
        boardsService.updatePost(dto.toUpdatePost(id));
        return DataResponse.empty();
    }


    @DeleteMapping("/posts/{id}")
    public DataResponse<Void> deletePost(@PathVariable Integer id) {
        authorizationPost(id);
        boardsService.deletePost(id);
        return DataResponse.empty();
    }

    @GetMapping("/comments/{postId}")
    public DataResponse<List<Comment>> getComments(@PathVariable Integer postId) {
        List<Comment> comments = commentService.findComments(postId);
        return DataResponse.of(comments);
    }

    @PostMapping("/comments")
    public DataResponse<Integer> createComment(@Valid CommentDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        dto.setMemberId(member.getId());
        int id = commentService.saveComment(dto);
        return DataResponse.of(id);
    }

    @PostMapping("/comments/{parent}")
    public DataResponse<Integer> createChildComment(@PathVariable Integer parent, @Valid CommentDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        dto.setMemberId(member.getId());
        int id = commentService.saveChildComment(parent, dto);
        return DataResponse.of(id);
    }

    @PutMapping("/comments")
    public DataResponse<Void> updateComment(@Valid CommentDto dto) {
        authorizationComment(dto.getId());
        commentService.updateComment(dto);
        return DataResponse.empty();
    }

    @DeleteMapping("/comments/{id}")
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
        Member writer = boardsService.findPostById(postId).getMember();
        if (!SecurityUtil.getCurrentUsername().equals(writer.getUsername())) {
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
        if (!SecurityUtil.getCurrentUsername().equals(writer.getUsername())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

}
