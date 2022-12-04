package dibuono.sturdywaddle.model;


import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NamedBeanCreationQueue implements Iterable<NamedBean> {
    public static NamedBeanCreationQueue EMPTY = new NamedBeanCreationQueue();
    private final CreationQueueStatus status;
    private final List<NamedBean> queue;

    public NamedBeanCreationQueue(){
        this.status = CreationQueueStatus.TO_INIT;
        this.queue = Collections.emptyList();
    }

    public NamedBeanCreationQueue(List<NamedBean> queue){
        this.queue = queue;
        this.status = CreationQueueStatus.READY;
    }
    
    public boolean isToInit(){
        return status == CreationQueueStatus.TO_INIT;
    }
    
    public int size(){
        return this.queue.size();
    }
    
    @Override
    public Iterator<NamedBean> iterator(){
        return new CreationQueueIterator();
    }
    
    private class CreationQueueIterator implements Iterator<NamedBean>{
        private int currentPos = -1;

        @Override
        public boolean hasNext() {
            return currentPos  < size() -1;
        }

        @Override
        public NamedBean next() {
            if(!hasNext()){
                throw new IndexOutOfBoundsException();
            }

            return queue.get(++currentPos);
        }
    }
    enum CreationQueueStatus{
        TO_INIT,
        READY
    }
}
