package kinggora.portal.domain.typehandler;

import kinggora.portal.domain.type.AccessLevel;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(AccessLevel.class)
public class AccessLevelHandler implements TypeHandler<AccessLevel> {
    /**
     * SQL 파라미터 지정 (DB에 실제로 저장될 값)
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, AccessLevel parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    /**
     * 컬럼 이름(columnName) 기반으로 조회한 값을 Enum(AccessLevel) 타입으로 변환
     */
    @Override
    public AccessLevel getResult(ResultSet rs, String columnName) throws SQLException {
        String levelValue = rs.getString(columnName);
        return getAccessLevel(levelValue);
    }

    /**
     * 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 Enum(AccessLevel) 타입으로 변환
     */
    @Override
    public AccessLevel getResult(ResultSet rs, int columnIndex) throws SQLException {
        String levelValue = rs.getString(columnIndex);
        return getAccessLevel(levelValue);
    }

    /**
     * CallableStatement에서 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 Enum(AccessLevel) 타입으로 변환
     */
    @Override
    public AccessLevel getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String typeValue = cs.getString(columnIndex);
        return getAccessLevel(typeValue);
    }

    /**
     * DB에서 조회한 값을 기반으로 Enum 타입 매칭
     *
     * @param levelValue AccessLevel에서 value에 해당하는 값
     * @return levelValue을 value로 가지는 AccessLevel 타입
     */

    private static AccessLevel getAccessLevel(String levelValue) {
        switch (levelValue) {
            case "ALL":
                return AccessLevel.ALL;
            case "USER":
                return AccessLevel.USER;
            case "ADMIN":
                return AccessLevel.ADMIN;
            case "NONE":
                return AccessLevel.NONE;
        }
        return null;
    }
}
