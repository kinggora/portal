package kinggora.portal.domain.type.typehandler;

import kinggora.portal.domain.type.CodeEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;

public class CodeEnumTypeHandler<E extends Enum<E> & CodeEnum> implements TypeHandler<CodeEnum> {

    private Class<E> type;

    public CodeEnumTypeHandler() {
    }

    public CodeEnumTypeHandler(Class<E> type) {
        this.type = type;
    }

    /**
     * SQL 파라미터 지정 (DB에 실제로 저장될 값)
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, CodeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    /**
     * 컬럼 이름(columnName) 기반으로 조회한 값을 CodeEnum 타입으로 변환
     */
    @Override
    public CodeEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return getCodeEnum(rs.getString(columnName));
    }

    /**
     * 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 CodeEnum 타입으로 변환
     */
    @Override
    public CodeEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getCodeEnum(rs.getString(columnIndex));
    }

    /**
     * CallableStatement에서 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 CodeEnum 타입으로 변환
     */
    @Override
    public CodeEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getCodeEnum(cs.getString(columnIndex));
    }

    /**
     * DB에서 조회한 값을 기반으로 CodeEnum 타입 매칭
     *
     * @param code CodeEnum에서 code 해당하는 값
     * @return CodeEnum.getCode == code 인 enum 타입
     */
    private CodeEnum getCodeEnum(String code) {
        return EnumSet.allOf(type)
                .stream()
                .filter(value -> value.getCode().equals(code))
                .findFirst()
                .orElseThrow(TypeException::new);
    }
}
