package org.onap.vid.aai.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Test;

public class CustomJacksonJaxBJsonProviderTest {

    private CustomJacksonJaxBJsonProvider createTestSubject() {
        return new CustomJacksonJaxBJsonProvider();
    }

    @Test
    public void testMapperHasCorrectConfig() throws Exception {
        CustomJacksonJaxBJsonProvider testSubject = createTestSubject();
        DeserializationConfig deserializationConfig = testSubject.getMapper().getDeserializationConfig();
        SerializationConfig serializationConfig = testSubject.getMapper().getSerializationConfig();

        Assert.assertFalse(serializationConfig.hasSerializationFeatures(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS.getMask()));
        Assert.assertFalse(serializationConfig.hasSerializationFeatures(SerializationFeature.INDENT_OUTPUT.getMask()));
        Assert.assertFalse(serializationConfig.hasSerializationFeatures(SerializationFeature.WRAP_ROOT_VALUE.getMask()));
        Assert.assertFalse(serializationConfig.hasSerializationFeatures(SerializationFeature.CLOSE_CLOSEABLE.getMask()));

        Assert.assertFalse(deserializationConfig.hasDeserializationFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES.getMask()));
        Assert.assertFalse(deserializationConfig.hasDeserializationFeatures(DeserializationFeature.UNWRAP_ROOT_VALUE.getMask()));

        Assert.assertEquals(serializationConfig.getSerializationInclusion(), JsonInclude.Include.NON_NULL);
    }
}