package dongfang.mavlink_10.serialization;
import dongfang.mavlink_10.messages.MavlinkMessage;

public class MessageReceivedResult extends MavlinkReceiveResult {
	private MavlinkMessage message;
	public MessageReceivedResult(MavlinkMessage message) {
		this.message = message;
	}
	public MavlinkMessage getMessage() {
		return message;
	}
	public String toString() {
		return message.toString();
	}
}
