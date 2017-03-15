package cn.gitv.bi.external.khloader.utils;

import org.apache.hadoop.io.BytesWritable;

/**
 * Created by Kang on 2016/12/20.
 */
public class MapInputParse {
    public static String bytesWritable2String(BytesWritable value) {
        return new String(value.getBytes(), 0, value.getLength());
    }
}
