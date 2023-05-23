package kinggora.portal.controller;


import kinggora.portal.api.DataResponse;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.Category;
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

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/free")
public class FreeController {
    private final FreeService freeService;
    private final MemberService memberService;

    @GetMapping(value = "/category")
    public DataResponse<List<Category>> category() {
        List<Category> categories = freeService.findCategories();
        return DataResponse.of(categories);
    }

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

    @PostMapping("/posts")
    public DataResponse<Integer> createPost(@RequestBody PostDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        dto.setMemberId(member.getId());
        Integer id = freeService.savePost(dto.toFreePost());
        return DataResponse.of(id);
    }

    @PutMapping("/posts")
    public DataResponse<Void> updatePost(@RequestBody PostDto dto) {
        authorization(dto.getId());
        freeService.updatePost(dto.toFreePost());
        return DataResponse.empty();
    }

    @DeleteMapping("/posts/{id}")
    public DataResponse<Void> deletePost(@PathVariable int id) {
        authorization(id);
        freeService.deletePost(id);
        return DataResponse.empty();
    }

    private void authorization(Integer postId){
        Member writer = freeService.findPostById(postId).getMember();
        Member signInMember = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        if(!writer.getId().equals(signInMember.getId())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}
