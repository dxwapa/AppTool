package net.lgpage.androidant.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

public class StringBuilderUtil {

	public static void main(String[] args) throws Exception {
		String str = StringBuilderUtil.toStringBuilder(new File(
				"C:\\Users\\Administrator\\Desktop\\sb.txt"));
		System.out.println(str);
	}

	public static String toStringBuilder(File file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		sb.append("StringBuilder sb = new StringBuilder();\n");
		String line = br.readLine();

		while (line != null) {
			line = line.replace("\"", "\\\"");
			sb.append("sb.append(\"" + line + "\\n\");\n");
			line = br.readLine();
		}
		return sb.toString();
	}
}
