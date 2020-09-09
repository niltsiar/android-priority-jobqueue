package dev.niltsiar.android.jobqueue;

import dev.niltsiar.android.jobqueue.cachedQueue.CachedJobQueue;
import dev.niltsiar.android.jobqueue.config.Configuration;
import dev.niltsiar.android.jobqueue.inMemoryQueue.SimpleInMemoryPriorityQueue;

/**
 * Default implementation of QueueFactory that creates one {@link SimpleInMemoryPriorityQueue}
 * which is wrapped inside a {@link CachedJobQueue} to
 * improve performance
 */
public class DefaultQueueFactory implements QueueFactory {

    @Override
    public JobQueue createNonPersistent(Configuration configuration, long sessionId) {
        return new CachedJobQueue(new SimpleInMemoryPriorityQueue(configuration, sessionId));
    }
}
