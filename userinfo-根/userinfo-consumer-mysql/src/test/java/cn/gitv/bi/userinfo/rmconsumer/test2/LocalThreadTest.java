package cn.gitv.bi.userinfo.rmconsumer.test2;

import java.util.concurrent.TimeUnit;

class SafeTask implements Runnable {
    public ThreadLocal<Integer> i = new ThreadLocal<Integer>();

    public SafeTask() {
        i=initStartDate();
        System.out.println("firstly:"+i.get());
    }

    private ThreadLocal<Integer> initStartDate() {
        i.set(1);
        System.out.println("after set:"+i.get());
        return i;
    }

    @Override
    public void run() {
        int name = i.get();
        System.out.printf("starting %s,i is %s;\n", Thread.currentThread().getName(),name);
        System.out.println(i);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int result = i.get();
        i.set(result);
        System.out.printf("Finished: %s : %s\n", Thread.currentThread().getName(), i.get());
    }

}


public class LocalThreadTest {

    public static void main(String[] args) {
        SafeTask ut = new SafeTask();
        Thread t = new Thread(ut);
//        t.run();
        t.start();
    }
}