package com.birbit.android.jobqueue.test.jobmanager;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.test.jobs.DummyJob;
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
