package httpserver.server;

public class Servlet {
	public void service(Request request, Response response ) {
		if ("get".equalsIgnoreCase(request.getMethod()))
			doGet(request,response);
		if ("post".equalsIgnoreCase(request.getMethod()))
		doPost(request,response);
	}

	protected void doGet(Request request, Response response) {
		
	}
	
	protected void doPost(Request request, Response response) {
		
	}
}
