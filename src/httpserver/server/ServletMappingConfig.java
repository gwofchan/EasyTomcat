package httpserver.server;

import java.util.ArrayList;
import java.util.List;

/**
 * ServletMappingConfig
 * @author Gwof
 *
 */
public class ServletMappingConfig {

	public static List<ServletMapping> servletMappingList = new ArrayList<ServletMapping>();

	static {
		servletMappingList.add(new ServletMapping("login","/login","httpserver.controller.LoginServlet"));
	}

	
}
