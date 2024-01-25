package kinggora.portal.domain.type.typehandler;

import kinggora.portal.domain.type.AccessLevel;
import org.apache.ibatis.type.MappedTypes;

/**
 * AccessLevel 타입에 대한 MyBatis Type Handler
 * CodeEnumTypeHandler의 TypeHandler<CodeEnum> 구현을 사용
 * 생성자에서 AccessLevel 클래스 정보를 초기화
 */
@MappedTypes(AccessLevel.class)
public class AccessLevelHandler extends CodeEnumTypeHandler<AccessLevel> {
    public AccessLevelHandler() {
        super(AccessLevel.class);
    }
}
