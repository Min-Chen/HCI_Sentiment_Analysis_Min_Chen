package app.concurrent;
import java.util.HashMap;

/**
 * A read/write lock that allows multiple readers, disallows multiple writers, and allows a writer to
 * acquire a read lock while holding the write lock.
 *
 */
public class ReentrantLock {
    private HashMap<Long, Integer> readerMap;
    private int writers;
    private long writerThreadID;

    /**
     * Construct a new ReentrantLock.
     */
    public ReentrantLock() {
        readerMap = new HashMap<>();
        writers = 0;
        writerThreadID = -1;
    }

    /**
     * Returns true if the invoking thread holds a read lock.
     * @return
     */
    public synchronized boolean hasRead() {
        return readerMap.containsKey(Thread.currentThread().getId());
    }

    /**
     * Returns true if the invoking thread holds a write lock.
     * @return
     */
    public synchronized boolean hasWrite() {
        // verify that the thread that is calling the method is holding write lock.
        if (writers > 0 && writerThreadID == Thread.currentThread().getId()) {
            return true;
        }
        return false;
    }

    /**
     * Non-blocking method that attempts to acquire the read lock.
     * Returns true if successful.
     * @return
     */
    public synchronized boolean tryLockRead() {
        if (writers > 0 && writerThreadID != Thread.currentThread().getId()) {
            return false;
        }
        long currReaderThreadId = Thread.currentThread().getId();
        if (!readerMap.containsKey(currReaderThreadId)) {
            readerMap.put(currReaderThreadId, 1);
        }
        else {
            readerMap.put(currReaderThreadId, readerMap.get(Thread.currentThread().getId()) + 1);
        }
        return true;
    }


    /**
     * Non-blocking method that attempts to acquire the write lock.
     * Returns true if successful.
     * @return
     */
    public synchronized boolean tryLockWrite() {
        if (writers > 0 && writerThreadID != Thread.currentThread().getId()) {
            return false;
        }

        // When this thread is holding both read and write lock,
        // it should be given another write lock.
        else if (writerThreadID == Thread.currentThread().getId() &&
                readerMap.size() == 1 && readerMap.containsKey(Thread.currentThread().getId())) {
            writers++;
            return true;
        }

        else if (!readerMap.isEmpty()) {
            return false;
        }
        writers++;
        writerThreadID = Thread.currentThread().getId();
        return true;
    }

    /**
     * Blocking method that will return only when the read lock has been
     * acquired.
     */
    public synchronized void lockRead() {
        try {
            while (!tryLockRead()) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Releases the read lock held by the calling thread. Other threads may continue
     * to hold a read lock.
     */
    public synchronized void unlockRead() {

//TODO: verify the caller holds the read lock.?
        long currReaderThreadId = Thread.currentThread().getId();
        // use hasRead method to check whether this thread is holding read lock or not.
        if (hasRead()) {
            if (readerMap.get(currReaderThreadId) == 1) {
                readerMap.remove(currReaderThreadId);
                //This condition shoule be invoked only when the lock is given up.
                if (readerMap.isEmpty()) {
                    notifyAll();
                }
            }
            else {
                readerMap.put(currReaderThreadId, readerMap.get(currReaderThreadId) - 1);
            }
        }
    }

    /**
     * Blocking method that will return only when the write lock has been
     * acquired.
     */
    public synchronized void lockWrite() {
        try {
            while(!tryLockWrite()) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Releases the write lock held by the calling thread. The calling thread may continue to hold
     * a read lock.
     */

    public synchronized void unlockWrite() {

//TODO: verify the write lock is held by the thread invoking this method.
        //use hasWrite() method to check whether this thread is holding write lock or not.
        if (hasWrite()) {
            writers--;
            if (writers == 0) {
                writerThreadID = -1;
                notifyAll();
            }
        }
    }
}
