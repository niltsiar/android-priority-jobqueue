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

public class ClearTest extends JobManagerTestBase {
    @Test
    public void testClear() throws Exception {
        JobManager jobManager = createJobManager();
        final int LIMIT = 20;
        for(int i = 0; i < LIMIT; i++) {
            jobManager.addJob(new DummyJob(new Params(0)));
        }
        jobManager.clear();
        MatcherAssert.assertThat("after clear, count should be 0", jobManager.count(), equalTo(0));
    }
}
