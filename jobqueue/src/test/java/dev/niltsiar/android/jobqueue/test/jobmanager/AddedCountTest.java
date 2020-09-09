package dev.niltsiar.android.jobqueue.test.jobmanager;

import dev.niltsiar.android.jobqueue.JobManager;
import dev.niltsiar.android.jobqueue.Params;
import dev.niltsiar.android.jobqueue.test.jobs.DummyJob;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(RobolectricTestRunner.class)

public class AddedCountTest extends JobManagerTestBase {
    @Test
    public void testAddedCount() throws Exception {
        testAddedCount(new DummyJob(new Params(0)));

    }

    @Test
    public void testAddedCountPersistent() {
        testAddedCount(new DummyJob(new Params(0)));
    }

    private void testAddedCount(DummyJob dummyJob) {
        JobManager jobManager = createJobManager();
        jobManager.stop();
        jobManager.addJob(dummyJob);
        MatcherAssert.assertThat(1, equalTo(dummyJob.getOnAddedCnt()));
    }
}
