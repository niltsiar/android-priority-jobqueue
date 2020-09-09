package dev.niltsiar.android.jobqueue.test.jobmanager;

import android.annotation.TargetApi;
import android.os.Build;
import androidx.annotation.NonNull;
import dev.niltsiar.android.jobqueue.Job;
import dev.niltsiar.android.jobqueue.JobManager;
import dev.niltsiar.android.jobqueue.Params;
import dev.niltsiar.android.jobqueue.callback.JobManagerCallback;
import dev.niltsiar.android.jobqueue.callback.JobManagerCallbackAdapter;
import dev.niltsiar.android.jobqueue.test.jobs.DummyJob;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(RobolectricTestRunner.class)

public class CountTest extends JobManagerTestBase {
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Test
    public void testCount() throws Exception {
        enableDebug();
        JobManager jobManager = createJobManager();
        jobManager.stop();
        for (int i = 0; i < 10; i++) {
            jobManager.addJob(new DummyJob(new Params(0)));
            MatcherAssert.assertThat(jobManager.count(), equalTo(i * 2 + 1));
            jobManager.addJob(new DummyJob(new Params(0)));
            MatcherAssert.assertThat(jobManager.count(), equalTo(i * 2 + 2));
        }
        final CountDownLatch jobsToRun = new CountDownLatch(20);
        jobManager.addCallback(new JobManagerCallbackAdapter() {
            @Override
            public void onAfterJobRun(@NonNull Job job, int resultCode) {
                if (resultCode == JobManagerCallback.RESULT_SUCCEED) {
                    jobsToRun.countDown();
                }
            }
        });
        jobManager.start();
        MatcherAssert.assertThat("test sanity", jobsToRun.await(1, TimeUnit.MINUTES), is(true));
        MatcherAssert.assertThat(jobManager.count(), equalTo(0));
    }
}
