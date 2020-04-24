package saasex;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HelloServLet", urlPatterns = { "/hello" })
public class HelloServLet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

		res.setContentType("text/plain");
		res.setCharacterEncoding("UTF-8");

		res.getWriter().print("Hello App Engine!\r\n");

	}
}