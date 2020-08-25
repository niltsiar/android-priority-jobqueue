package com.birbit.android.jobqueue;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;

public abstract class JobManagerThreadRunnable<T> {
    JobManager jobManager;
    AtomicBoolean running = new AtomicBoolean(false);
    public JobManagerThreadRunnable(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    private void assertRunning() {
        MatcherAssert.assertThat("can call this method only inside #onRun", running.get(),
                CoreMatchers.is(true));
    }

    protected JobHolder getNextJob() {
        assertRunning();
        return jobManager.jobManagerThread.getNextJobForTesting();
    }

    protected JobHolder getNextJob(Collection<String> exclude) {
        assertRunning();
        return jobManager.jobManagerThread.getNextJobForTesting(exclude);
    }

    protected void removeJob(JobHolder jobHolder) {
        assertRunning();
        jobManager.jobManagerThread.nonPersistentJobQueue.remove(jobHolder);
    }

    protected JobHolder findJobFromQueues(String id) {
        assertRunning();
        return jobManager.jobManagerThread.nonPersistentJobQueue.findJobById(id);
    }

    public T run() throws Throwable {
        final Object[] result = new Object[1];
        jobManager.internalRunInJobManagerThread(new Runnable() {
            @Override
            public void run() {
                try {
                    running.set(true);
                    result[0] = onRun();
                } finally {
                    running.set(false);
                }
            }
        });
        //noinspection unchecked
        return (T) result[0];
    }

    public abstract T onRun();
}
