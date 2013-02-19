package dongfang.mavlink_10.serialization;

public class UnknownMessageReceivedResult extends MavlinkReceiveResult {
	private int messageId;
	public UnknownMessageReceivedResult(int messageId) {
		this.messageId = messageId;
	}
	public int getMessageId() {
		return messageId;
	}
	public String toString() {
		return "Unknown message Id: " + messageId;
	}
}
