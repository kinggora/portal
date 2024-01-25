package kinggora.portal.domain.type.typehandler;

import kinggora.portal.domain.type.OrderDirection;
import org.apache.ibatis.type.MappedTypes;

/**
 * OrderDirection 타입에 대한 MyBatis Type Handler
 * CodeEnumTypeHandler의 TypeHandler<CodeEnum> 구현을 사용
 * 생성자에서 OrderDirection 클래스 정보를 초기화
 */
@MappedTypes(OrderDirection.class)
public class OrderDirectionHandler extends CodeEnumTypeHandler<OrderDirection> {
    public OrderDirectionHandler() {
        super(OrderDirection.class);
    }
}
