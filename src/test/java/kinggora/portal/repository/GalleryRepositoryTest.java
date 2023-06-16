package kinggora.portal.repository;

import kinggora.portal.domain.GalleryPost;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.MemberRole;
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
class GalleryRepositoryTest {

    @Autowired GalleryRepository galleryRepository;
    @Autowired MemberRepository memberRepository;
    Member writer;

    @BeforeEach
    void registerWriter() {
        writer = Member.builder()
                .username(UUID.randomUUID().toString().substring(0, 8))
                .password(UUID.randomUUID().toString().substring(0, 8))
                .name("작성자")
                .role(MemberRole.USER)
                .build();
        memberRepository.saveMember(writer);
    }

    @Test
    void 게시글_저장() {
        GalleryPost post = GalleryPost.builder()
                .member(writer)
                .title("제목123")
                .build();
        int id = galleryRepository.savePost(post);

        GalleryPost findPost = galleryRepository.findPostById(id).get();
        Assertions.assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
        Assertions.assertThat(findPost.getMember().getId()).isEqualTo(writer.getId());
        Assertions.assertThat(findPost.getMember().getPassword()).isNull();
    }

    @Test
    void 조회수_증가() {
        GalleryPost post = GalleryPost.builder()
                .member(writer)
                .title("제목123")
                .build();
        int id = galleryRepository.savePost(post);

        galleryRepository.hitUp(id);
        GalleryPost findPost = galleryRepository.findPostById(post.getId()).get();
        Assertions.assertThat(findPost.getHit()).isEqualTo(1);
    }
}