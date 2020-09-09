package dev.niltsiar.android.jobqueue.test.jobmanager;

import dev.niltsiar.android.jobqueue.Job;
import dev.niltsiar.android.jobqueue.JobHolder;
import dev.niltsiar.android.jobqueue.JobManager;
import dev.niltsiar.android.jobqueue.Params;
import dev.niltsiar.android.jobqueue.test.jobs.DummyJob;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(RobolectricTestRunner.class)

public class SessionIdTest extends JobManagerTestBase {
    @Test
    public void testSessionId() throws Throwable {
        JobManager jobManager = createJobManager();
        Long sessionId = mockTimer.nanoTime(); //we know job manager uses this value :/
        jobManager.stop();
        Job[] jobs = new Job[]{new DummyJob(new Params(0)), new DummyJob(new Params(0))};
        for (Job job : jobs) {
            jobManager.addJob(job);
        }

        for (int i = 0; i < jobs.length; i++) {
            JobHolder jobHolder = nextJob(jobManager);
            MatcherAssert.assertThat("session id should be correct for job " + i, jobHolder.getRunningSessionId(), equalTo(sessionId));
        }
    }
}
