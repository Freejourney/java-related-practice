package com.ddw.javarelatedpractice.thread.join;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlightQueryExample {

    private static final List<String> FLIGHTCOMPANY = new ArrayList<String>(){
        {
            add("CSA");
            add("CEA");
            add("HNA");
        }
    };

    /**
     * FlightQueryTask是继承自Thread实现了FlightQuery接口的线程
     *
     * 可以看到join阻塞结束之后，也就是线程已经TERMINATED了，这个时候使用get方法依然是能够获取到线程所拥有的的成员变量的
     * @param origin
     * @param destination
     * @return
     */
    private static List<String> search(String origin, String destination) {
        final List<String> result = new ArrayList<>();

        // 创建查询航班信息的线程列表
        List<FlightQueryTask> tasks = FLIGHTCOMPANY.stream().map(f -> createSearchTask(f, origin, destination)).collect(Collectors.toList());

        // 启动线程
        tasks.forEach(Thread::start);

        // 阻塞主线程
        tasks.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {

            }
        });

        // 汇总结果
        tasks.stream().map(FlightQuery::get).forEach(result::addAll);

        return result;
    }

    private static FlightQueryTask createSearchTask(String flight, String origin, String destination) {
        return new FlightQueryTask(flight, origin, destination);
    }

    public static void main(String[] args) {
        List<String> results = search("SH", "BJ");
        System.out.println("======RESULT======");
        results.forEach(System.out::println);
    }
}
