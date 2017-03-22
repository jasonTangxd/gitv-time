package cn.gitv.bi.launcher.logclean.scheme;

import java.nio.ByteBuffer;
import java.util.List;

import cn.gitv.bi.launcher.beanparse.utils.StringHandle;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.spout.Scheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class NginxScheme implements Scheme {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1597691499377755124L;

	@Override
	public List<Object> deserialize(ByteBuffer ser) {
		String info = StringScheme.deserializeString(ser);
		return getNeedValues(info);
	}

	private Values getNeedValues(String info) {
		if (StringUtils.isNotBlank(info)) {
			List<String> contents = StringHandle.str_split(info);
			String ip = contents.get(0);
			String time = contents.get(1);
			String data = contents.get(2);
			return new Values(ip, time, data);
		}
		return new Values("", "", "");
	}

	@Override
	public Fields getOutputFields() {
		return new Fields("ip", "time", "data");
	}

}
