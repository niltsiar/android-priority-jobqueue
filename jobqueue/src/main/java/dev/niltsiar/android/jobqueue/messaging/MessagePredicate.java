package dev.niltsiar.android.jobqueue.messaging;

public interface MessagePredicate {
    boolean onMessage(Message message);
}
