package org.onap.vid.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.vid.aai.PombaClientInterface;
import org.onap.vid.model.PombaInstance.PombaRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PombaServiceImplTest {

  @Mock
  private PombaClientInterface pombaClientInterface;

  @InjectMocks
  private PombaServiceImpl testSubject;

  @BeforeClass
  public void beforeClass() {
    initMocks(this);
  }

  @BeforeMethod
  public void resetMocks() {
    Mockito.reset(pombaClientInterface);
  }

  @Test
  public void testVerify() {
    PombaRequest pombaRequest = new PombaRequest();
    testSubject.verify(pombaRequest);
    verify(pombaClientInterface, times(1))
        .verify(pombaRequest);
  }
}