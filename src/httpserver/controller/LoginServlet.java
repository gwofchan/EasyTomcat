package httpserver.controller;

import httpserver.server.Request;
import httpserver.server.Response;
import httpserver.server.Servlet;

import java.io.IOException;

/**
 * @author gwofchan
 */
public class LoginServlet extends Servlet {

	@Override
	protected void doGet(Request request, Response response) throws IOException {
		response.write("<h3>Hello get " + request.getParameter("username")+ request.getParameterValues("likes") + "</h3>");

	}

	@Override
	protected void doPost(Request request, Response response) throws IOException {
		// 获取喜好
		response.write("<h3>Hello post " + request.getParameter("username") + request.getParameterValues("likes") + "</h3>");

	}

}
