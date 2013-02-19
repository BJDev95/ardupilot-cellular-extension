package dongfang.mavlink_10.serialization;

import java.io.IOException;
import java.io.InputStream;

public class CRC16InputStreamDecorator extends InputStream {
	private InputStream source;
	private int crc16;

	public CRC16InputStreamDecorator(InputStream source) {
		this.source = source;
	}
	
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

	public int read() throws IOException {
		int data = source.read();
		addToCRC16(data);
		return data;
	}

	public int available() throws IOException {
		return source.available();
	}

	public long skip(long n) throws IOException {
		return source.skip(n);
	}

	public void close() throws IOException {
		source.close();
	}

	public void mark(int readlimit) {
		source.mark(readlimit);
	}

	public boolean markSupported() {
		return source.markSupported();
	}

	public void reset() throws IOException {
		source.reset();
	}
}
