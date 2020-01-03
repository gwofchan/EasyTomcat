package httpserver.server;

import java.io.IOException;
import java.net.Socket;

public class Dispatcher implements Runnable {
	// socket 客户端
	private Socket socket;
	// 请求对象
	private Request request;
	// 响应对象
	private Response response;
	// 响应码
	private int code = 200;
	
	public Dispatcher(Socket socket) {
		this.socket = socket;
		try {
			this.request = new Request(socket.getInputStream());
			this.response = new Response(socket.getOutputStream());
		} catch (IOException e) {
			code = 500;
			return;
		}
		
	}

	@Override
	public void run() {
		try {
			// 获取控制器包名
			String servletClass = WebApp.getServletClass(this.request.getUrl());
			if (servletClass!=null) {
				// 通过反射实例化控制器对象
				Servlet servlet = (Servlet) Class.forName(servletClass).getDeclaredConstructor().newInstance();
				// 处理请求
				servlet.service(request, response);
				System.out.println(servletClass);
				this.response.pushToClient(code);
			}
			this.socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
