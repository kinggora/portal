package kinggora.portal.service;

import kinggora.portal.domain.Category;
import kinggora.portal.domain.FreePost;
import kinggora.portal.domain.dto.PostDto;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.repository.CategoryRepository;
import kinggora.portal.repository.FreeRepository;
import kinggora.portal.util.PageManager;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeService {

    private final static int BOARD_ID = 4;
    private final FreeRepository freeRepository;
    private final CategoryRepository categoryRepository;

    public int savePost(PostDto post) {

        return freeRepository.savePost(post);
    }

    public void updatePost(PostDto post) {
        freeRepository.updatePost(post);
    }

    public void deletePost(Integer id) {
        freeRepository.deletePost(id);
    }

    public FreePost findPostById(Integer id) {
        return freeRepository.findPostById(id)
                .orElseThrow(() -> new RuntimeException("fail FreeService.findPostById"));
    }

    public List<FreePost> findPosts(SearchCriteria criteria) {
        int startRow = (criteria.getPage() - 1) * PageManager.PAGE_SIZE;
        return freeRepository.findPosts(criteria, startRow, PageManager.PAGE_SIZE);
    }

    public void hitUp(Integer id) {
        freeRepository.hitUp(id);
    }

    public List<Category> findCategories() {
        return categoryRepository.findCategories(BOARD_ID);
    }
}
