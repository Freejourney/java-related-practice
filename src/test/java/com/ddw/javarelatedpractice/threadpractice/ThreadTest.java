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


    @Test
    public void priorityTest() {
        ThreadGroup group
    }

}
