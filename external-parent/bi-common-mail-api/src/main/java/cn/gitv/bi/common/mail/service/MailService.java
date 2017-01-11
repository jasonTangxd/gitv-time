package cn.gitv.bi.common.mail.service;



import cn.gitv.bi.common.mail.service.model.Email;

import javax.mail.MessagingException;
import java.io.Serializable;

/**
 * Created by ADMIN on 2016/8/30.
 */
public interface MailService extends Serializable {

    boolean sendMail(Email email) throws RuntimeException, MessagingException;
}
