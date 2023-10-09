package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Category;
import kinggora.portal.domain.CommonPost;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.PostDto;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.exception.BizException;
import kinggora.portal.service.BoardInfoService;
import kinggora.portal.service.CategoryService;
import kinggora.portal.service.CommonPostService;
import kinggora.portal.service.MemberService;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("boards")
public class BoardsController {
    private final CommonPostService commonPostService;
    private final MemberService memberService;

    private final BoardInfoService boardInfoService;
    private final CategoryService categoryService;

    @GetMapping("/info")
    public DataResponse<List<BoardInfo>> boardInfo() {
        List<BoardInfo> boardInfo = boardInfoService.findBoardInfo();
        return DataResponse.of(boardInfo);
    }

    @GetMapping("/info/{id}")
    public DataResponse<BoardInfo> boardInfoById(@PathVariable Integer id) {
        BoardInfo boardInfo = boardInfoService.findBoardInfoById(id);
        return DataResponse.of(boardInfo);
    }

    @GetMapping(value = "/category/{boardId}")
    public DataResponse<List<Category>> category(@PathVariable Integer boardId) {
        List<Category> categories = categoryService.findCategories(boardId);
        return DataResponse.of(categories);
    }

    @GetMapping("/post/{id}")
    public DataResponse<CommonPost> getPost(@PathVariable int id) {
        commonPostService.hitUp(id);
        CommonPost post = commonPostService.findPostById(id);
        return DataResponse.of(post);
    }

    @GetMapping("/posts")
    public DataResponse<List<CommonPost>> getPosts(SearchCriteria criteria) {
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
        authorizationWriter(dto.getId());
        commonPostService.updatePost(dto.toCommonPost());
        return DataResponse.empty();
    }

    @DeleteMapping("/post/{id}")
    public DataResponse<Void> deletePost(@PathVariable int id) {
        authorizationWriter(id);
        commonPostService.deletePost(id);
        return DataResponse.empty();
    }

    /**
     * 게시물의 작성자와 현재 로그인 한 사용자가 일치하는지 확인
     * 일치하지 않으면 UNAUTHORIZED ACCESS 예외 발생
     *
     * @param postId 게시물 id
     */
    private void authorizationWriter(Integer postId) {
        Member writer = commonPostService.findPostById(postId).getMember();
        Member signInMember = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        if (!writer.getId().equals(signInMember.getId())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

}
