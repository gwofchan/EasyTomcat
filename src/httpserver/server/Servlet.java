package httpserver.server;

import java.io.IOException;

/**
 * @author gwofchan
 */
public class Servlet {
	public void service(Request request, Response response ) throws IOException {
		if (request.getMethod().equalsIgnoreCase("GET")){
			doGet(request,response);
		}
		else if(request.getMethod().equalsIgnoreCase("POST")) {
			doPost(request,response);
		}
	}

	protected void doGet(Request request, Response response) throws IOException {
		
	}
	
	protected void doPost(Request request, Response response) throws IOException {
		
	}
}
