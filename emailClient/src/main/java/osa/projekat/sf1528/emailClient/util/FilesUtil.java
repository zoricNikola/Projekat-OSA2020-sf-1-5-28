package osa.projekat.sf1528.emailClient.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesUtil {
	
	public static boolean saveBytes(byte[] data, String path) {
		try (FileOutputStream fos = new FileOutputStream(path)) {
			fos.write(data);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static byte[] readBytes(String path) {
		byte[] data = null;
		try {
			data = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}

}
