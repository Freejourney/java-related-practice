package com.ddw.javarelatedpractice.threadpractice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootTest
public class ThreadTest {

    /**
     * long ms 2_000L sleep;
     * Thread.currentThread().getName();
     * String.format("xxx%d/s..", xxx);
     */
    @Test
    public void sleepTestandNameTest() {

        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            sleep(2_000L);
            long endTime = System.currentTimeMillis();
            System.out.println(String.format("Total spend %d ms", endTime-startTime));

            System.out.println(String.format("Thread Name is : %s", Thread.currentThread().getName()));
        }).start();

        long startTime = System.currentTimeMillis();
        sleep(3_000L);
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Main thread total spend %d ms", endTime-startTime));
        System.out.println(String.format("Thread Name is : %s", Thread.currentThread().getName()));
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }


    /**
     * TimeUnit.HOURS/MINUTES/SECONDS/MILLSECONDS
     */
    @Test
    public void timeUnitTest() {
        try {
            long startTime = System.currentTimeMillis();
            TimeUnit.HOURS.sleep(1);
            TimeUnit.MINUTES.sleep(1);
            TimeUnit.SECONDS.sleep(2);
            TimeUnit.MILLISECONDS.sleep(100);
            long endTime = System.currentTimeMillis();
            System.out.println(String.format("%s thread total spend %d ms", Thread.currentThread().getName(), endTime-startTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    /**
     * IntStream.range(0, 2).mapToObj(xxx).forEach(xxx);
     * yield() --> yield并不每次都执行，执行情况取决于CPU状态
     */
    @Test
    public void yieldTest() {
        IntStream.range(0, 2).mapToObj(ThreadTest::create).forEach(Thread::start);
    }

    private static Thread create(int index) {
        return new Thread(() -> {
            if (index == 0) {
                Thread.yield();
            }
            System.out.println(index);
        });
    }



    /**
     * ThreadGroup, Priority
     * 默认情况下线程的优先级与父线程一致，也就是与main线程一致，为5;
     */
    @Test
    public void priorityTest() {
        // 定义一个线程组
        ThreadGroup group = new ThreadGroup("test");
        // 将线程组的优先级设定为7
        group.setMaxPriority(7);
        // 定义一个线程，将其加入到线程组中
        Thread thread = new Thread(group, "test-thread");
        // 企图将线程的优先级设成10
        thread.setPriority(10);
        // 企图失败
        System.out.println(String.format("the priority of thread : %d", thread.getPriority()));
    }


    /**
     * result:
     *      thread is interrrupted ? false
     *      I am be interrupted ? false
     *      thread is interrrupted ? false
     *
     * 从结果来看三次都是false，也就是未中断。
     *
     * 为什么呢？为什么不是未中断，中断，中断呢？
     *
     * 其实是有中断的，中断标志位变成true的时间段在interrupt()方法调用之后，抛出InterruptException异常之前。
     * 也就是说本来是中断了的，但是在抛出异常之前就被消除了，这也是为了保证线程下一次中断能够正常工作，不产生影响。
     *
     *
     * 可中断方法才有这种特性，也就是能够抛InterruptedException异常的方法才会有这种复位的特性，这里的这个异常
     * 是sleep方法抛出来的。
     *
     * 线程在阻塞状态下被打断都会抛出InterruptedException异常。
     * @throws InterruptedException
     */
    @Test
    public void interrupteTest1() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                // ignore the exception
                // here the interrupt flag will be clear.
                System.out.println(String.format("I am be interrupted ? %s", Thread.currentThread().isInterrupted())); // 中断复位后
            }
        });

        thread.setDaemon(true);
        thread.start();

        TimeUnit.MILLISECONDS.sleep(200);

        // 中断前
        System.out.println(String.format("thread is interrrupted ? %s", thread.isInterrupted()));

        // 中断
        thread.interrupt();

        // 中断后
        System.out.println(String.format("thread is interrrupted ? %s", thread.isInterrupted()));
    }



    /**
     *  在执行可中断方法(sleep())之前中断线程不会影响线程正常执行，但是在执行到sleep的时候会立马中断
     */
    @Test
    public void interrupteTest2() {
        // 判断当前线程是否被中断（中断标志是否是true）
        System.out.println("Current thread : " + Thread.currentThread().getName() +" is interrupted? " + Thread.currentThread().isInterrupted());
//        与上面代码等价，区别是interrupted()是个static方法, 进去之后可以看到：
//              return currentThread().isInterrupted(true);
//        同样是获取当前线程判断是否中断，需要注意的是这里参数是true，默认不加的时候是false，false表示仅判断，true表示判断完了之后擦除中断标志也就是置为false
//        System.out.println("Current thread : " + Thread.currentThread().getName() +" is interrupted? " + Thread.interrupted());

        // 中断当前线程（中断标志设置成true）
        Thread.currentThread().interrupt();

        // 判断当前线程是否被中断（中断标志是否是true）
        System.out.println("Current thread : " + Thread.currentThread().getName() +" is interrupted? " + Thread.currentThread().isInterrupted());

        try {
            long startTime = System.currentTimeMillis();
            System.out.println("startTime is " + startTime);
            // 直接抛异常，后面的代码不会执行
            TimeUnit.SECONDS.sleep(1);
            long endTime = System.currentTimeMillis();
            System.out.println("endTime is " + endTime);
        } catch (InterruptedException e) {
            // 直接打印出来的，时间跟startTime相同
            System.out.println("Thread is interrupted! The exception catched time is " + System.currentTimeMillis());
        }

    }

}
