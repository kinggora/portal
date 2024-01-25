package kinggora.portal.domain.type.typehandler;

import kinggora.portal.domain.type.MemberRole;

/**
 * List<MemberRole> 타입에 대한 MyBatis Type Handler
 * CodeEnumListTypeHandler의 TypeHandler<List<CodeEnum>> 구현을 사용
 * 생성자에서 MemberRole 클래스 정보를 초기화
 */
public class MemberRolesTypeHandler extends CodeEnumListTypeHandler<MemberRole> {
    public MemberRolesTypeHandler() {
        super(MemberRole.class);
    }
}
