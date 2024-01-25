package kinggora.portal.domain;

import kinggora.portal.annotation.ByteSize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ByteSizeValidationTest {

    CustomValidator<TestDto> validator;
    final static String titleErrorMessage = "Title Error Message";
    final static String contentErrorMessage = "Content Error Message";
    final static int titleMin = 1;
    final static int titleMax = 30;
    final static int contentMin = 3;
    final static int contentMax = 50;

    @BeforeEach
    void init() {
        validator = new CustomValidator<>();
    }

    @Test
    @DisplayName("Null")
    void validateNull() {
        String title = null;
        String content = null;
        TestDto testDto = new TestDto(title, content);
        List<String> messages = validator.getErrorMessage(testDto);
        Assertions.assertThat(messages).containsOnly(
                titleErrorMessage
                , contentErrorMessage
        );
    }

    @Test
    @DisplayName("Valid Min")
    void validMin() {
        String title = "a"; // byte size: 1
        String content = "ㄱ"; // byte size: 3
        TestDto testDto = new TestDto(title, content);
        List<String> messages = validator.getErrorMessage(testDto);
        Assertions.assertThat(messages).containsOnly();
    }

    @Test
    @DisplayName("Invalid Min")
    void invalidMin() {
        String title = ""; // byte size: 0
        String content = "ab"; // byte size: 2
        TestDto testDto = new TestDto(title, content);
        List<String> messages = validator.getErrorMessage(testDto);
        Assertions.assertThat(messages).containsOnly(
                titleErrorMessage
                , contentErrorMessage
        );
    }

    @Test
    @DisplayName("Valid Max")
    void validMax() {
        String korean = "가나다라마바ㅅㅇㅈㅊ"; // 3 * 10 = 30 byte
        String english = "abcdefghij"; // 1 * 10 = 10 byte
        String blank = "          "; // 1 * 10 = 10 byte
        String special = "!@#$%^&*()"; // 1 * 10 = 10 byte
        String emoji = "😀😃😄😁😆😅🤣😂🙂🙃"; // 4 * 10 = 40 byte

        Assertions.assertThat(korean.length()).isEqualTo(10);
        Assertions.assertThat(english.length()).isEqualTo(10);
        Assertions.assertThat(blank.length()).isEqualTo(10);
        Assertions.assertThat(special.length()).isEqualTo(10);

        Assertions.assertThat(korean.getBytes(StandardCharsets.UTF_8).length).isEqualTo(korean.length() * 3);
        Assertions.assertThat(english.getBytes(StandardCharsets.UTF_8).length).isEqualTo(english.length());
        Assertions.assertThat(blank.getBytes(StandardCharsets.UTF_8).length).isEqualTo(blank.length());
        Assertions.assertThat(special.getBytes(StandardCharsets.UTF_8).length).isEqualTo(special.length());
        Assertions.assertThat(emoji.getBytes(StandardCharsets.UTF_8).length).isEqualTo(4 * 10);

        String title = korean;
        String content = emoji + special;
        TestDto testDto = new TestDto(title, content);
        List<String> messages = validator.getErrorMessage(testDto);
        Assertions.assertThat(messages).containsOnly();
    }

    @Test
    @DisplayName("InValid Max")
    void invalidMax() {
        String korean = "가나다라마바ㅅㅇㅈㅊ"; // 3 * 10 = 30 byte
        String special = "!@#$%^&*()"; // 1 * 10 = 10 byte
        String emoji = "😀😃😄😁😆😅🤣😂🙂🙃"; // 4 * 10 = 40 byte

        String title = korean + " ";
        String content = emoji + special + " ";
        TestDto testDto = new TestDto(title, content);
        List<String> messages = validator.getErrorMessage(testDto);
        Assertions.assertThat(messages).containsOnly(
                titleErrorMessage
                , contentErrorMessage
        );
    }


    @Getter
    @AllArgsConstructor
    public static class TestDto {
        @ByteSize(min = titleMin, max = titleMax, message = titleErrorMessage)
        private String title;

        @ByteSize(min = contentMin, max = contentMax, message = contentErrorMessage)
        private String content;
    }
}
