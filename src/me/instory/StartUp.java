package me.instory;

import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServlet;

public class StartUp extends HttpServlet {
	private static final long serialVersionUID = -5584545648924276184L;

	public StartUp() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Bangkok"));
		Locale.setDefault(Locale.US);
	}
}
