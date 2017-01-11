package cn.gitv.bi.userinfo.rmconsumer.test1;

/**
 * Created by Kang on 2017/1/10.
 */
public class ThreadDemo {
    public static ThreadLocal<Person> threadLocal = new ThreadLocal() {
        @Override
        protected Person initialValue() {
            return new Person();
        }

        @Override
        public Person get() {
            return threadLocal.get();
        }
    };

    public static Person get() {
        return threadLocal.get();
    }

}
