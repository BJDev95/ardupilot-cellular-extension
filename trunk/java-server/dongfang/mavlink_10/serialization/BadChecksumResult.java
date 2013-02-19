package dongfang.mavlink_10.serialization;
import dongfang.mavlink_10.messages.MavlinkMessage;

public class BadChecksumResult extends MavlinkReceiveResult {
	private MavlinkMessage message;
	public BadChecksumResult(MavlinkMessage message) {
		this.message = message;
	}
	public MavlinkMessage getCorruptedMessage() {
		return message;
	}
	public String toString() {
		return "CORRUPTED (bad CRC) MESSAGE: " + message.toString();
	}
}
