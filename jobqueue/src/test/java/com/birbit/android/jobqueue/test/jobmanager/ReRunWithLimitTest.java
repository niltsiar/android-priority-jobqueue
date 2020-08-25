package com.birbit.android.jobqueue.test.jobmanager;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(RobolectricTestRunner.class)

public class ReRunWithLimitTest extends JobManagerTestBase {

    @Test
    public void testReRunWithLimit() throws Exception {
        JobManager jobManager = createJobManager();
        testReRun(jobManager);
    }

    private void testReRun(final JobManager jobManager) throws
                                                        InterruptedException {
        DummyJobWithRunCount.runCount = 0;//reset
        final DummyJobWithRunCount job = new DummyJobWithRunCount();
        waitUntilAJobIsDone(jobManager, new WaitUntilCallback() {
            @Override
            public void run() {
                jobManager.addJob(job);
            }

            @Override
            public void assertJob(Job job) {

            }
        });

        MatcherAssert.assertThat(DummyJobWithRunCount.runCount, equalTo(job.getRetryLimit()));
        MatcherAssert.assertThat((int) jobManager.count(), equalTo(0));
    }
}
