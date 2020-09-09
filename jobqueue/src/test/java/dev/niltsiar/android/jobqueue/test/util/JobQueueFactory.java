package dev.niltsiar.android.jobqueue.test.util;

import dev.niltsiar.android.jobqueue.JobQueue;
import dev.niltsiar.android.jobqueue.timer.Timer;

public interface JobQueueFactory {
    JobQueue createNew(long sessionId, String id, Timer timer);
}
