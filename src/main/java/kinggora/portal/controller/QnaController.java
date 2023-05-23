package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.Category;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.QnaPost;
import kinggora.portal.domain.dto.MemberRole;
import kinggora.portal.domain.dto.PostDto;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.exception.BizException;
import kinggora.portal.service.MemberService;
import kinggora.portal.service.QnaService;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {

    private final QnaService qnaService;
    private final MemberService memberService;

    @GetMapping(value = "/category")
    public DataResponse<List<Category>> category() {
        List<Category> categories = qnaService.findCategories();
        return DataResponse.of(categories);
    }

    @GetMapping("/posts/{id}")
    public DataResponse<QnaPost> getPost(@PathVariable int id) {
        Member signInMember = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        if(signInMember.getRole().equals(MemberRole.ADMIN)) {
            qnaService.hitUp(id);
            return DataResponse.of(qnaService.findPostById(id));
        }
        QnaPost post = qnaService.findPostById(id);
        Member writer = post.getMember();
        if(!writer.getId().equals(signInMember.getId())) {
            if(post.getParent() != null) {
                QnaPost question = qnaService.findPostById(post.getParent());
                if(!question.getMember().getId().equals(signInMember.getId())) {
                    throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS, "QnA 답변은 질문 작성자만 열람 가능합니다.");
                }
            } else {
                throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS, "질문 작성자만 열람 가능합니다.");
            }
        }
        qnaService.hitUp(id);
        return DataResponse.of(qnaService.findPostById(id));
    }

    @GetMapping("/posts")
    public DataResponse<List<QnaPost>> getPosts(SearchCriteria criteria) {
        List<QnaPost> posts = qnaService.findPosts(criteria);
        return DataResponse.of(posts);
    }

    @PostMapping("/posts/q")
    public DataResponse<Integer> createQuestion(@RequestBody PostDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        dto.setMemberId(member.getId());
        Integer id = qnaService.saveQuestion(dto.toQnaPost());
        return DataResponse.of(id);
    }

    @PostMapping("/posts/a")
    public DataResponse<Integer> createAnswer(@RequestBody PostDto dto) {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        if(!member.getRole().equals(MemberRole.ADMIN)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS, "QnA 답변은 관리자만 작성 가능합니다.");
        }
        dto.setMemberId(member.getId());
        Integer id = qnaService.saveAnswer(dto.toQnaPost());
        return DataResponse.of(id);
    }

    @PutMapping("/posts")
    public DataResponse<Void> updatePost(@RequestBody PostDto dto) {
        authorizationWriter(dto.getId());
        qnaService.updateQuestion(dto.toQnaPost());
        return DataResponse.empty();
    }

    @DeleteMapping("/posts/{id}")
    public DataResponse<Void> deletePost(@PathVariable int id) {
        authorizationWriter(id);
        qnaService.deleteQuestion(id);
        return DataResponse.empty();
    }

    private void authorizationWriter(Integer postId) {
        Member writer = qnaService.findPostById(postId).getMember();
        Member signInMember = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        if (!writer.getId().equals(signInMember.getId())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}
