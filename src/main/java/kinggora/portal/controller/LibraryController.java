package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.LibraryPost;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.MemberRole;
import kinggora.portal.domain.dto.PostDto;
import kinggora.portal.exception.BizException;
import kinggora.portal.service.LibraryService;
import kinggora.portal.service.MemberService;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/lib")
public class LibraryController {

    private final LibraryService libraryService;
    private final MemberService memberService;

    @GetMapping("/post/{id}")
    public DataResponse<LibraryPost> getPost(@PathVariable int id) {
        libraryService.hitUp(id);
        LibraryPost post = libraryService.findPostById(id);
        return DataResponse.of(post);
    }

    @PostMapping("/posts")
    public DataResponse<Integer> createPost(@RequestBody PostDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        authorizationAdmin(member);
        dto.setMemberId(member.getId());
        Integer id = libraryService.savePost(dto.toLibraryPost());
        return DataResponse.of(id);
    }

    @PutMapping("/post")
    public DataResponse<Void> updatePost(@RequestBody PostDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        authorizationAdmin(member);
        libraryService.updatePost(dto.toLibraryPost());
        return DataResponse.empty();
    }

    @DeleteMapping("/post/{id}")
    public DataResponse<Void> deletePost(@PathVariable int id) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        authorizationAdmin(member);
        libraryService.deletePost(id);
        return DataResponse.empty();
    }

    /**
     * 회원의 권한이 관리자인지 확인
     * 관리자가 아니면 UNAUTHORIZED ACCESS 예외 발생
     * @param member 회원 정보
     */
    private void authorizationAdmin(Member member) {
        if(!member.getRole().equals(MemberRole.ADMIN)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}
