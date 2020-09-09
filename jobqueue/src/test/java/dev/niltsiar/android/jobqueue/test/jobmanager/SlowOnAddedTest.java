package dev.niltsiar.android.jobqueue.test.jobmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dev.niltsiar.android.jobqueue.CancelReason;
import dev.niltsiar.android.jobqueue.Job;
import dev.niltsiar.android.jobqueue.JobManager;
import dev.niltsiar.android.jobqueue.Params;
import dev.niltsiar.android.jobqueue.RetryConstraint;
import dev.niltsiar.android.jobqueue.test.jobs.DummyJob;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)

public class SlowOnAddedTest extends JobManagerTestBase {
    @Test
    public void testNonPersistent() throws InterruptedException {
        JobManager jobManager = createJobManager();
        CountDownLatch runLatch = new CountDownLatch(1);
        MyDummyJob job = new MyDummyJob(new Params(2), runLatch);
        for(int i = 0; i < 50; i++) {
            jobManager.addJob(new DummyJob(new Params(1)));
        }
        jobManager.addJob(job);
        runLatch.await();
        assertThat("on added should be called before on run", job.onAddedCntWhenRun, equalTo(1));
    }

    @Test
    public void testPersistent() throws InterruptedException {
        JobManager jobManager = createJobManager();
        MyDummyPersistentJob.persistentJobLatch = new CountDownLatch(1);
        for(int i = 0; i < 50; i++) {
            jobManager.addJob(new DummyJob(new Params(1)));
        }
        jobManager.addJob(new MyDummyPersistentJob(2));
        MyDummyPersistentJob.persistentJobLatch.await();
        assertThat("even if job is persistent, onAdded should be called b4 onRun",
                MyDummyPersistentJob.onAddedCountWhenOnRun, equalTo(1));
    }

    public static class MyDummyPersistentJob extends Job {
        private static CountDownLatch persistentJobLatch;
        private static int persistentOnAdded = 0;
        private static int onAddedCountWhenOnRun = -1;

        protected MyDummyPersistentJob(int priority) {
            super(new Params(priority));
        }

        @Override
        public void onAdded() {
            try {
                //noinspection SLEEP_IN_CODE
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                //
            }
            persistentOnAdded ++;
        }

        @Override
        public void onRun() throws Throwable {
            onAddedCountWhenOnRun = persistentOnAdded;
            persistentJobLatch.countDown();
        }

        @Override
        protected void onCancel(@CancelReason int cancelReason, @Nullable Throwable throwable) {

        }

        @Override
        protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
            return RetryConstraint.RETRY;
        }
    }

    private static class MyDummyJob extends DummyLatchJob {
        int onAddedCntWhenRun = -1;

        protected MyDummyJob(Params params, CountDownLatch latch) {
            super(params, latch);
        }

        @Override
        public void onAdded() {
            try {
                //noinspection SLEEP_IN_CODE
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            super.onAdded();
        }

        @Override
        public void onRun() throws Throwable {
            onAddedCntWhenRun = super.getOnAddedCnt();
            super.onRun();
        }
    }
}
