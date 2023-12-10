package kinggora.portal.domain.type.typehandler;

import kinggora.portal.domain.type.FileType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(FileType.class)
public class FileTypeHandler extends CodeEnumTypeHandler<FileType> {
    public FileTypeHandler() {
        super(FileType.class);
    }

    //    /**
//     * SQL 파라미터 지정 (DB에 실제로 저장될 값)
//     */
//    @Override
//    public void setParameter(PreparedStatement ps, int i, FileType parameter, JdbcType jdbcType) throws SQLException {
//        ps.setString(i, parameter.getCode());
//    }
//
//    /**
//     * 컬럼 이름(columnName) 기반으로 조회한 값을 Enum(FileType) 타입으로 변환
//     */
//    @Override
//    public FileType getResult(ResultSet rs, String columnName) throws SQLException {
//        return getFileType(rs.getString(columnName));
//    }
//
//    /**
//     * 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 Enum(FileType) 타입으로 변환
//     */
//    @Override
//    public FileType getResult(ResultSet rs, int columnIndex) throws SQLException {
//        return getFileType(rs.getString(columnIndex));
//    }
//
//    /**
//     * CallableStatement에서 컬럼 인덱스(columnIndex) 기반으로 조회한 값을 Enum(FileType) 타입으로 변환
//     */
//    @Override
//    public FileType getResult(CallableStatement cs, int columnIndex) throws SQLException {
//        return getFileType(cs.getString(columnIndex));
//    }
//
//    /**
//     * DB에서 조회한 값을 기반으로 Enum 타입 매칭
//     *
//     * @param typeValue FileType에서 value에 해당하는 값
//     * @return typeValue을 value로 가지는 FileType 타입
//     */
//
//    private FileType getFileType(String typeValue) {
//        switch (typeValue) {
//            case "A":
//                return FileType.ATTACHMENT;
//            case "C":
//                return FileType.CONTENT;
//            case "T":
//                return FileType.THUMBNAIL;
//        }
//        return null;
//    }
}
