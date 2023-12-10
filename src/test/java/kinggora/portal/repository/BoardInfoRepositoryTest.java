package kinggora.portal.repository;

import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.type.AccessLevel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BoardInfoRepositoryTest {

    @Autowired
    BoardInfoRepository repository;

    @Test
    void findById() {
        BoardInfo boardInfo = repository.findBoardInfoById(1).get();
        Assertions.assertThat(boardInfo.getName()).isEqualTo("notice");
        Assertions.assertThat(boardInfo.getSubject()).isEqualTo("공지");
        Assertions.assertThat(boardInfo.getAccessList()).isEqualTo(AccessLevel.ALL);
        Assertions.assertThat(boardInfo.getAccessRead()).isEqualTo(AccessLevel.ALL);
        Assertions.assertThat(boardInfo.getAccessWrite()).isEqualTo(AccessLevel.ADMIN);
    }
}
