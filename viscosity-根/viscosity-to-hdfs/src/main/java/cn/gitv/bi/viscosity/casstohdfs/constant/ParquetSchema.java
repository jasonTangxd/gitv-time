package cn.gitv.bi.viscosity.casstohdfs.constant;

import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.OriginalType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Types;

/**
 * Created by Kang on 2016/12/8.
 */
public interface ParquetSchema {
    MessageType VodUser_SCHEMA = Types.buildMessage().required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("partner")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("logdate")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("mac")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("srcname")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("chnname")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("chnId")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("albumname")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("albumId")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("playorder")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("videoId")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("click_num")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("playlength")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("timelength")
            .named("VodUser");

    MessageType VodProgram_SCHEMA = Types.buildMessage().required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("partner")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("logdate")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("srcname")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("chnname")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("chnId")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("albumname")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("albumId")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("playorder")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("videoId")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("province")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("city")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("click_num")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("playlength")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("timelength")
            .named("VodProgram");

    MessageType LivProgram_SCHEMA = Types.buildMessage().required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("partner")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("logdate")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("srcname")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("chncode")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("albumname")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("playorder")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("province")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("city")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("click_num")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("playlength")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("timelength")
            .named("LivProgram");

    MessageType LivUser_SCHEMA = Types.buildMessage().required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("partner")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("logdate")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("mac")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("srcname")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("chncode")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("albumname")
            .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("playorder")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("click_num")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("playlength")
            .required(PrimitiveType.PrimitiveTypeName.INT64).named("timelength")
            .named("LivUser");
}
