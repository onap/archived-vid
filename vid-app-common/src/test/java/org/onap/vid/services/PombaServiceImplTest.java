/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */
package org.onap.vid.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import com.google.common.collect.ImmutableList;
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
    PombaRequest pombaRequest = new PombaRequest(ImmutableList.of());
    testSubject.verify(pombaRequest);
    verify(pombaClientInterface, times(1))
        .verify(pombaRequest);
  }
}