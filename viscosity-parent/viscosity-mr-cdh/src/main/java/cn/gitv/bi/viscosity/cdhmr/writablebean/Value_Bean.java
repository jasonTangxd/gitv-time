//package cn.gitv.bi.writablebean;
//
//import java.io.DataInput;
//import java.io.DataOutput;
//import java.io.IOException;
//
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.io.WritableComparable;
//
//public class Value_Bean implements WritableComparable<Value_Bean> {
//
//	private IntWritable play_length;
//	private Text record_date;
//	private IntWritable count = new IntWritable(1);
//
//	public IntWritable getCount() {
//		return count;
//	}
//
//	public IntWritable getPlay_length() {
//		return play_length;
//	}
//
//	public void setPlay_length(IntWritable play_length) {
//		this.play_length = play_length;
//	}
//
//	public Text getRecord_date() {
//		return record_date;
//	}
//
//	public void setRecord_date(Text record_date) {
//		this.record_date = record_date;
//	}
//
//	//
//	public Value_Bean() {
//		set(new IntWritable(), new Text());
//	}
//
//	public Value_Bean(IntWritable t1, Text t2) {
//		set(t1, t2);
//	}
//
//	public void set(IntWritable t1, Text t2) {
//		this.play_length = t1;
//		this.record_date = t2;
//	}
//
//	@Override
//	public void write(DataOutput out) throws IOException {
//		this.play_length.write(out);
//		this.record_date.write(out);
//		this.count.write(out);
//	}
//
//	@Override
//	public void readFields(DataInput in) throws IOException {
//		this.play_length.readFields(in);
//		this.record_date.readFields(in);
//		this.count.readFields(in);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//		if (obj instanceof Value_Bean) {
//			Value_Bean obj1 = (Value_Bean) obj;
//			return this.play_length.equals(obj1.play_length) && this.record_date.equals(obj1.record_date);
//		} else {
//			return false;
//		}
//	}
//
//	@Override
//	public int compareTo(Value_Bean o) {
//		int a = this.play_length.get() > o.play_length.get() ? 1
//				: (this.play_length.get() == o.play_length.get() ? 0 : -1);
//		if (a == 0) {
//			return this.record_date.toString().compareTo(o.record_date.toString());
//		}
//		return a;
//	}
//
//}
