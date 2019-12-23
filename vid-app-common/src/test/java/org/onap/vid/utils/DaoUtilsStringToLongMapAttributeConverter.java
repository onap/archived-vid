package org.onap.vid.utils;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.collect.ImmutableMap;
import org.onap.vid.utils.DaoUtils.StringToLongMapAttributeConverter;
import org.testng.annotations.Test;

public class DaoUtilsStringToLongMapAttributeConverter {

    private final StringToLongMapAttributeConverter stringToLongMapAttributeConverter = new StringToLongMapAttributeConverter();

    @Test
    public void toEntity_givenNullString_yieldNullMap() {
        assertThat(
            stringToLongMapAttributeConverter.convertToEntityAttribute(null),
            is(nullValue()));
    }

    @Test
    public void toEntity_givenValidString_yieldGoodMap() {
        assertThat(
            stringToLongMapAttributeConverter.convertToEntityAttribute("{\"a\": 5}"),
            is(ImmutableMap.of("a", 5L)));
    }

    @Test(expectedExceptions = Exception.class)
    public void toEntity_givenBadString_throws() {
        stringToLongMapAttributeConverter.convertToEntityAttribute("{a: not-good}");
    }

    @Test
    public void fromEntity_givenNullMap_yieldNullString() {
        assertThat(
            stringToLongMapAttributeConverter.convertToDatabaseColumn(null),
            is(nullValue()));
    }

    @Test
    public void fromEntity_givenValidMap_yieldString() {
        assertThat(
            stringToLongMapAttributeConverter.convertToDatabaseColumn(ImmutableMap.of("a", 5L)),
            jsonEquals("{\"a\": 5}"));
    }

}
