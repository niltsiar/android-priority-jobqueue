package com.birbit.android.jobqueue.test.jobmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.network.NetworkUtil;
import com.birbit.android.jobqueue.scheduling.Scheduler;
import com.birbit.android.jobqueue.scheduling.SchedulerConstraint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(ParameterizedRobolectricTestRunner.class)

public class SchedulerSimpleTestCase extends JobManagerTestBase {
    final boolean requireNetwork;
    final boolean requireUnmeteredNetwork;
    final long delayInMs;
    final Long deadline;

    public SchedulerSimpleTestCase(boolean requireNetwork, boolean requireUnmeteredNetwork, long delayInMs, Long deadline) {
        this.requireNetwork = requireNetwork;
        this.requireUnmeteredNetwork = requireUnmeteredNetwork;
        this.delayInMs = delayInMs;
        this.deadline = deadline;
    }

    @ParameterizedRobolectricTestRunner.Parameters(name = "reqNetwork: {0} reqUnmetered: {1} delay: {2} deadline: {3}")
    public static List<Object[]> getParams() {
        List<Object[]> params = new ArrayList<>();
        for (long delay : new long[]{0, 1000, JobManager.MIN_DELAY_TO_USE_SCHEDULER_IN_MS}) {
            for (Long deadline : new Long[]{null, 500L, JobManager.MIN_DELAY_TO_USE_SCHEDULER_IN_MS}) {
                if (deadline != null && deadline < delay) {
                    continue;
                }
                for (int i = 0; i < 4; i++) {
                    params.add(new Object[]{(i & 1) == 1, (i & 2) == 2, delay, deadline
                    });
                }
            }
        }
        return params;
    }

    @Test
    public void testScheduleWhenJobAdded() throws
                                           InterruptedException {
        Scheduler scheduler = Mockito.mock(Scheduler.class);
        ArgumentCaptor<SchedulerConstraint> captor = ArgumentCaptor.forClass(SchedulerConstraint.class);
        DummyNetworkUtilWithConnectivityEventSupport networkUtil = new DummyNetworkUtilWithConnectivityEventSupport();
        Configuration.Builder builder = new Configuration.Builder(RuntimeEnvironment.application).timer(mockTimer)
                                                                                                 .networkUtil(networkUtil)
                                                                                                 .inTestMode()
                                                                                                 .scheduler(scheduler, false);
        if (requireUnmeteredNetwork) {
            networkUtil.setNetworkStatus(NetworkUtil.UNMETERED);
        } else if (requireNetwork) {
            networkUtil.setNetworkStatus(NetworkUtil.METERED);
        } else {
            networkUtil.setNetworkStatus(NetworkUtil.DISCONNECTED);
        }

        final JobManager jobManager = createJobManager(builder);
        Params params = new Params(1);
        params.setRequiresNetwork(requireNetwork);
        params.setRequiresUnmeteredNetwork(requireUnmeteredNetwork);
        params.setDelayMs(delayInMs);
        if (deadline != null) {
            params.overrideDeadlineToRunInMs(deadline);
        }
        final SchedulerJob job = new SchedulerJob(params);
        final CountDownLatch cancelLatch = new CountDownLatch(1);
        Mockito.doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws
                                                            Throwable {
                cancelLatch.countDown();
                return null;
            }
        })
               .when(scheduler)
               .cancelAll();
        waitUntilJobsAreDone(jobManager, Collections.singletonList(job), new Runnable() {
            @Override
            public void run() {
                jobManager.addJob(job);
                mockTimer.incrementMs(delayInMs);
            }
        });

        Mockito.verify(scheduler, Mockito.never())
               .request(Mockito.any(SchedulerConstraint.class));
    }

    public static class SchedulerJob extends Job {

        @SuppressWarnings("WeakerAccess")
        public SchedulerJob(Params params) {
            super(params);
        }

        @Override
        public void onAdded() {

        }

        @Override
        public void onRun() throws
                            Throwable {

        }

        @Override
        protected void onCancel(@CancelReason int cancelReason, @Nullable Throwable throwable) {

        }

        @Override
        protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
            throw new UnsupportedOperationException("not expected to arrive here");
        }
    }
}
