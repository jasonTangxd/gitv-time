package cn.gitv.bi.realtime.ttl.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Kang on 2016/12/24.
 */
public class ThreadPoolTest {
    private static ExecutorService executorService = Executors.newFixedThreadPool(3);
    private static String[] testStr = {"1", "2", "3", "4", "5", "6", "7"};

    public static void main(String args[]) {
        for (String item : testStr) {
            executorService.submit(new TestTask(item));
        }
    }
}

class TestTask implements Runnable {
    String mac = null;

    public TestTask(String mac) {
        this.mac = mac;
    }

    @Override
    public void run() {
        System.out.println(this.mac);
    }
}
