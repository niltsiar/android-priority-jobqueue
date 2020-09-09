package dev.niltsiar.android.jobqueue.messaging;

import dev.niltsiar.android.jobqueue.timer.Timer;

public class SafeMessageQueueTest extends MessageQueueTestBase<SafeMessageQueue> {

    @Override
    SafeMessageQueue createMessageQueue(Timer timer, MessageFactory factory) {
        return new SafeMessageQueue(timer, factory, "test");
    }
}
