package dongfang.mavlink_10.serialization;

import java.io.IOException;
import java.io.OutputStream;

public class CRC16OutputStreamDecorator extends OutputStream {
	private OutputStream destination;
	private int crc16;

	public void resetCRC16() {
		crc16 = 0xFFFF;
	}

	public int getCRC16() {
		return crc16;
	}

	public void addToCRC16(int data) {
		int tmp = data ^ (crc16 & 0xff);
		tmp ^= (tmp << 4);
		tmp &= 0xff;
		crc16 = (crc16 >>> 8) ^ (tmp << 8) ^ (tmp << 3) ^ (tmp >>> 4);
	}

	public void write(int data) throws IOException {
		destination.write(data);
		addToCRC16(data); // only if no exception thrown.
	}

	public void flush() throws IOException {
		destination.flush();
	}

	public void close() throws IOException {
		destination.close();
	}
}
