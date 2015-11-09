package vandy.mooc.presenter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageOpsThreadPool {
    private static final int CORE_POOL_SIZE = 20;
    private static final int MAXIMUM_POOL_SIZE = 256;
    private static final int KEEP_ALIVE = 1;
    
    private static final ThreadFactory sThreadFactory = 
            new ThreadFactory() {
                private final AtomicInteger mCount = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "AsyncImageOpsWorker #" +
                            mCount.getAndDecrement());
                }
            };
    
    private static final BlockingQueue<Runnable> sPoolWorkQueue = 
            new LinkedBlockingQueue<Runnable>(MAXIMUM_POOL_SIZE);
    
    public static final Executor IMAGE_OPS_THREAD_POOL_EXECUTOR = 
            new ThreadPoolExecutor(CORE_POOL_SIZE,
                    MAXIMUM_POOL_SIZE,
                    KEEP_ALIVE,
                    TimeUnit.SECONDS,
                    sPoolWorkQueue,
                    sThreadFactory);
}
