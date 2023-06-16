package kinggora.portal.repository;

import kinggora.portal.domain.Category;
import kinggora.portal.domain.LibraryPost;
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
class LibraryRepositoryTest {

    @Autowired
    LibraryRepository libraryRepository;
    @Autowired MemberRepository memberRepository;
    Member writer;

    @BeforeEach
    void registerWriter() {
        writer = Member.builder()
                .username(UUID.randomUUID().toString().substring(0, 8))
                .password(UUID.randomUUID().toString().substring(0, 8))
                .name("관리자")
                .role(MemberRole.ADMIN)
                .build();
        memberRepository.saveMember(writer);
    }

    @Test
    void 게시글_저장() {
        LibraryPost post = LibraryPost.builder()
                .boardId(1)
                .member(writer)
                .title("공지")
                .content("공지입니다")
                .build();
        int id = libraryRepository.savePost(post);

        LibraryPost findPost = libraryRepository.findPostById(id).get();
        Assertions.assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
        Assertions.assertThat(findPost.getBoardId()).isEqualTo(post.getBoardId());
        Assertions.assertThat(findPost.getMember().getId()).isEqualTo(writer.getId());
        Assertions.assertThat(findPost.getMember().getRole()).isEqualTo(writer.getRole());
        Assertions.assertThat(findPost.getMember().getPassword()).isNull();
    }

    @Test
    void 조회수_증가() {
        LibraryPost post = LibraryPost.builder()
                .boardId(2)
                .member(writer)
                .title("뉴스")
                .content("뉴스입니다")
                .build();
        int id = libraryRepository.savePost(post);

        libraryRepository.hitUp(id);
        LibraryPost findPost = libraryRepository.findPostById(post.getId()).get();
        Assertions.assertThat(findPost.getHit()).isEqualTo(1);
    }

}