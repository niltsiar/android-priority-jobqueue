package com.birbit.android.jobqueue.test.jobmanager;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.di.DependencyInjector;
import com.birbit.android.jobqueue.test.jobs.DummyJob;
import java.util.concurrent.atomic.AtomicInteger;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(RobolectricTestRunner.class)

public class InjectorTest extends JobManagerTestBase {
    @Test
    public void testInjector() throws Throwable {
        Configuration.Builder builder = new Configuration.Builder(RuntimeEnvironment.application);
        final AtomicInteger injectionCallCount = new AtomicInteger(0);
        DependencyInjector dependencyInjector = new DependencyInjector() {
            @Override
            public void inject(Job job) {
                injectionCallCount.incrementAndGet();
            }
        };
        builder.injector(dependencyInjector);
        builder.timer(mockTimer);
        JobManager jobManager = createJobManager(builder);
        jobManager.stop();
        jobManager.addJob(new DummyJob(new Params(4)));
        MatcherAssert.assertThat("injection should be called after adding a job", injectionCallCount.get(), equalTo(1));
        nextJob(jobManager);
        MatcherAssert.assertThat("injection should be called once for job", injectionCallCount.get(), equalTo(1));
    }
}
