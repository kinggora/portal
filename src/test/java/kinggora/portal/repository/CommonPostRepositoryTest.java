package kinggora.portal.repository;

import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Category;
import kinggora.portal.domain.CommonPost;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.type.MemberRole;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@SpringBootTest
@Transactional
class CommonPostRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CommonPostRepository commonPostRepository;
    Member writer;

    @BeforeEach
    void registerWriter() {
        writer = Member.builder()
                .username(UUID.randomUUID().toString().substring(0, 8))
                .password(UUID.randomUUID().toString().substring(0, 8))
                .name(UUID.randomUUID().toString().substring(0, 5))
                .role(MemberRole.USER)
                .build();
        memberRepository.saveMember(writer);
    }

    @Test
    void 게시글_저장() {
        CommonPost post = CommonPost.builder()
                .member(writer)
                .boardInfo(new BoardInfo(4))
                .category(new Category(1))
                .title("test title")
                .content("test content")
                .build();
        int id = commonPostRepository.savePost(post);

        CommonPost findPost = commonPostRepository.findPostById(id).get();
        Assertions.assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
        Assertions.assertThat(findPost.getContent()).isEqualTo(post.getContent());
        Assertions.assertThat(findPost.getMember().getId()).isEqualTo(writer.getId());
        Assertions.assertThat(findPost.getMember().getPassword()).isNull();
        Assertions.assertThat(findPost.getBoardInfo().getName()).isEqualTo("free");
        Assertions.assertThat(findPost.getCategory().getName()).isEqualTo("JAVA");
    }

    @Test
    void 조회수_증가() {
        CommonPost post = CommonPost.builder()
                .member(writer)
                .boardInfo(new BoardInfo(4))
                .category(new Category(1))
                .title("test title")
                .content("test content")
                .build();
        int id = commonPostRepository.savePost(post);

        commonPostRepository.hitUp(id);
        CommonPost findPost = commonPostRepository.findPostById(post.getId()).get();
        Assertions.assertThat(findPost.getHit()).isEqualTo(1);
    }
}
