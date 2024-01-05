package kinggora.portal.converter;

import kinggora.portal.domain.dto.request.Id;
import org.springframework.core.convert.converter.Converter;

public class IdConverter implements Converter<String, Id> {
    @Override
    public Id convert(String source) {
        return Id.from(source);
    }
}
