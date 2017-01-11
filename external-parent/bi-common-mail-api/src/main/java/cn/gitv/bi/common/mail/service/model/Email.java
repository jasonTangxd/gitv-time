package cn.gitv.bi.common.mail.service.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ADMIN on 2016/8/31.
 */
public class Email implements Serializable{
    private static final long serialVersionUID = -4265186154873031600L;
    public int priority = 3;
    String from = null;
    /** 邮件主题 **/
    private String subject;
    List<String> to = new ArrayList<>();
    List<String> cc = new ArrayList<>();
    List<String> bcc = new ArrayList<>();
    String content =null;
    Set<File> files = new HashSet<>();

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files.addAll(files);
    }
    public void addFile(File file){
        this.files.add(file);
    }

    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to.addAll(to);
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
