package kinggora.portal.repository;

import kinggora.portal.domain.Category;
import kinggora.portal.domain.FreePost;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.PostDto;
import kinggora.portal.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class FreeRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired FreeRepository freeRepository;
    Member writer;

    @BeforeEach
    void registerWriter() {
        writer = Member.builder()
                .username(UUID.randomUUID().toString().substring(0, 8))
                .password(UUID.randomUUID().toString().substring(0, 8))
                .name("작성자")
                .build();
        memberRepository.saveMember(writer);
    }

    @Test
    void 게시글_저장() {
        FreePost post = FreePost.builder()
                .member(writer)
                .category(new Category(1))
                .title("제목123")
                .content("내용123")
                .build();
        int id = freeRepository.savePost(post);

        FreePost findPost = freeRepository.findPostById(id).get();
        Assertions.assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
        Assertions.assertThat(findPost.getContent()).isEqualTo(post.getContent());
        Assertions.assertThat(findPost.getMember().getId()).isEqualTo(writer.getId());
        Assertions.assertThat(findPost.getMember().getPassword()).isNull();
        Assertions.assertThat(findPost.getCategory().getName()).isEqualTo("JAVA");
    }

    @Test
    void 조회수_증가() {
        FreePost post = FreePost.builder()
                .member(writer)
                .category(new Category(1))
                .title("제목123")
                .content("내용123")
                .build();
        int id = freeRepository.savePost(post);

        freeRepository.hitUp(id);
        FreePost findPost = freeRepository.findPostById(post.getId()).get();
        Assertions.assertThat(findPost.getHit()).isEqualTo(1);
    }
}