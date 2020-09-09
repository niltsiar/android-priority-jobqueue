package dev.niltsiar.android.jobqueue.test.jobqueue;

import dev.niltsiar.android.jobqueue.JobQueue;
import dev.niltsiar.android.jobqueue.inMemoryQueue.SimpleInMemoryPriorityQueue;
import dev.niltsiar.android.jobqueue.test.util.JobQueueFactory;
import dev.niltsiar.android.jobqueue.timer.Timer;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)

public class SimpleInMemoryJobQueueTest extends JobQueueTestBase {
    public SimpleInMemoryJobQueueTest() {
        super(new JobQueueFactory() {
            @Override
            public JobQueue createNew(long sessionId, String id, Timer timer) {
                return new SimpleInMemoryPriorityQueue(null, sessionId);
            }
        });
    }
}
