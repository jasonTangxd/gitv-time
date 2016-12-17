package cn.gitv.bi.launcher.beanparse.apkparse;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Created by Kang on 2016/11/26.
 */
public class ApkParseUtils {
    /**
     * 解析apk->json后的标准json字段-->ApkBean
     */
    public static ApkBean jsonToBean(String json) {
        ApkBean apkBean = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> maps = objectMapper.readValue(json, Map.class);
            apkBean = new ApkBean();
            for (Map.Entry<String, Object> item : maps.entrySet()) {
                String key = item.getKey();
                Object value = item.getValue();
                if ("mAction".equals(key)) {
                    apkBean.setmAction((String) item.getValue());
                }
                if ("mPackage".equals(key)) {
                    apkBean.setmPackage((String) item.getValue());
                }
                if ("mComponent".equals(key)) {
                    Map<String, String> mcomponent_mp = (Map) value;
                    apkBean.setCls(mcomponent_mp.get("mClass"));
                    apkBean.setPkg(mcomponent_mp.get("mPackage"));
                }
                if ("mExtras".equals(key)) {
                    Map<String, Map<String, String>> mextras_map = (Map) value;
                    Map<String, String> extras = mextras_map.get("mMap");
                    apkBean.setAlbumId(extras.get("albumId"));
                    apkBean.setChnId(extras.get("chnId"));
                    apkBean.setCpId(extras.get("cpId"));
                    apkBean.setType(extras.get("enumtype"));
                    apkBean.setCpContentId(extras.get("cpContentId"));
                    apkBean.setTopicLayout(extras.get("topicLayout"));
                    apkBean.setTopicPicBg(extras.get("topicPicBg"));
                    apkBean.setChnName(extras.get("chnName"));
                    apkBean.setCurrTypingCode(extras.get("currTypingCode"));
                    apkBean.setCurrTypingName(extras.get("currTypingName"));
                    apkBean.setTypings(extras.get("typings"));
                    apkBean.setNum(extras.get("num"));
                    apkBean.setTvId(extras.get("tvId"));
                    apkBean.setTagId(extras.get("tagId"));
                    apkBean.setPageName(extras.get("pageName"));
                    apkBean.setScreenInfo(extras.get("screenInfo"));
                    apkBean.setPicUrl(extras.get("picUrl"));
                }
            }
        } catch (Exception e) {
//            log.error("ApkParseUtils ERROR josn解析异常-->{}", e.getMessage()); 因为首次测试数据大多数没有改为''用的是\x22，所以pass
        }
        return apkBean;
    }
}
