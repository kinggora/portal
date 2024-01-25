package kinggora.portal.domain;

import kinggora.portal.domain.type.OrderDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Getter
public class Pageable {
    private final int limit;
    private final int offset;
    private final Sort sort;

    public Pageable(int page, int size) {
        this.limit = size;
        this.offset = (page - 1) * size;
        this.sort = new Sort();
    }

    public Pageable(int page, int size, List<Order> orders) {
        this.limit = size;
        this.offset = (page - 1) * size;
        this.sort = new Sort(orders);
    }

    @Getter
    public static class Sort {

        private final boolean sorted;

        private List<Order> orders;

        public Sort() {
            this.sorted = false;
        }

        public Sort(List<Order> orderList) {
            if (ObjectUtils.isEmpty(orderList)) {
                this.sorted = false;
            } else {
                this.sorted = true;
                this.orders = orderList;
            }
        }

    }

    @Getter
    @AllArgsConstructor
    public static class Order {
        private String property;
        private OrderDirection direction;
    }

}
