package marytts.util.pinyin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;



public class ConvertZh2Pinyin {
	public static HashMap<String, String> pinyinMap;
	
	//public ConvertZh2Pinyin(String dictname) {
	//	System.out.println("init ConvertZh2Pinyin");
	//}
	
	static {
		System.out.println("in convert2 pinyin");
	}
	
	public static void loadDict(String dictName) {
		System.out.println("load " + dictName);
		int initialCapacity = 26000;
		pinyinMap = new HashMap<String, String>(initialCapacity);
		//pinyinMap.put("1000", "zhong1");
		//pinyinMap.put("9F99", "guo2");
		
		try {
			String str;
			BufferedReader in = new BufferedReader(new FileReader(dictName));
			while ((str = in.readLine()) != null) {
				String [] temps = str.split("\\s+");
				pinyinMap.put(temps[0], temps[1].toLowerCase());
			}
			in.close();
		} catch (IOException e) {
		}
	}
	
	public static String getKeyPinyin(char queryChar) {
		//hexTest(100);
		int value = (int) (queryChar);
		String hexString = Change2Hex(value);
		if (pinyinMap.containsKey(hexString)) {
			String pinyinString  = pinyinMap.get(hexString);
			//System.out.println("get the pinyin " + pinyinString);
			return pinyinString;
		} else {
			return null;
		}
	}
	
	public static String Change2Hex(int value) {
		//System.out.println("hex100 " + String.format("%X", value));
		return String.format("%X", value);
	}
}
