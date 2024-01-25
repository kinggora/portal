package kinggora.portal.domain.type.typehandler;

import kinggora.portal.domain.type.FileType;
import org.apache.ibatis.type.MappedTypes;

/**
 * FileType 타입에 대한 MyBatis Type Handler
 * CodeEnumTypeHandler의 TypeHandler<CodeEnum> 구현을 사용
 * 생성자에서 FileType 클래스 정보를 초기화
 */
@MappedTypes(FileType.class)
public class FileTypeHandler extends CodeEnumTypeHandler<FileType> {
    public FileTypeHandler() {
        super(FileType.class);
    }

}
