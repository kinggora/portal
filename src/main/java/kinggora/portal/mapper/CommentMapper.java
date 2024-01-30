package kinggora.portal.mapper;

import kinggora.portal.domain.Comment;
import kinggora.portal.domain.Pageable;
import kinggora.portal.model.data.request.CommentCriteria;
import kinggora.portal.model.data.response.MyComment;
import kinggora.portal.model.data.response.PostComment;
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

    List<PostComment> findComments(@Param("pageable") Pageable pageable, @Param("criteria") CommentCriteria criteria);

    List<MyComment> findMyComments(@Param("pageable") Pageable pageable, @Param("criteria") CommentCriteria criteria);

    List<PostComment> findPostComments(@Param("pageable") Pageable pageable, @Param("criteria") CommentCriteria criteria);

    int findCommentsCount(CommentCriteria criteria);

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

