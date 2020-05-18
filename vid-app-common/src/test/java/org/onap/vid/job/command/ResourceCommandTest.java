/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.job.command;

import static java.util.Collections.emptyList;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.job.command.ResourceCommandKt.ACTION_PHASE;
import static org.onap.vid.job.command.ResourceCommandKt.INTERNAL_STATE;
import static org.onap.vid.job.command.ResourceCommandTest.FakeResourceCreator.createGroup;
import static org.onap.vid.job.command.ResourceCommandTest.FakeResourceCreator.createMember;
import static org.onap.vid.job.command.ResourceCommandTest.FakeResourceCreator.createNetwork;
import static org.onap.vid.job.command.ResourceCommandTest.FakeResourceCreator.createService;
import static org.onap.vid.job.command.ResourceCommandTest.FakeResourceCreator.createVfModule;
import static org.onap.vid.job.command.ResourceCommandTest.FakeResourceCreator.createVnf;
import static org.onap.vid.model.Action.Create;
import static org.onap.vid.model.Action.Delete;
import static org.onap.vid.model.Action.None;
import static org.onap.vid.model.Action.Resume;
import static org.onap.vid.model.Action.values;
import static org.onap.vid.utils.Logging.getMethodCallerName;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.ProcessingException;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.onap.vid.exceptions.AbortingException;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.exceptions.TryAgainException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.Action;
import org.onap.vid.model.serviceInstantiation.BaseResource;
import org.onap.vid.model.serviceInstantiation.InstanceGroup;
import org.onap.vid.model.serviceInstantiation.InstanceGroupMember;
import org.onap.vid.model.serviceInstantiation.Network;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.VfModule;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.properties.FeatureSetsManager;
import org.springframework.http.HttpMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ResourceCommandTest {

    public static class MockCommand extends ResourceCommand {

        public MockCommand(InternalState mockState, Action mockPhase, Job.JobStatus mockedJobStatus) {
            this(mockState, mockPhase, mockedJobStatus, false);
        }

        public MockCommand(InternalState mockState, Action mockPhase, Job.JobStatus mockedJobStatus, boolean lateInit) {
            super(
                    mock(RestMsoImplementation.class, RETURNS_MOCKS),
                    mock(InProgressStatusService.class),
                    mock(MsoResultHandlerService.class, RETURNS_MOCKS),
                    mock(WatchChildrenJobsBL.class),
                    mock(JobsBrokerService.class, RETURNS_MOCKS),
                    mock(JobAdapter.class, RETURNS_MOCKS),
                    mock(FeatureSetsManager.class));
            this.mockedJobStatus = mockedJobStatus;
            this.mockState = mockState;
            this.mockPhase = mockPhase;
            if (!lateInit) {
                init();
            }
            when(this.getWatchChildrenJobsBL().cumulateJobStatus(any(), any())).thenReturn(mockedJobStatus);
        }

        protected void init() {
            if (mockState == InternalState.INITIAL) {
                init(mock(JobSharedData.class), Collections.emptyMap());
            } else {
                init(mock(JobSharedData.class), ImmutableMap.of(INTERNAL_STATE, mockState.name(), ACTION_PHASE, mockPhase.name()));
            }
        }

        private final Job.JobStatus mockedJobStatus;
        private final InternalState mockState;
        private final Action mockPhase;


        @NotNull
        @Override
        public Job.JobStatus createChildren() {
            if (mockState == InternalState.CREATING_CHILDREN || (mockState == InternalState.INITIAL && mockPhase== Delete))
                return mockedJobStatus;
            throw (new RuntimeException("Not expected to call "+getMethodCallerName()));
        }

        protected Job.JobStatus mockedStatusOrThrow(InternalState expectedState) {
            if (mockState == expectedState)
                return mockedJobStatus;
            throw (new RuntimeException("Not expected to call "+getMethodCallerName()));
        }

        protected MsoRestCallPlan mockedPlanOrThrow(InternalState expectedState) {
            if (mockState == expectedState)
                return new MsoRestCallPlan(HttpMethod.POST, "path", Optional.empty(), Optional.empty(), "nothing");
            throw (new RuntimeException("Not expected to call "+getMethodCallerName()));
        }

        @NotNull
        @Override
        public MsoRestCallPlan planCreateMyselfRestCall(@NotNull CommandParentData commandParentData, @NotNull JobAdapter.AsyncJobRequest request, @NotNull String userId, String testApi) {
            return mockedPlanOrThrow(InternalState.CREATE_MYSELF);
        }

        @NotNull
        @Override
        public MsoRestCallPlan planDeleteMyselfRestCall(@NotNull CommandParentData commandParentData, @NotNull JobAdapter.AsyncJobRequest request, @NotNull String userId) {
            return mockedPlanOrThrow(InternalState.DELETE_MYSELF);
        }
    }

    public static class MockCommandTestingStateMachine extends MockCommand {

        private final JobSharedData sharedData;
        private final boolean isDescendantHasAction;

        public MockCommandTestingStateMachine(InternalState mockState, Action mockPhase, Job.JobStatus mockedJobStatus, boolean mockedNeedToDeleteMySelf) {
            this(mockState, mockPhase, mockedJobStatus, mockedNeedToDeleteMySelf, false, true);
        }

        public MockCommandTestingStateMachine(InternalState mockState, Action mockPhase, Job.JobStatus mockedJobStatus, boolean mockedNeedToDeleteMySelf, boolean isService, boolean isDescendantHasAction) {
            super(mockState, mockPhase, mockedJobStatus, true);
            this.mockedNeedToDeleteMySelf = mockedNeedToDeleteMySelf;
            this.isService = isService;
            this.sharedData = mock(JobSharedData.class, RETURNS_MOCKS);
            this.isDescendantHasAction = isDescendantHasAction;
            init();
        }

        protected final boolean mockedNeedToDeleteMySelf;
        private final boolean isService;

        @NotNull
        @Override
        public Job.JobStatus inProgress() {
            return mockedStatusOrThrow(InternalState.IN_PROGRESS);
        }

        @NotNull
        @Override
        public Job.JobStatus watchChildren() {
            return mockedStatusOrThrow(InternalState.WATCHING);
        }

        @Override
        public boolean isNeedToDeleteMyself() {
            return mockedNeedToDeleteMySelf;
        }

        @Override
        protected boolean isServiceCommand() {
            return isService;
        }

        @Override
        public JobSharedData getSharedData() {
            return sharedData;
        }

        @Override
        protected boolean isDescendantHasAction(@NotNull Action phase) {
            return isDescendantHasAction;
        }
    }

    @DataProvider
    public static Object[][] nextStateDeletePhaseProvider() {
        return new Object[][]{
                {InternalState.CREATING_CHILDREN, Job.JobStatus.COMPLETED, InternalState.WATCHING},
                {InternalState.WATCHING, Job.JobStatus.COMPLETED, InternalState.DELETE_MYSELF},
                {InternalState.WATCHING, Job.JobStatus.IN_PROGRESS, InternalState.WATCHING},
                {InternalState.WATCHING, Job.JobStatus.RESOURCE_IN_PROGRESS, InternalState.WATCHING},
                {InternalState.DELETE_MYSELF, Job.JobStatus.COMPLETED, InternalState.IN_PROGRESS},
                {InternalState.IN_PROGRESS, Job.JobStatus.COMPLETED, InternalState.TERMINAL},
                {InternalState.IN_PROGRESS, Job.JobStatus.IN_PROGRESS, InternalState.IN_PROGRESS},
                {InternalState.IN_PROGRESS, Job.JobStatus.RESOURCE_IN_PROGRESS, InternalState.IN_PROGRESS},
        };
    }

    @Test(dataProvider = "nextStateDeletePhaseProvider")
    public void whenCalcNextStateDeletePhase_expectedStateIsReturned(
            InternalState internalState, Job.JobStatus jobStatus, InternalState expectedState) {

        //there is no meaning to the constructor inputs here
        MockCommandTestingStateMachine underTest = new MockCommandTestingStateMachine(InternalState.TERMINAL, Delete, Job.JobStatus.FAILED, true);
        assertEquals(expectedState, underTest.calcNextStateDeletePhase(jobStatus, internalState));
    }

    @Test
    public void whenNoNeedToDeleteMyself_internalStateMovesFromWatchingToTerminal() {
        MockCommandTestingStateMachine underTest = new MockCommandTestingStateMachine(InternalState.WATCHING, Delete, Job.JobStatus.COMPLETED, false);
        assertEquals(InternalState.TERMINAL, underTest.calcNextStateDeletePhase(Job.JobStatus.COMPLETED, InternalState.WATCHING));
    }

    @DataProvider
    public static Object[][] testShallStopJobDataProvider() {
        return new Object[][]{
                {Job.JobStatus.IN_PROGRESS, None, false, false},
                {Job.JobStatus.COMPLETED_WITH_NO_ACTION, None, false, false},
                {Job.JobStatus.COMPLETED, None, false, false},
                {Job.JobStatus.FAILED, None, false, true},
                {Job.JobStatus.COMPLETED_WITH_ERRORS, None, false, true},
                {Job.JobStatus.COMPLETED_WITH_ERRORS, None, true, false},
                {Job.JobStatus.FAILED, None, true, false},
                {Job.JobStatus.FAILED, Delete, true, true},
                {Job.JobStatus.FAILED, Create, true, true},
        };
    }


    @Test(dataProvider = "testShallStopJobDataProvider")
    public void testShallStopJob(Job.JobStatus jobStatus, Action action, boolean isService, boolean expectedResult) {
        //in this test, there is no meaning to constructor parameters besides isService
        MockCommandTestingStateMachine underTest = new MockCommandTestingStateMachine(InternalState.WATCHING, Delete, Job.JobStatus.COMPLETED, false, isService, true);

        BaseResource mockedRequest = mock(BaseResource.class);
        when(underTest.getSharedData().getRequest()).thenReturn(mockedRequest);
        when(mockedRequest.getAction()).thenReturn(action);

        assertEquals(expectedResult, underTest.shallStopJob(jobStatus));
    }

    public static class FakeResourceCreator {

        public static<T> Map<String, T> convertToMap(List<T> list) {
            if (list==null) {
                return null;
            }
            return list.stream().collect(Collectors.toMap(x-> UUID.randomUUID().toString(), x->x));
        }

        static ServiceInstantiation createService(List<Vnf> vnfs, List<Network> networks, List<InstanceGroup> vnfGroups) {
            return new ServiceInstantiation(mock(ModelInfo.class), null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                    convertToMap(vnfs),
                    convertToMap(networks),
                    convertToMap(vnfGroups),
                    null,
                    null, false, 1, false,false,null, null, null, null, null, null, null, null);
        }

        public static ServiceInstantiation createServiceWith2InstancesInEachLevel(Action action) {
            return createService(
                    ImmutableList.of(
                            createVnf(ImmutableList.of(createVfModule(action), createVfModule(action)), action),
                            createVnf(ImmutableList.of(createVfModule(action), createVfModule(action)), action)),
                    ImmutableList.of(
                            createNetwork(action),
                            createNetwork(action)),
                    ImmutableList.of(
                            createGroup(ImmutableList.of(createMember(action), createMember(action)), action),
                            createGroup(ImmutableList.of(createMember(action), createMember(action)), action))
                    );
        }

        static InstanceGroup createGroup(List<InstanceGroupMember> groupMembers, Action action) {
            return new InstanceGroup(mock(ModelInfo.class), null, action.name(), false, null, convertToMap(groupMembers), null, null, null,
                null, null);
        }

        static InstanceGroupMember createMember(Action action) {
            return new InstanceGroupMember(null, action.toString(), null, null, null, null, null);
        }

        static Vnf createVnf(List<VfModule> vfModules, Action action) {
            Map<String, Map<String, VfModule>> vfModulesMap = new HashMap<>();
            vfModulesMap.put("abc",convertToMap(vfModules));

            return new Vnf(mock(ModelInfo.class), null, null, action.toString(), null, null, null, null, null, null, false, null, vfModulesMap, null, null, null,
                null, null);
        }

        static Vnf createVnf(Action action) {
            return new Vnf(mock(ModelInfo.class), null, null, action.toString(), null, null, null, null, null, null, false, null,null, null, null, null,
                null, null);
        }

        static VfModule createVfModule(Action action) {
            return new VfModule(mock(ModelInfo.class), null, null, action.toString(), null, null, null, null, null,
                false, false, null, null, null, null, null, null, null, null, null);
        }

        static Network createNetwork(Action action) {
            return new Network(mock(ModelInfo.class), null, null, action.toString(), null, null, null, null, null, null, false, null, null, null, null,
                null, null);
        }
    }

    @DataProvider
    public static Object[][] testIsDescendantHasActionDataProvider() {
        return new Object[][]{
                {"empty service", Create, false, createService(emptyList(), emptyList(), emptyList())},
                {"instance group with None", Create, false, createService(emptyList(), emptyList(), ImmutableList.of(createGroup(emptyList(), None)))},
                {"instance group with Create", Create, true, createService(emptyList(), emptyList(), ImmutableList.of(createGroup(emptyList(), Create)))},
                {"instance group None + member Delete", Delete, true, createService(emptyList(), emptyList(), ImmutableList.of(createGroup(ImmutableList.of(createMember(Delete)), None)))},
                {"instance group None + member Create", Delete, false, createService(emptyList(), emptyList(), ImmutableList.of(createGroup(ImmutableList.of(createMember(Create)), None)))},
                {"instance group None + member Create + member Delete", Delete, true,
                        createService(emptyList(), emptyList(), ImmutableList.of(createGroup(ImmutableList.of(createMember(Create), createMember(Delete)), None)))},
                {"vnf Create", Delete, false, createService(ImmutableList.of(createVnf(emptyList(), Create)), emptyList(),emptyList())},
                {"vnf Create", Create, true, createService(ImmutableList.of(createVnf(emptyList(), Create)), emptyList(),emptyList())},
                {"vnf Create null VfModules internal map", Create, false, createService(ImmutableList.of(createVnf(null, Delete)), emptyList(),emptyList())},
                {"vnf Delete with null VfModules", Create, false, createService(ImmutableList.of(createVnf(Delete)), emptyList(),emptyList())},
                {"vnf None + VfModule Create", Create, true, createService(ImmutableList.of(createVnf(ImmutableList.of(createVfModule(Create)), None)), emptyList(),emptyList())},
                {"vnf None + VfModule None", Create, false, createService(ImmutableList.of(createVnf(ImmutableList.of(createVfModule(None)), None)), emptyList(),emptyList())},
                {"network Create", Create, true, createService(emptyList(), ImmutableList.of(createNetwork(Create)), emptyList())},
                {"network Delete", Create, false, createService(emptyList(), ImmutableList.of(createNetwork(Delete)), emptyList())},
        };
    }

    @Test(dataProvider = "testIsDescendantHasActionDataProvider")
    public void testIsDescendantHasAction(String desc, Action action, boolean expectedResult, BaseResource request) {
        //in this test, there is no meaning to constructor parameters
        MockCommand underTest = new MockCommand(InternalState.WATCHING, Delete, Job.JobStatus.COMPLETED);
        assertEquals(expectedResult, underTest.isDescendantHasAction(request, action));
    }

    @DataProvider
    public static Object[][] testCallDataProvider() {
        return new Object[][]{
                {"initial state with successful creating children" ,InternalState.INITIAL, Job.JobStatus.COMPLETED, InternalState.WATCHING, Job.JobStatus.RESOURCE_IN_PROGRESS},
                {"initial state with failed creating children", InternalState.INITIAL, Job.JobStatus.FAILED, null, Job.JobStatus.FAILED},
                {"watching state with children still in progress" ,InternalState.WATCHING, Job.JobStatus.RESOURCE_IN_PROGRESS, InternalState.WATCHING, Job.JobStatus.RESOURCE_IN_PROGRESS},
                {"watching state with children that completed with errors" ,InternalState.WATCHING, Job.JobStatus.COMPLETED_WITH_ERRORS, null, Job.JobStatus.COMPLETED_WITH_ERRORS},
                {"watching state with children that completed with no action" ,InternalState.WATCHING, Job.JobStatus.COMPLETED_WITH_NO_ACTION, InternalState.DELETE_MYSELF, Job.JobStatus.RESOURCE_IN_PROGRESS},
                {"watching state with children that has completed" ,InternalState.WATCHING, Job.JobStatus.COMPLETED, InternalState.DELETE_MYSELF, Job.JobStatus.RESOURCE_IN_PROGRESS},
                {"mso call state that failed" ,InternalState.DELETE_MYSELF, Job.JobStatus.FAILED, null, Job.JobStatus.FAILED},
                //TODO handle AAI get unique name state {"mso call state that still in progress" ,InternalState.DELETE_MYSELF, Job.JobStatus.FAILED, null, Job.JobStatus.FAILED, false},
                {"mso call state that success" ,InternalState.DELETE_MYSELF, Job.JobStatus.COMPLETED, InternalState.IN_PROGRESS, Job.JobStatus.RESOURCE_IN_PROGRESS},
                {"in progress return in progress" ,InternalState.IN_PROGRESS, Job.JobStatus.IN_PROGRESS, InternalState.IN_PROGRESS, Job.JobStatus.RESOURCE_IN_PROGRESS},
                {"in progress return in pause" ,InternalState.IN_PROGRESS, Job.JobStatus.PAUSE, InternalState.IN_PROGRESS, Job.JobStatus.RESOURCE_IN_PROGRESS},
                {"in progress return in pause" ,InternalState.IN_PROGRESS, Job.JobStatus.STOPPED, null, Job.JobStatus.STOPPED},
                {"in progress return in pause" ,InternalState.IN_PROGRESS, Job.JobStatus.FAILED, null, Job.JobStatus.FAILED},
                {"in progress return in pause" ,InternalState.IN_PROGRESS, Job.JobStatus.COMPLETED, null, Job.JobStatus.COMPLETED},

        };
    }

    @Test(dataProvider = "testCallDataProvider")
    public void whenCallCommandWithDeletePhase_nextJobStatusAndInternalStateAreAsExpected(
            String description, InternalState internalState, Job.JobStatus currentStateResult,
            InternalState expectedNextState, Job.JobStatus expectedNextStatus) {

        MockCommandTestingStateMachine underTest = new MockCommandTestingStateMachine(internalState, Delete, currentStateResult, true);
        NextCommand nextCommand = underTest.call();
        assertEquals(expectedNextStatus, nextCommand.getStatus());

        //expectedNextState == null means nextCommand has no real command
        if (expectedNextState!=null) {
            assertEquals(expectedNextState, (nextCommand.getCommand().getData().get(INTERNAL_STATE)));
            assertFalse(nextCommand.getStatus().isFinal());
        }
        else {
            assertNull(nextCommand.getCommand());
            assertTrue(nextCommand.getStatus().isFinal());
        }
    }

    @DataProvider
    public static Object[][] InProgressDataProvider() {
        return Stream.of(Job.JobStatus.values())
                .map(status -> new Object[] { status })
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "InProgressDataProvider")
    public void whenGetResultFromMso_InProgressReturnThem(Job.JobStatus mockedJobStatus) {
        Job.JobStatus expectedJobStatus = (mockedJobStatus== Job.JobStatus.PAUSE) ? Job.JobStatus.IN_PROGRESS : mockedJobStatus;
        MockCommand underTest = new MockCommand(InternalState.IN_PROGRESS, Delete, mockedJobStatus);
        when(underTest.getInProgressStatusService().call(any(), any(), any())).thenReturn(mockedJobStatus);
        assertEquals(expectedJobStatus, underTest.inProgress());
    }

    @DataProvider
    public static Object[][] InProgressExceptionsDataProvider() {
        return new Object[][]{
                {new ProcessingException(""), Job.JobStatus.IN_PROGRESS},
                {new InProgressStatusService.BadResponseFromMso(null), Job.JobStatus.IN_PROGRESS},
                {new GenericUncheckedException(""),Job.JobStatus.STOPPED }
        };
    }

    @Test(dataProvider = "InProgressExceptionsDataProvider")
    public void whenInProgressStatusServiceThrowException_InProgressReturnStatus(Exception exception, Job.JobStatus expectedJobStatus) {
        MockCommand underTest = new MockCommand(InternalState.IN_PROGRESS, Delete, expectedJobStatus);
        when(underTest.getInProgressStatusService().call(any(), any(), any())).thenThrow(exception);
        assertEquals(expectedJobStatus, underTest.inProgress());
    }

    @DataProvider
    public static Object[][] testIsNeedToDeleteMySelfDataProvider() {
        return Stream.of(values())
                .map(status -> new Object[] { status })
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "testIsNeedToDeleteMySelfDataProvider")
    public void testIsNeedToDeleteMySelf(Action action) {
        boolean expectedResult = (action== Delete);
        MockCommand underTest = new MockCommand(InternalState.DELETE_MYSELF, Delete, Job.JobStatus.IN_PROGRESS);
        BaseResource mockedBaseResource = mock(BaseResource.class);
        when(underTest.getSharedData().getRequest()).thenReturn(mockedBaseResource);
        when(mockedBaseResource.getAction()).thenReturn(action);
        assertEquals(expectedResult, underTest.isNeedToDeleteMyself());
    }

    @DataProvider
    public static Object[][] testWatchingDataProvider() {
        return new Object[][]{
                {"all children final, no failed child ", Job.JobStatus.COMPLETED, Job.JobStatus.COMPLETED},
                {"all children final, there is failed child ", Job.JobStatus.COMPLETED_WITH_ERRORS, Job.JobStatus.COMPLETED_WITH_ERRORS},
                {"not all children final", Job.JobStatus.IN_PROGRESS, Job.JobStatus.IN_PROGRESS},
        };
    }

    @Test(dataProvider = "testWatchingDataProvider")
    public void testWatching(String desc, Job.JobStatus childrenJobsStatus, Job.JobStatus expectedJobStatus) {
        MockCommand underTest = new MockCommand(InternalState.WATCHING, Delete, Job.JobStatus.IN_PROGRESS);
        when(underTest.getWatchChildrenJobsBL().retrieveChildrenJobsStatus(any())).thenReturn(childrenJobsStatus);
        assertEquals(expectedJobStatus, underTest.watchChildren());
    }

    @DataProvider
    public static Object[][] testCalcInitialStateDataProvider() {
        return new Object[][]{
                {Delete, true, Delete, InternalState.CREATING_CHILDREN},
                {Delete, false, Delete, InternalState.DELETE_MYSELF},
                {Delete, false, Create, InternalState.TERMINAL},
                {Delete, true, Create, InternalState.CREATING_CHILDREN},
                {Create, true, Create, InternalState.CREATE_MYSELF},
                {Create, false, Create, InternalState.CREATE_MYSELF},
                {Create, false, Delete, InternalState.TERMINAL},
                {Create, true, Delete, InternalState.CREATING_CHILDREN},
                {Create, true, Resume, InternalState.RESUME_MYSELF},
                {Delete, false, Resume, InternalState.TERMINAL},
        };
    }

    @Test(dataProvider = "testCalcInitialStateDataProvider")
    public void testCalcInitialState(Action phase, boolean isDescendantHasAction, Action action, InternalState expectedState) {
        ResourceCommand underTest = mock(ResourceCommand.class);
        when(underTest.calcInitialState(any(), any())).thenCallRealMethod();
        when(underTest.isDescendantHasAction(eq(phase))).thenReturn(isDescendantHasAction);
        when(underTest.getActionType()).thenReturn(action);
        when(underTest.isNeedToDeleteMyself()).thenCallRealMethod();
        when(underTest.isNeedToCreateMyself()).thenCallRealMethod();
        when(underTest.isNeedToResumeMySelf()).thenCallRealMethod();

        Map<String, String> commandData = ImmutableMap.of(INTERNAL_STATE, InternalState.INITIAL.name());
        assertEquals(expectedState, underTest.calcInitialState(commandData, phase));
    }


    //throw exception when call to create children
    //create children is just example, it could be any other method that called by ResourceCommand.invokeCommand
    public static class MockCommandThrowExceptionOnCreateChildren extends MockCommandTestingStateMachine {

        private final RuntimeException exceptionToThrow;

        public MockCommandThrowExceptionOnCreateChildren(RuntimeException exceptionToThrow) {
            super(InternalState.CREATING_CHILDREN, Delete, Job.JobStatus.COMPLETED, true);
            this.exceptionToThrow = exceptionToThrow;
            doAnswer(returnsFirstArg()).when(this.getWatchChildrenJobsBL()).cumulateJobStatus(any(), any());
        }

        @NotNull
        @Override
        public Job.JobStatus createChildren() {
            throw exceptionToThrow;
        }
    }

    @DataProvider
    public static Object[][] exceptionAndStateProvider() {
        return new Object[][]{
                {new TryAgainException(new Exception()), Job.JobStatus.RESOURCE_IN_PROGRESS},
                {new AbortingException(new Exception()), Job.JobStatus.FAILED},
        };
    }

    @Test(dataProvider = "exceptionAndStateProvider")
    public void whenKnownExceptionThrownInCommandInvocation_thenStateIsAsExpected(RuntimeException exception, Job.JobStatus expectedNextStatus) {
        MockCommandTestingStateMachine underTest = new MockCommandThrowExceptionOnCreateChildren(exception);
        NextCommand nextCommand = underTest.call();
        assertEquals(expectedNextStatus, nextCommand.getStatus());
    }

    @DataProvider
    public static Object[][] resourcePosition() {
        return new Object[][]{
            {1, 2, 3, ImmutableList.of(1,2,3)},
            {null, 1, 100, ImmutableList.of(101,1,100)},
            {null, null, null, ImmutableList.of(1,2,3)},
            {1,2,2, ImmutableList.of(1,2,2)}
        };
    }

    @Test(dataProvider = "resourcePosition")
    public void sortChildren_sortAccordingToPosition(Integer firstPosition, Integer secondPosition, Integer thirdPosition, List<Integer> expectedPositions){
        BaseResource mockedRequest1 = mock(BaseResource.class);
        when(mockedRequest1.getPosition()).thenReturn(firstPosition);
        BaseResource mockedRequest2 = mock(BaseResource.class);
        when(mockedRequest2.getPosition()).thenReturn(secondPosition);
        BaseResource mockedRequest3 = mock(BaseResource.class);
        when(mockedRequest3.getPosition()).thenReturn(thirdPosition);

        MockCommand underTest = new MockCommand(InternalState.CREATING_CHILDREN, Create, Job.JobStatus.IN_PROGRESS);
        List<Pair<BaseResource, Integer>> sortedList = underTest.setPositionWhereIsMissing(ImmutableList.of(mockedRequest1, mockedRequest2, mockedRequest3));

        assertEquals(sortedList.get(0).getSecond(),expectedPositions.get(0));
        assertEquals(sortedList.get(1).getSecond(),expectedPositions.get(1));
        assertEquals(sortedList.get(2).getSecond(),expectedPositions.get(2));
    }
}
