package common;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

public class Const {

	public static void init(HttpServletRequest request) {
		String headerdir = request.getServletContext().getRealPath("/header");
		String bookdir = request.getServletContext().getRealPath("/book");
		String headercache = "C://BookLibWeb/header";
		String bookcache = "C://BookLibWeb/book";
		createDir(headercache);
		createDir(headerdir);
		createDir(bookcache);
		createDir(bookdir);
	}
	
	
	private static void createDir(String dir) {
		System.out.println("createDir  "+dir);
		File file = new File(dir);
		if(file.exists())
			return;
		file.mkdirs();
	}
}
