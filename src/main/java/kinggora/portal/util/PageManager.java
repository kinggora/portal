package kinggora.portal.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PageManager {
    public static int PAGE_SIZE = 10;
    private int totalCount;
    public int getTotalPage(){
        return totalCount / PAGE_SIZE + 1;
    }
}
