package polluxPak;

import java.io.*;

public class Test {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		PrintWriter writer = new PrintWriter("mon-fichier.txt", "UTF-8");
		writer.println("1");
		writer.println("2");
		writer.close();

	}}
