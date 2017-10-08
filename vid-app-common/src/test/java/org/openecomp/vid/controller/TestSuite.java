package org.openecomp.vid.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(

{ HealthCheckControllerTest.class, PropertyControllerTest.class, AaiControllerTest.class })
public class TestSuite { // nothing
}
