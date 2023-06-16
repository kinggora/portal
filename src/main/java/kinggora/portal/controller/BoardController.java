package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Category;
import kinggora.portal.service.BoardInfoService;
import kinggora.portal.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardInfoService boardInfoService;
    private final CategoryService categoryService;

    @GetMapping(value = "/index")
    public DataResponse<List<BoardInfo>> index() {
        List<BoardInfo> boardInfo = boardInfoService.findBoardInfo();
        return DataResponse.of(boardInfo);
    }

    @GetMapping(value = "/category")
    public DataResponse<List<Category>> category(Integer boardId) {
        List<Category> categories = categoryService.findCategories(boardId);
        return DataResponse.of(categories);
    }
}
