package com.ddw.javarelatedpractice.threadpractice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class ThreadJoinTest {

    /**
     *  mapToObj(), box() == > Stream<xxx>
     */
    @Test
    public void joinTest() throws InterruptedException {
        // 定义两个线程，并保存在threads中
        List<Thread> threads = IntStream.range(1, 3).mapToObj(ThreadJoinTest::createThread).collect(Collectors.toList());
        // 启动线程
        threads.forEach(Thread::start);

        // join阻塞的是执行join命令的当前线程
        for (Thread thread : threads) {
            thread.join();
        }

        // main循环输出
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "#" + i);
            shortSleep();
        }
    }

    private static Thread createThread(int seq) {
        return new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "#" + i);
                shortSleep();
            }
        }, String.valueOf(seq));
    }

    private static void shortSleep() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }






}
