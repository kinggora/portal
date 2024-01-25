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

/**
 * CodeEnum 타입에 대한 MyBatis Type Handler
 * CodeEnum 타입을 DB에 저장할 때 컬럼 값을 설정하고, 조회할 때 CodeEnum 타입 매핑
 * CodeEnum의 하위 타입에 대한 공통 처리를 위해 Enum Class를 type 필드로 포함
 *
 * @param <E> CodeEnum을 상속한 Enum Class
 */
public class CodeEnumTypeHandler<E extends Enum<E> & CodeEnum> implements TypeHandler<CodeEnum> {

    private Class<E> type;

    public CodeEnumTypeHandler() {
    }

    public CodeEnumTypeHandler(Class<E> type) {
        this.type = type;
    }

    /**
     * CodeEnum에 대한 SQL 파라미터 지정 (DB에 실제로 저장될 값)
     *
     * @param ps        Precompiled SQL statement.
     * @param i         지정할 파라미터의 인덱스
     * @param parameter 저장할 Enum
     * @param jdbcType  DB의 컬럼 타입
     * @throws SQLException
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, CodeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    /**
     * 컬럼 이름(columnName) 기반으로 조회한 값을 CodeEnum 타입으로 변환
     *
     * @param rs         데이터베이스 결과 집합
     * @param columnName 조회할 컬럼의 이름
     * @return 변환한 CodeEnum
     * @throws SQLException
     */
    @Override
    public CodeEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return getCodeEnum(rs.getString(columnName));
    }

    /**
     * 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 CodeEnum 타입으로 변환
     *
     * @param rs          데이터베이스 결과 집합
     * @param columnIndex 조회할 컬럼의 인덱스
     * @return 변환한 CodeEnum
     * @throws SQLException
     */
    @Override
    public CodeEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getCodeEnum(rs.getString(columnIndex));
    }

    /**
     * CallableStatement에서 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 CodeEnum 타입으로 변환
     *
     * @param cs          Interface used to execute SQL stored procedures
     * @param columnIndex 조회할 컬럼의 인덱스
     * @return 변환한 CodeEnum
     * @throws SQLException
     */
    @Override
    public CodeEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getCodeEnum(cs.getString(columnIndex));
    }

    /**
     * DB에서 조회한 값을 code로 하는 CodeEnum 매핑
     * type의 모든 element를 포함하는 EnumSet에서 value를 code로 가지는 CodeEnum 반환
     *
     * @param value DB에서 조회한 문자열
     * @return value를 code로 가지는 CodeEnum
     * @throws TypeException type 내 요소 중 value를 code로 가지는 CodeEnum가 없는 경우
     */
    private CodeEnum getCodeEnum(String value) {
        return EnumSet.allOf(type)
                .stream()
                .filter(codeEnum -> codeEnum.getCode().equals(value))
                .findFirst()
                .orElseThrow(TypeException::new);
    }
}
