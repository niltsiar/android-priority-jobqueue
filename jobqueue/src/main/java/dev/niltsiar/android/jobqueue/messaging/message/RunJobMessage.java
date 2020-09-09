package dev.niltsiar.android.jobqueue.messaging.message;

import dev.niltsiar.android.jobqueue.JobHolder;
import dev.niltsiar.android.jobqueue.messaging.Message;
import dev.niltsiar.android.jobqueue.messaging.Type;

public class RunJobMessage extends Message {
    private JobHolder jobHolder;
    public RunJobMessage() {
        super(Type.RUN_JOB);
    }

    public JobHolder getJobHolder() {
        return jobHolder;
    }

    public void setJobHolder(JobHolder jobHolder) {
        this.jobHolder = jobHolder;
    }

    @Override
    protected void onRecycled() {
        jobHolder = null;
    }
}
