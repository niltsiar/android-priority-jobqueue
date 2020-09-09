package dev.niltsiar.android.jobqueue;

import dev.niltsiar.android.jobqueue.config.Configuration;

public interface QueueFactory {

    JobQueue createNonPersistent(Configuration configuration, long sessionId);
}
