package httpserver.controller;

import httpserver.server.Request;
import httpserver.server.Response;
import httpserver.server.Servlet;

public class LoginServlet extends Servlet {

	@Override
	protected void doGet(Request request, Response response) {
		response.println("<!DOCTYPE html>")
        .println("<html lang=\"zh\">")
        .println("    <head>      ")
        .println("        <meta harset=\"UTF-8\">")
        .println("        <title>测试</title>")
        .println("    </head>     ")
        .println("    <body>      ")
				// 获取登陆名
        .println("        <h3>Hello get " + request.getParameter("username") + "</h3>")
		.println("    </body>     ")
		.println("</html>");
	}

	@Override
	protected void doPost(Request request, Response response) {
		response.println("<!DOCTYPE html>")
				.println("<html lang=\"zh\">")
				.println("    <head>      ")
				.println("        <meta harset=\"UTF-8\">")
				.println("        <title>测试</title>")
				.println("    </head>     ")
				.println("    <body>      ")
				// 获取登陆名
				.println("        <h3>Hello post " + request.getParameter("username") + "</h3>")
				.println("    </body>     ")
				.println("</html>");
	}

}
