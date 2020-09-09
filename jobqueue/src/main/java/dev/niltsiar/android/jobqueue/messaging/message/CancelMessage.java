package dev.niltsiar.android.jobqueue.messaging.message;

import dev.niltsiar.android.jobqueue.CancelResult;
import dev.niltsiar.android.jobqueue.TagConstraint;
import dev.niltsiar.android.jobqueue.messaging.Message;
import dev.niltsiar.android.jobqueue.messaging.Type;

public class CancelMessage extends Message {
    private TagConstraint constraint;
    private String[] tags;
    private CancelResult.AsyncCancelCallback callback;

    public CancelMessage() {
        super(Type.CANCEL);
    }

    @Override
    protected void onRecycled() {

    }

    public TagConstraint getConstraint() {
        return constraint;
    }

    public void setConstraint(TagConstraint constraint) {
        this.constraint = constraint;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public CancelResult.AsyncCancelCallback getCallback() {
        return callback;
    }

    public void setCallback(CancelResult.AsyncCancelCallback callback) {
        this.callback = callback;
    }
}
