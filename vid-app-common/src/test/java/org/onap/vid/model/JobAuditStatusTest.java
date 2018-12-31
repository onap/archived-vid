package org.onap.vid.model;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JobAuditStatusTest {

    @DataProvider
    public static Object[][] AdditionalInfoSizes() {
        return new Object[][]{
                {5, 5},
                {1999,1999},
                {2000, 2000},
                {2001, 2000},
                {10000, 2000}
        };
    }

    @Test(dataProvider = "AdditionalInfoSizes")
    public void testAdditionalInfoMaxLength(int originalSize, int finalSize) {
        JobAuditStatus jobAuditStatus = new JobAuditStatus();
        jobAuditStatus.setAdditionalInfo(StringUtils.repeat("a", originalSize));
        assertThat(jobAuditStatus.getAdditionalInfo().length(), is(finalSize));
    }

    @Test(dataProvider = "AdditionalInfoSizes")
    public void testAdditionalInfoMaxLengthInConstructor(int originalSize, int finalSize) {
        final String additionalInfo = StringUtils.repeat("a", originalSize);
        JobAuditStatus jobAuditStatus = new JobAuditStatus(UUID.randomUUID(), "myJobStatus", JobAuditStatus.SourceStatus.MSO, UUID.randomUUID(), additionalInfo, new Date());
        assertThat(jobAuditStatus.getAdditionalInfo().length(), is(finalSize));
    }

}
