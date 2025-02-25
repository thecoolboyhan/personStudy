package Concurrency.StructuredTaskScope;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Stream;

public class CollectingScope<T> extends StructuredTaskScope<T> {
    private final Queue<Subtask<?extends T>> successSubtasks=new LinkedTransferQueue<>();
    private final Queue<Subtask<?extends T>> failedSubtasks=new LinkedTransferQueue<>();

    @Override
    protected void handleComplete(Subtask<? extends T> subtask) {
        if(subtask.state()==Subtask.State.SUCCESS) successSubtasks.add(subtask);
        else if (subtask.state()==Subtask.State.FAILED) failedSubtasks.add(subtask);
    }

    @Override
    public StructuredTaskScope<T> join() throws InterruptedException {
        super.join();
        return this;
    }
    public Stream<Subtask<? extends T>> successfulTasks(){
        super.ensureOwnerAndJoined();
        return successSubtasks.stream();
    }

    public Stream<Subtask<? extends T>> failedTasks(){
        super.ensureOwnerAndJoined();
        return failedSubtasks.stream();
    }
}
