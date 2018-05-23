package org.onap.vid.aai.util;

import com.att.eelf.configuration.EELFLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LogHelperTest {

    public static final String TEST_MESSAGE = "TestMessage";
    public static final String PREFIX_REGEXP = "^\\d\\d\\:\\d\\d\\:\\d\\d\\:\\d\\d\\d\\d<==\\s\\b";

    @Mock
    private EELFLoggerDelegate eelfLoggerDelegate;
    @Captor
    private ArgumentCaptor<String> captorInfo;
    @Captor
    private ArgumentCaptor<String> captorDebug;

    private LogHelper logHelper;

    @Before
    public void setUp() throws Exception {
        logHelper = new LogHelper(eelfLoggerDelegate);
    }

    @Test
    public void testMultilogShouldHaveCorrectFormat(){
        logHelper.multilog(TEST_MESSAGE);
        verify(eelfLoggerDelegate).info(Mockito.any(EELFLogger.class), captorInfo.capture());
        assertThat(captorInfo.getValue(), matchesPattern(PREFIX_REGEXP+TEST_MESSAGE+"\\b$"));
        verify(eelfLoggerDelegate).debug(Mockito.any(EELFLogger.class), captorDebug.capture());
        assertThat(captorDebug.getValue(), matchesPattern(PREFIX_REGEXP+TEST_MESSAGE+"\\b$"));
    }

    @Test
    public void testMultilogWithThrowableShouldHaveCorrectFormat(){
        logHelper.multilog(TEST_MESSAGE + " ", new NullPointerException(TEST_MESSAGE));
        verify(eelfLoggerDelegate).info(Mockito.any(EELFLogger.class), captorInfo.capture());
        assertThat(captorInfo.getValue(), matchesPattern(PREFIX_REGEXP+TEST_MESSAGE+"\\b.*\\bNullPointerException\\b.*$"));
        verify(eelfLoggerDelegate).debug(Mockito.any(EELFLogger.class), captorDebug.capture());
        assertThat(captorDebug.getValue(), matchesPattern(PREFIX_REGEXP+TEST_MESSAGE+"\\b.*\\bNullPointerException\\b.*$"));
    }

    @Test
    public void testDebuglogShouldHaveCorrectFormat(){
        logHelper.logDebug(TEST_MESSAGE);
        verify(eelfLoggerDelegate).debug(Mockito.any(EELFLogger.class), captorDebug.capture());
        assertThat(captorDebug.getValue(), matchesPattern(PREFIX_REGEXP+TEST_MESSAGE+"\\b$"));
    }

    @Test
    public void testDebuglogWithThrowableShouldHaveCorrectFormat(){
        logHelper.logDebug(TEST_MESSAGE + " ", new NullPointerException(TEST_MESSAGE));
        verify(eelfLoggerDelegate).debug(Mockito.any(EELFLogger.class), captorDebug.capture());
        assertThat(captorDebug.getValue(), matchesPattern(PREFIX_REGEXP+TEST_MESSAGE+"\\b.*\\bNullPointerException\\b.*$"));
    }
}
