package kinggora.portal.domain.typehandler;

import kinggora.portal.domain.type.FileType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(FileType.class)
public class FileTypeHandler implements TypeHandler<FileType> {

    /**
     * SQL 파라미터 지정 (DB에 실제로 저장될 값)
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, FileType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    /**
     * 컬럼 이름(columnName) 기반으로 조회한 값을 Enum(FileType) 타입으로 변환
     */
    @Override
    public FileType getResult(ResultSet rs, String columnName) throws SQLException {
        String typeValue = rs.getString(columnName);
        return getFileType(typeValue);
    }

    /**
     * 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 Enum(FileType) 타입으로 변환
     */
    @Override
    public FileType getResult(ResultSet rs, int columnIndex) throws SQLException {
        String typeValue = rs.getString(columnIndex);
        return getFileType(typeValue);
    }

    /**
     * CallableStatement에서 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 Enum(FileType) 타입으로 변환
     */
    @Override
    public FileType getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String typeValue = cs.getString(columnIndex);
        return getFileType(typeValue);
    }

    /**
     * DB에서 조회한 값을 기반으로 Enum 타입 매칭
     *
     * @param typeValue FileType에서 value에 해당하는 값
     * @return typeValue을 value로 가지는 FileType 타입
     */

    private static FileType getFileType(String typeValue) {
        switch (typeValue) {
            case "A":
                return FileType.ATTACHMENT;
            case "C":
                return FileType.CONTENT;
            case "T":
                return FileType.THUMBNAIL;
            default:
                return FileType.UNKNOWN;
        }
    }
}
