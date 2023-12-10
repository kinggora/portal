package kinggora.portal.mapper;

import kinggora.portal.domain.CommonPost;
import kinggora.portal.domain.Post;
import kinggora.portal.domain.QnaPost;
import kinggora.portal.domain.dto.request.SearchCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardsMapper {

    /**
     * 게시글 저장
     *
     * @param post 게시글 정보
     * @return 게시글 id
     */
    int savePost(Post post);

    /**
     * 게시글 단건 조회
     *
     * @param id 게시글 id
     * @return 게시글 정보
     */
    Optional<Post> findPostById(int id);

    /**
     * 파일 id로 게시글 단건 조회
     *
     * @param fileId 파일 id
     * @return 게시글 정보
     */
    Optional<Post> findPostByFileId(int fileId);

    /**
     * 게시글 조회수 1 증가
     *
     * @param id 게시글 id
     */
    int hitUp(Integer id);

    /**
     * 게시글 수정
     *
     * @param post 수정할 데이터
     * @return 수정된 게시글 개수
     */
    int updatePost(Post post);

    /**
     * 게시글 삭제
     *
     * @param id 게시글 id
     * @return 삭제된 게시글 개수
     */
    int deletePostById(int id);

    /**
     * 검색 조건에 해당하는 게시글 조회 + 페이징 처리
     *
     * @param criteria 검색 조건
     * @param limit    페이징
     * @param offset   페이징
     * @return 게시글 리스트
     */
    List<CommonPost> findCommonPosts(@Param("criteria") SearchCriteria criteria, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 검색 조건에 해당하는 질문 게시글 조회 + 페이징 처리
     * 질문: parent=null
     *
     * @param criteria 검색 조건
     * @param limit    페이징
     * @param offset   페이징
     * @return 게시글 리스트
     */
    List<QnaPost> findQnaPosts(@Param("criteria") SearchCriteria criteria, @Param("offset") int offset, @Param("limit") int limit);

    List<Post> findChildPosts(int id);

    /**
     * 검색 조건에 해당하는 게시글 개수 조회
     *
     * @param sc 검색 조건
     * @return 게시글 개수
     */
    int findPostsCount(SearchCriteria sc);

    /**
     * parent=id 인 게시글(child) 존재 여부
     *
     * @param id 게시글 id
     * @return true: 존재, false: 존재X
     */
    boolean childExists(int id);

    /**
     * 게시글 존재 여부
     *
     * @param id 게시글 id
     * @return true: 존재, false: 존재X
     */
    boolean existById(int id);
}
