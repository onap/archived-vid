package org.onap.vid.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class BooleanAsStringSerializer extends JsonSerializer<Boolean> {

    /**
     * Annotate a Jackson getter with <code>@JsonSerialize(using=BooleanAsStringSerializer.class)</code>
     * to get a boolean value be serialized as quoted string.
     */
    @Override
    public void serialize(Boolean bool, JsonGenerator generator, SerializerProvider provider) throws IOException {
        // bool is guaranteed not to be null
        generator.writeString(bool.toString());
    }
}
