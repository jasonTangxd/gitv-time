package cn.gitv.bi.launcher.logclean.tokafka;

import org.apache.commons.lang3.StringUtils;
import org.apache.storm.kafka.bolt.selector.KafkaTopicSelector;
import org.apache.storm.tuple.Tuple;
import static cn.gitv.bi.launcher.beanparse.constant.Constant.topicPrefix;

public class ActionKafkaTopicSelector implements KafkaTopicSelector {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7757775692778129939L;


	public String getTopic(Tuple tuple) {
		String Action = tuple.getStringByField("Action");
		if (StringUtils.isNotBlank(Action)) {
			return new StringBuffer(topicPrefix).append(Action).toString();
		}
		return null;
	}

}
