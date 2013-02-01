import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class BinFileGen {

	public static void main(String[] args) throws IOException {
		File f = new File("rbinary.bin");
		FileOutputStream os = new FileOutputStream(f);
		for (int i=0; i<256; i++)
			os.write(255-i);
		os.close();
	}
}
