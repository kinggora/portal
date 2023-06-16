package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.FreePost;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.PostDto;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.exception.BizException;
import kinggora.portal.service.FreeService;
import kinggora.portal.service.MemberService;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/free")
public class FreeController {
    private final FreeService freeService;
    private final MemberService memberService;

    @GetMapping("/posts/{id}")
    public DataResponse<FreePost> getPost(@PathVariable int id) {
        freeService.hitUp(id);
        FreePost post = freeService.findPostById(id);
        return DataResponse.of(post);
    }

    @GetMapping("/posts")
    public DataResponse<List<FreePost>> getPosts(SearchCriteria criteria) {
        List<FreePost> posts = freeService.findPosts(criteria);
        return DataResponse.of(posts);
    }

    @PostMapping("/post")
    public DataResponse<Integer> createPost(@RequestBody PostDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        dto.setMemberId(member.getId());
        Integer id = freeService.savePost(dto.toFreePost());
        return DataResponse.of(id);
    }

    @PutMapping("/post")
    public DataResponse<Void> updatePost(@RequestBody PostDto dto) {
        authorizationWriter(dto.getId());
        freeService.updatePost(dto.toFreePost());
        return DataResponse.empty();
    }

    @DeleteMapping("/post/{id}")
    public DataResponse<Void> deletePost(@PathVariable int id) {
        authorizationWriter(id);
        freeService.deletePost(id);
        return DataResponse.empty();
    }

    /**
     * 게시물의 작성자와 현재 로그인 한 사용자가 일치하는지 확인
     * 일치하지 않으면 UNAUTHORIZED ACCESS 예외 발생
     * @param postId 게시물 id
     */
    private void authorizationWriter(Integer postId){
        Member writer = freeService.findPostById(postId).getMember();
        Member signInMember = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        if(!writer.getId().equals(signInMember.getId())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}
