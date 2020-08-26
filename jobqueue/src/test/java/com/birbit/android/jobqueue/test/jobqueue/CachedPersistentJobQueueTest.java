package com.birbit.android.jobqueue.test.jobqueue;

import androidx.test.core.app.ApplicationProvider;
import com.birbit.android.jobqueue.JobQueue;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.persistentQueue.sqlite.SqliteJobQueue;
import com.birbit.android.jobqueue.test.util.JobQueueFactory;
import com.birbit.android.jobqueue.timer.Timer;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)

public class CachedPersistentJobQueueTest extends JobQueueTestBase {
    public CachedPersistentJobQueueTest() {
        super(new JobQueueFactory() {
            @Override
            public JobQueue createNew(long sessionId, String id, Timer timer) {
                SqliteJobQueue.JavaSerializer jobSerializer = new SqliteJobQueue.JavaSerializer();
                return new SqliteJobQueue(new Configuration.Builder(ApplicationProvider.getApplicationContext()).id(id)
                                                                                                                .jobSerializer(jobSerializer)
                                                                                                                .inTestMode()
                                                                                                                .timer(timer)
                                                                                                                .build(), sessionId, jobSerializer);
            }
        });
    }
}
