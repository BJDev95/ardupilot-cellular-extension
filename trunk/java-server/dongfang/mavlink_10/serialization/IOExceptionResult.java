package dongfang.mavlink_10.serialization;
import java.io.IOException;

public class IOExceptionResult extends MavlinkReceiveResult {
	private IOException exception;
	public IOExceptionResult(IOException exception) {
		this.exception = exception;
	}
	public IOException getException() {
		return exception;
	}
	public String toString() {
		return exception.toString();
	}
}