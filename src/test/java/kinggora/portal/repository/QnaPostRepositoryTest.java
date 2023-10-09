package kinggora.portal.repository;

import kinggora.portal.domain.Category;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.QnaPost;
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
class QnaPostRepositoryTest {

    @Autowired
    QnaPostRepository qnaPostRepository;
    @Autowired
    MemberRepository memberRepository;
    Member user;
    Member admin;

    @BeforeEach
    void registerWriter() {
        user = Member.builder()
                .username(UUID.randomUUID().toString().substring(0, 8))
                .password(UUID.randomUUID().toString().substring(0, 8))
                .name(UUID.randomUUID().toString().substring(0, 5))
                .role(MemberRole.USER)
                .build();
        memberRepository.saveMember(user);
        admin = Member.builder()
                .username(UUID.randomUUID().toString().substring(0, 8))
                .password(UUID.randomUUID().toString().substring(0, 8))
                .name(UUID.randomUUID().toString().substring(0, 5))
                .role(MemberRole.ADMIN)
                .build();
        memberRepository.saveMember(admin);
    }

    @Test
    void 질문_등록() {
        Category category = new Category(1);
        QnaPost question = QnaPost.builder()
                .member(user)
                .category(category)
                .title("test title")
                .content("test content")
                .build();
        int questionId = qnaPostRepository.saveQuestion(question);
        QnaPost answer = QnaPost.builder()
                .member(admin)
                .category(category)
                .parent(questionId)
                .title("test title")
                .content("test content")
                .build();
        int answerId = qnaPostRepository.saveAnswer(answer);

        QnaPost findQuestion = qnaPostRepository.findPostById(questionId).get();
        QnaPost findAnswer = qnaPostRepository.findPostById(answerId).get();

        Assertions.assertThat(findQuestion.getMember().getRole()).isEqualTo(MemberRole.USER);
        Assertions.assertThat(findQuestion.getCategory().getId()).isEqualTo(question.getCategory().getId());
        Assertions.assertThat(findQuestion.getTitle()).isEqualTo(question.getTitle());
        Assertions.assertThat(findQuestion.getContent()).isEqualTo(question.getContent());
        Assertions.assertThat(findQuestion.getParent()).isNull();

        Assertions.assertThat(findAnswer.getMember().getRole()).isEqualTo(MemberRole.ADMIN);
        Assertions.assertThat(findAnswer.getCategory().getId()).isEqualTo(answer.getCategory().getId());
        Assertions.assertThat(findAnswer.getTitle()).isEqualTo(answer.getTitle());
        Assertions.assertThat(findAnswer.getContent()).isEqualTo(answer.getContent());
        Assertions.assertThat(findAnswer.getParent()).isEqualTo(findQuestion.getId());

    }


}
