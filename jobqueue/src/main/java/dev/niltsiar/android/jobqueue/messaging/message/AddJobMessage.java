package dev.niltsiar.android.jobqueue.messaging.message;

import dev.niltsiar.android.jobqueue.Job;
import dev.niltsiar.android.jobqueue.messaging.Message;
import dev.niltsiar.android.jobqueue.messaging.Type;

public class AddJobMessage extends Message {
    private Job job;
    public AddJobMessage() {
        super(Type.ADD_JOB);
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    protected void onRecycled() {
        job = null;
    }
}
