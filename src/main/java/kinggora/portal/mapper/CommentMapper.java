package kinggora.portal.mapper;

import kinggora.portal.domain.Comment;
import kinggora.portal.model.response.CommentResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis Mapper Interface
 * CommentMapper.xml에 정의된 SQL과 메서드를 매핑
 */
@Mapper
public interface CommentMapper {

    int save(Comment comment);

    Optional<Comment> findById(int id);

    List<CommentResponse> findByPostId(int postId);

    int update(Comment comment);

    int hideById(int id);

    int deleteById(int id);

    int findRefOrderOfChild(Comment parent);

    int getMaxRef();

    boolean isOnlyMinimumDepth(Comment comment);

    int getMaxRefOrder(int ref);

    int refOrderUp(@Param("ref") int ref, @Param("refOrder") int refOrder);

    boolean hasChild(int id);

    int getChildCount(int id);

}

