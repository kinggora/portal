package kinggora.portal.domain.type.typehandler;

import kinggora.portal.domain.type.CodeEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CodeEnum의 List 타입에 대한 MyBatis Type Handler
 * List<CodeEnum> 타입을 DB에 저장할 때 컬럼 값을 설정하고, 조회할 때 List<CodeEnum> 타입 매핑
 * CodeEnum의 하위 타입에 대한 공통 처리를 위해 Enum Class를 type 필드로 포함
 *
 * @param <E> CodeEnum을 상속한 Enum Class
 */
public class CodeEnumListTypeHandler<E extends Enum<E> & CodeEnum> implements TypeHandler<List<CodeEnum>> {

    private Class<E> type;

    public CodeEnumListTypeHandler() {
    }

    public CodeEnumListTypeHandler(Class<E> type) {
        this.type = type;
    }

    /**
     * List<CodeEnum>에 대한 SQL 파라미터 지정 (DB에 실제로 저장될 값)
     * 리스트 요소의 code 값을 하나의 문자열로 join하여 저장
     *
     * @param ps        Precompiled SQL statement.
     * @param i         지정할 파라미터의 인덱스
     * @param parameter 저장할 Enum 리스트
     * @param jdbcType  DB의 컬럼 타입
     * @throws SQLException
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, List<CodeEnum> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            List<String> codes = parameter.stream()
                    .map(CodeEnum::getCode)
                    .collect(Collectors.toList());
            ps.setString(i, String.join(",", codes));
        } else {
            ps.setString(i, null);
        }
    }

    /**
     * 컬럼 이름(columnName) 기반으로 조회한 값을 List<CodeEnum>타입으로 변환
     * 하나의 문자열을 code 단위로 분해하여 CodeEnum으로 변환
     *
     * @param rs         데이터베이스 결과 집합
     * @param columnName 조회할 컬럼의 이름
     * @return 변환한 List<CodeEnum>
     * @throws SQLException
     */
    @Override
    public List<CodeEnum> getResult(ResultSet rs, String columnName) throws SQLException {
        String codes = rs.getString(columnName);
        return Arrays.stream(codes.split(","))
                .map(this::getCodeEnum)
                .collect(Collectors.toList());
    }

    /**
     * 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 List<CodeEnum> 타입으로 변환
     * 하나의 문자열을 code 단위로 분해하여 CodeEnum으로 변환
     *
     * @param rs          데이터베이스 결과 집합
     * @param columnIndex 조회할 컬럼의 인덱스
     * @return 변환한 List<CodeEnum>
     * @throws SQLException
     */
    @Override
    public List<CodeEnum> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String codes = rs.getString(columnIndex);
        return Arrays.stream(codes.split(","))
                .map(this::getCodeEnum)
                .collect(Collectors.toList());
    }

    /**
     * CallableStatement에서 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 List<CodeEnum> 타입으로 변환
     *
     * @param cs          Interface used to execute SQL stored procedures
     * @param columnIndex 조회할 컬럼의 인덱스
     * @return 변환한 List<CodeEnum>
     * @throws SQLException
     */
    @Override
    public List<CodeEnum> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String codes = cs.getString(columnIndex);
        return Arrays.stream(codes.split(","))
                .map(this::getCodeEnum)
                .collect(Collectors.toList());
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
