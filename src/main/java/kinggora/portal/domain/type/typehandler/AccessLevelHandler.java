package kinggora.portal.domain.type.typehandler;

import kinggora.portal.domain.type.AccessLevel;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(AccessLevel.class)
public class AccessLevelHandler extends CodeEnumTypeHandler<AccessLevel> {
    public AccessLevelHandler() {
        super(AccessLevel.class);
    }
}
