package kinggora.portal.domain.typehandler;

import kinggora.portal.domain.type.MemberRole;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(MemberRole.class)
public class MemberRoleTypeHandler implements TypeHandler<MemberRole> {

    /**
     * SQL 파라미터 지정 (DB에 실제로 저장될 값)
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, MemberRole parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    /**
     * 컬럼 이름(columnName) 기반으로 조회한 값을 Enum(MemberRole) 타입으로 변환
     */
    @Override
    public MemberRole getResult(ResultSet rs, String columnName) throws SQLException {
        String roleValue = rs.getString(columnName);
        return getMemberRole(roleValue);
    }

    /**
     * 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 Enum(MemberRole) 타입으로 변환
     */
    @Override
    public MemberRole getResult(ResultSet rs, int columnIndex) throws SQLException {
        String roleValue = rs.getString(columnIndex);
        return getMemberRole(roleValue);
    }

    /**
     * CallableStatement에서 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 Enum(MemberRole) 타입으로 변환
     */
    @Override
    public MemberRole getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String roleValue = cs.getString(columnIndex);
        return getMemberRole(roleValue);
    }

    /**
     * DB에서 조회한 값을 기반으로 Enum 타입 매칭
     *
     * @param roleValue MemberRole에서 value에 해당하는 값
     * @return roleValue을 value로 가지는 MemberRole 타입
     */
    private static MemberRole getMemberRole(String roleValue) {
        switch (roleValue) {
            case "ROLE_USER":
                return MemberRole.USER;
            case "ROLE_ADMIN":
                return MemberRole.ADMIN;
            default:
                return MemberRole.GUEST;
        }
    }
}
