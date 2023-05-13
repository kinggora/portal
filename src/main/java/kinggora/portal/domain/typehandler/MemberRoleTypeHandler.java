package kinggora.portal.domain.typehandler;

import kinggora.portal.domain.dto.MemberRole;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(MemberRole.class)
public class MemberRoleTypeHandler implements TypeHandler<MemberRole> {
    @Override
    public void setParameter(PreparedStatement ps, int i, MemberRole parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public MemberRole getResult(ResultSet rs, String columnName) throws SQLException {
        String roleValue = rs.getString(columnName);
        return getMemberRole(roleValue);
    }

    @Override
    public MemberRole getResult(ResultSet rs, int columnIndex) throws SQLException {
        String roleValue = rs.getString(columnIndex);
        return getMemberRole(roleValue);
    }

    @Override
    public MemberRole getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String roleValue = cs.getString(columnIndex);
        return getMemberRole(roleValue);
    }


    private static MemberRole getMemberRole(String roleValue) {
        switch(roleValue) {
            case "ROLE_USER":
                return MemberRole.USER;
            case "ROLE_ADMIN":
                return MemberRole.ADMIN;
            default:
                return MemberRole.GUEST;
        }
    }
}
