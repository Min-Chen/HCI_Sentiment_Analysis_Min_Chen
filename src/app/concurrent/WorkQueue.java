package app.concurrent;
import java.util.LinkedList;

public class WorkQueue {
    /** The default number of threads to use when not specified. */
    public static final int nThreads = 10;

    /** Pool of worker threads that will wait in the background until work is available. */
    private final PoolWorker[] threads;

    /** Queue of pending work requests. */
    private final LinkedList<Runnable> queue;

    /** Used to signal the queue should be shutdown. */
    private volatile boolean shutdown;


    /**
     * Construct a WorkQueue with 10 default workers.
     */
    public WorkQueue() {
        this(nThreads);
    }

    /**
     * Starts a work queue with the specified number of threads.
     *
     * @param nThreads: number of worker threads; should be greater than 1
     */
    public WorkQueue(int nThreads)
    {
        if (nThreads <= 0) nThreads = this.nThreads;
        this.threads = new PoolWorker[nThreads];
        this.queue = new LinkedList();

        // start the threads so they are waiting in the background
        for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    /**
     * Execute a new Runnable job.
     * @param r
     */
    public void execute(Runnable r) {
        synchronized(queue) {
            if (!shutdown) {
                queue.addLast(r);
                queue.notify();
            }
        }
    }

    /**
     * Stop accepting new jobs.
     * This method should not block until work is complete.
     */
    public void shutdown() {
        shutdown = true;

        synchronized (queue) {
            queue.notifyAll();
        }
    }

    /**
     * Block until all jobs in the queue are complete.
     */
    public void awaitTermination() {
        // iterate every thread in the threadpool, do a join().
        for (PoolWorker pw: this.threads) {
            try {
                pw.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class PoolWorker extends Thread {
        @Override
        public void run() {
            Runnable r = null;

            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty() && !shutdown) {
                        try {
                            queue.wait();
                        }
                        catch (InterruptedException ex) {
                            System.err.println("Warning: Work queue interrupted " +
                                    "while waiting.");
                            Thread.currentThread().interrupt();
                        }
                    }

                    if (shutdown && queue.isEmpty()) {
                        break;
                    }

                    if (!queue.isEmpty()) {
                        r = queue.removeFirst();
                    }
                }

                try {
                    r.run();
                }
                catch (RuntimeException ex) {
                    System.err.println("Warning: Work queue encountered an " +
                            "exception while running.");
                    ex.printStackTrace();
                }
            }
        }
    }
}
