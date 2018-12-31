package org.onap.vid.job.command;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.Action;
import org.onap.vid.model.serviceInstantiation.BaseResource;
import org.onap.vid.mso.RestMsoImplementation;
import org.springframework.http.HttpMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.ws.rs.ProcessingException;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.onap.vid.job.command.ResourceCommandKt.ACTION_PHASE;
import static org.onap.vid.job.command.ResourceCommandKt.INTERNAL_STATE;
import static org.onap.vid.utils.Logging.getMethodCallerName;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class ResourceCommandTest {

    public static class MockCommand extends ResourceCommand {

        public MockCommand(InternalState mockState, Action mockPhase, Job.JobStatus mockedJobStatus) {
            super(mock(RestMsoImplementation.class, RETURNS_MOCKS), mock(InProgressStatusService.class), mock(MsoResultHandlerService.class, RETURNS_MOCKS), mock(WatchChildrenJobsBL.class));

            this.mockedJobStatus = mockedJobStatus;
            this.mockState = mockState;
            this.mockPhase = mockPhase;
            if (mockState==InternalState.INITIAL) {
                init(mock(JobSharedData.class), Collections.emptyMap());
            }
            else {
                init(mock(JobSharedData.class), ImmutableMap.of(INTERNAL_STATE, mockState.name(), ACTION_PHASE, mockPhase.name()));
            }
            when(this.getWatchChildrenJobsBL().cumulateJobStatus(any(), any())).thenReturn(mockedJobStatus);
        }

        private final Job.JobStatus mockedJobStatus;
        private final InternalState mockState;
        private final Action mockPhase;


        @NotNull
        @Override
        public Job.JobStatus createChildren() {
            if (mockState == InternalState.CREATING_CHILDREN || (mockState == InternalState.INITIAL && mockPhase== Action.Delete))
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
        public MsoRestCallPlan planCreateMyselfRestCall(@NotNull CommandParentData commandParentData, @NotNull JobAdapter.AsyncJobRequest request, @NotNull String userId) {
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

        public MockCommandTestingStateMachine(InternalState mockState, Action mockPhase, Job.JobStatus mockedJobStatus, boolean mockedNeedToDeleteMySelf) {
            this(mockState, mockPhase, mockedJobStatus, mockedNeedToDeleteMySelf, false);
        }

        public MockCommandTestingStateMachine(InternalState mockState, Action mockPhase, Job.JobStatus mockedJobStatus, boolean mockedNeedToDeleteMySelf, boolean isService) {
            super(mockState, mockPhase, mockedJobStatus);
            this.mockedNeedToDeleteMySelf = mockedNeedToDeleteMySelf;
            this.isService = isService;
            this.sharedData = mock(JobSharedData.class, RETURNS_MOCKS);
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
        MockCommandTestingStateMachine underTest = new MockCommandTestingStateMachine(InternalState.TERMINAL, Action.Delete, Job.JobStatus.FAILED, true);
        assertEquals(expectedState, underTest.calcNextStateDeletePhase(jobStatus, internalState));
    }

    @Test
    public void whenNoNeedToDeleteMyself_internalStateMovesFromWatchingToTerminal() {
        MockCommandTestingStateMachine underTest = new MockCommandTestingStateMachine(InternalState.WATCHING, Action.Delete, Job.JobStatus.COMPLETED, false);
        assertEquals(InternalState.TERMINAL, underTest.calcNextStateDeletePhase(Job.JobStatus.COMPLETED, InternalState.WATCHING));
    }

    @DataProvider
    public static Object[][] testShallStopJobDataProvider() {
        return new Object[][]{
                {Job.JobStatus.IN_PROGRESS, Action.None, false, false},
                {Job.JobStatus.COMPLETED_WITH_NO_ACTION, Action.None, false, false},
                {Job.JobStatus.COMPLETED, Action.None, false, false},
                {Job.JobStatus.FAILED, Action.None, false, true},
                {Job.JobStatus.COMPLETED_WITH_ERRORS, Action.None, false, true},
                {Job.JobStatus.COMPLETED_WITH_ERRORS, Action.None, true, false},
                {Job.JobStatus.FAILED, Action.None, true, false},
                {Job.JobStatus.FAILED, Action.Delete, true, true},
                {Job.JobStatus.FAILED, Action.Create, true, true},
        };
    }


    @Test(dataProvider = "testShallStopJobDataProvider")
    public void testShallStopJob(Job.JobStatus jobStatus, Action action, boolean isService, boolean expectedResult) {
        //in this test, there is no meaning to constructor parameters besides isService
        MockCommandTestingStateMachine underTest = new MockCommandTestingStateMachine(InternalState.WATCHING, Action.Delete, Job.JobStatus.COMPLETED, false, isService);

        BaseResource mockedRequest = mock(BaseResource.class);
        when(underTest.getSharedData().getRequest()).thenReturn(mockedRequest);
        when(mockedRequest.getAction()).thenReturn(action);

        assertEquals(expectedResult, underTest.shallStopJob(jobStatus));
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

        MockCommandTestingStateMachine underTest = new MockCommandTestingStateMachine(internalState, Action.Delete, currentStateResult, true);
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

    @Test(expectedExceptions = IllegalStateException.class)
    public void whenCommandInUnMappedState_exceptionIsThrown() {
        MockCommandTestingStateMachine underTest = new MockCommandTestingStateMachine(InternalState.TERMINAL, Action.Delete, Job.JobStatus.COMPLETED, true);
        underTest.call();
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
        MockCommand underTest = new MockCommand(InternalState.IN_PROGRESS, Action.Delete, mockedJobStatus);
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
        MockCommand underTest = new MockCommand(InternalState.IN_PROGRESS, Action.Delete, expectedJobStatus);
        when(underTest.getInProgressStatusService().call(any(), any(), any())).thenThrow(exception);
        assertEquals(expectedJobStatus, underTest.inProgress());
    }

    @DataProvider
    public static Object[][] testIsNeedToDeleteMySelfDataProvider() {
        return Stream.of(Action.values())
                .map(status -> new Object[] { status })
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "testIsNeedToDeleteMySelfDataProvider")
    public void testIsNeedToDeleteMySelf(Action action) {
        boolean expectedResult = (action== Action.Delete);
        MockCommand underTest = new MockCommand(InternalState.DELETE_MYSELF, Action.Delete, Job.JobStatus.IN_PROGRESS);
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
        MockCommand underTest = new MockCommand(InternalState.WATCHING, Action.Delete, Job.JobStatus.IN_PROGRESS);
        when(underTest.getWatchChildrenJobsBL().retrieveChildrenJobsStatus(any())).thenReturn(childrenJobsStatus);
        assertEquals(expectedJobStatus, underTest.watchChildren());
    }

}
