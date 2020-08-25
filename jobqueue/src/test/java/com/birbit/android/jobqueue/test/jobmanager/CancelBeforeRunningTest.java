package com.birbit.android.jobqueue.test.jobmanager;

import com.birbit.android.jobqueue.CancelResult;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.TagConstraint;
import com.birbit.android.jobqueue.test.jobs.DummyJob;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)

public class CancelBeforeRunningTest extends JobManagerTestBase {
    @Test
    public void testCancelBeforeRunning() {
        JobManager jobManager = createJobManager();
        jobManager.stop();
        DummyJob nonPersistentJob = new DummyJob(new Params(0).addTags("dummyTag"));
        DummyJob persistentJob = new DummyJob(new Params(0).addTags("dummyTag"));

        jobManager.addJob(nonPersistentJob);
        jobManager.addJob(persistentJob);
        CancelResult result = jobManager.cancelJobs(TagConstraint.ANY, "dummyTag");
        assertThat("both jobs should be cancelled", result.getCancelledJobs()
                                                          .size(), is(2));
        assertThat("both jobs should be cancelled", result.getFailedToCancel()
                                                          .size(), is(0));
        for (Job j : result.getCancelledJobs()) {
            DummyJob job = (DummyJob) j;
            assertThat("job is still added", job.getOnAddedCnt(), is(1));
            assertThat("job is cancelled", job.getOnCancelCnt(), is(1));
            assertThat("job is NOT run", job.getOnRunCnt(), is(0));
        }
    }

    CountDownLatch nonPersistentJobLatch = new CountDownLatch(1);

    @Test
    public void testCancelBeforeRunningWithGroups() throws
                                                    InterruptedException {
        JobManager jobManager = createJobManager();
        jobManager.stop();
        DummyJob nonPersistentJob = new DummyJob(new Params(0).addTags("dummyTag")
                                                              .groupBy("group1"));
        DummyJob persistentJob = new DummyJob(new Params(0).addTags("dummyTag")
                                                           .groupBy("group2"));
        jobManager.addJob(nonPersistentJob);
        jobManager.addJob(persistentJob);
        CancelResult result = jobManager.cancelJobs(TagConstraint.ANY, "dummyTag");
        assertThat("both jobs should be cancelled", result.getCancelledJobs()
                                                          .size(), is(2));
        assertThat("both jobs should be cancelled", result.getFailedToCancel()
                                                          .size(), is(0));
        for (Job j : result.getCancelledJobs()) {
            DummyJob job = (DummyJob) j;
            assertThat("job is still added", job.getOnAddedCnt(), is(1));
            assertThat("job is cancelled", job.getOnCancelCnt(), is(1));
            assertThat("job is NOT run", job.getOnRunCnt(), is(0));
        }
        assertThat("there should not be any jobs in the queue", jobManager.count(), is(0));
        jobManager.start();
        DummyJob nonPersistentJob2 = new DummyJob(new Params(0).addTags("dummyTag")
                                                               .groupBy("group1")) {
            @Override
            public void onRun() throws
                                Throwable {
                super.onRun();
                nonPersistentJobLatch.countDown();
            }
        };

        jobManager.addJob(nonPersistentJob2);
        assertThat("a new job in the same group with canceled job should run", nonPersistentJobLatch.await(1, TimeUnit.SECONDS), is(true));
    }
}
