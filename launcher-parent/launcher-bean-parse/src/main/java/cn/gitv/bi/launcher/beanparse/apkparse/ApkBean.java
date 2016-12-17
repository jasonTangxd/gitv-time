package cn.gitv.bi.launcher.beanparse.apkparse;


import static cn.gitv.bi.launcher.beanparse.constant.Constant.LOGBEAN_SEPARATOR;

/**
 * Created by Kang on 2016/11/26.
 */
public class ApkBean {
    private String mAction;
    private String mPackage;
    //class Component
    private String cls;
    private String pkg;
    //class Extras
    private String albumId;
    private String chnId;
    private String cpId;
    private String type;
    private String cpContentId;
    private String topicLayout;
    private String topicPicBg;
    private String chnName;
    private String currTypingCode;
    private String currTypingName;
    private String typings;
    private String num;
    private String tvId;
    private String tagId;
    private String pageName;
    private String screenInfo;
    private String picUrl;
    //how to init activity
    private String initType;
    private String initData;

    public void setmAction(String mAction) {
        this.mAction = mAction;
    }

    public void setmPackage(String mPackage) {
        this.mPackage = mPackage;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setChnId(String chnId) {
        this.chnId = chnId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCpContentId(String cpContentId) {
        this.cpContentId = cpContentId;
    }

    public void setTopicLayout(String topicLayout) {
        this.topicLayout = topicLayout;
    }

    public void setTopicPicBg(String topicPicBg) {
        this.topicPicBg = topicPicBg;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public void setCurrTypingCode(String currTypingCode) {
        this.currTypingCode = currTypingCode;
    }

    public void setCurrTypingName(String currTypingName) {
        this.currTypingName = currTypingName;
    }

    public void setTypings(String typings) {
        this.typings = typings;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setTvId(String tvId) {
        this.tvId = tvId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setScreenInfo(String screenInfo) {
        this.screenInfo = screenInfo;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        if (mAction != null) {
            //隐式调用
            initType = "implicit";
            initData = mAction;
        } else {
            //显式调用
            initType = "explicit";
            //优先使用mComponent的信息
            if (cls != null) {
                //cls中包含pkg，pkg可不用
                initData = cls;
            } else {
                initData = mPackage;
            }
        }
        final StringBuilder sb = new StringBuilder();
        //19个字段
        sb.append(initType).append(LOGBEAN_SEPARATOR).append(initData).append(LOGBEAN_SEPARATOR).append(albumId).append(LOGBEAN_SEPARATOR).append(chnId).append(LOGBEAN_SEPARATOR)
                .append(cpId).append(LOGBEAN_SEPARATOR).append(type).append(LOGBEAN_SEPARATOR).append(cpContentId).append(LOGBEAN_SEPARATOR).append(topicLayout)
                .append(LOGBEAN_SEPARATOR).append(topicPicBg).append(LOGBEAN_SEPARATOR).append(chnName).append(LOGBEAN_SEPARATOR).append(currTypingCode).append(LOGBEAN_SEPARATOR)
                .append(currTypingName).append(LOGBEAN_SEPARATOR).append(typings).append(LOGBEAN_SEPARATOR).append(num).append(LOGBEAN_SEPARATOR).append(tvId)
                .append(LOGBEAN_SEPARATOR).append(tagId).append(LOGBEAN_SEPARATOR).append(pageName).append(LOGBEAN_SEPARATOR).append(screenInfo).append(LOGBEAN_SEPARATOR).
                append(picUrl).append(LOGBEAN_SEPARATOR);
        return sb.toString();
    }

}
