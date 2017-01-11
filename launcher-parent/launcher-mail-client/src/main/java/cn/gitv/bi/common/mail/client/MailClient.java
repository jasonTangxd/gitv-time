package cn.gitv.bi.common.mail.client;

import cn.gitv.bi.common.mail.service.MailService;
import cn.gitv.bi.common.mail.service.model.Email;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 2016/9/1.
 */
public class MailClient {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:conf/mail-remote-context.xml");
        MailService mailService = (MailService) ctx.getBean("motanDemoReferer");
        Email mail = new Email();
        List<String> tos = new ArrayList<>();
        tos.add("2596348420@qq.com");
        mail.setTo(tos);
       /* for (int i = 0; i < 0; i++) {
            File f = new File("E:\\ah_dat\\观看记录\\20160630_gitv_ottuserviewdata_vod.dat");
            mail.addFile(f);
        }*/
        try {
            mail.setContent("hello");
            mail.setSubject("hello");
            System.out.println("*************************************" + System.currentTimeMillis());
            mailService.sendMail(mail);
            System.out.println("*************************************" + System.currentTimeMillis());
            System.exit(0);
        } catch (Exception e) {
            System.out.println("*************************************" + System.currentTimeMillis());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
