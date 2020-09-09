package dev.niltsiar.android.jobqueue.test.jobmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dev.niltsiar.android.jobqueue.CancelReason;
import dev.niltsiar.android.jobqueue.Job;
import dev.niltsiar.android.jobqueue.JobManager;
import dev.niltsiar.android.jobqueue.Params;
import dev.niltsiar.android.jobqueue.RetryConstraint;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(RobolectricTestRunner.class)

public class RunFailingJobTest extends JobManagerTestBase {
    @Test
    public void runFailingJob() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        JobManager jobManager = createJobManager();
        jobManager.addJob(new Job(new Params(0).requireNetwork()) {
            @Override
            public void onAdded() {

            }

            @Override
            public void onRun() throws Throwable {
                throw new RuntimeException();
            }

            @Override
            protected void onCancel(@CancelReason int cancelReason, @Nullable Throwable throwable) {
                latch.countDown();
            }

            @Override
            protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
                return RetryConstraint.CANCEL;
            }
        });
        latch.await(10, TimeUnit.SECONDS);
        MatcherAssert.assertThat((int) latch.getCount(), equalTo(0));
    }

}
