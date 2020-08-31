package com.birbit.android.jobqueue;

import com.birbit.android.jobqueue.config.Configuration;

public interface QueueFactory {

    JobQueue createNonPersistent(Configuration configuration, long sessionId);
}
