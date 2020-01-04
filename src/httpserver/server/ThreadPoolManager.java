package httpserver.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池管理(线程统一调度管理)
 * @author gwofchan
 */
public final class ThreadPoolManager {

    private static ThreadPoolExecutor pool= null;

    // 线程池维护线程的最少数量
    private static final int SIZE_CORE_POOL = 3;

    // 线程池维护线程的最大数量
    private static final int SIZE_MAX_POOL = 10;

/**
 * 线程池
 * @param corePoolSize - 池中所保存的线程数，包括空闲线程。
 * @param maximumPoolSize - 池中允许的最大线程数。
 * @param keepAliveTime - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
 * @param unit - keepAliveTime 参数的时间单位。
 * @param workQueue - 执行前用于保持任务的队列。此队列仅由保持 execute 方法提交的 Runnable 任务。
 * @param handler - 由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序。
 */
    /**
     * 线程池初始化方法
     *
     * corePoolSize 核心线程池大小----3
     * maximumPoolSize 最大线程池大小----10
     * keepAliveTime 线程池中超过corePoolSize数目的空闲线程最大存活时间----30+单位TimeUnit
     * TimeUnit keepAliveTime时间单位----TimeUnit.MINUTES
     * workQueue 阻塞队列----new ArrayBlockingQueue<Runnable>(5)====5容量的阻塞队列
     * threadFactory 新建线程工厂----new CustomThreadFactory()====定制的线程工厂
     * rejectedExecutionHandler 当提交任务数超过maxmumPoolSize+workQueue之和时,
     *                          即当提交第41个任务时(前面线程都没有执行完,此测试方法中用sleep(100)),
     *                                任务会交给RejectedExecutionHandler来处理
     */
    /*
     * 线程池单例创建方法
     */
      public void init() {
          pool = new ThreadPoolExecutor(
                SIZE_CORE_POOL,
                SIZE_MAX_POOL,
                30,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(5),
                new PoolThreadFactory(),
                new PoolRejectedExecutionHandler());
    }

    /**************************************************************************************************************
     * 常见的几种线程技术
     **************************************************************************************************************
     * Java通过Executors提供四种线程池，分别为：
     * newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
     * newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     * newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。 newSingleThreadExecutor
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
     *
     * 1、public static ExecutorService newFixedThreadPool(int nThreads) {
     * return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()); }
     *
     * 2、 public static ExecutorService newSingleThreadExecutor() {
     * return new FinalizableDelegatedExecutorService (new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())); }
     *
     * 3、public static ExecutorService newCachedThreadPool() {
     * return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>()); }
     ****************************************************************************************************************/

    /*
     * 关闭线程池，不在接受新的任务，会把已接受的任务执行玩
     */
    public void shutdown() {
        if(pool != null) {
            pool.shutdownNow();
        }
    }


    public ExecutorService getThreadPoolExecutor() {
        return pool;
    }

    private static class PoolThreadFactory implements ThreadFactory {

        private AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String threadName = ThreadPoolExecutor.class.getSimpleName() + count.addAndGet(1);
            System.out.println(threadName);
            t.setName(threadName);
            return t;
        }
    }


    private static class PoolRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // 记录异常
            // 报警处理等
            System.out.println("error.............");
        }
    }




    /*
     * 判断是否是最后一个任务
     */
    protected boolean isTaskEnd() {
        if (pool.getActiveCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 获取缓存大小
     */
    public int getQueue(){
        return pool.getQueue().size();
    }

    /*
     * 获取线程池中的线程数目
     */
    public int getPoolSize(){
        return pool.getPoolSize();
    }

    /*
     * 获取已完成的任务数
     */
    public long getCompletedTaskCount(){
        return pool.getCompletedTaskCount();
    }



}