package httpserver.server;

/**
 * ??? web.xml ?? <servlet> ???????
 * @author Gwof
 *
 */
public class ServletXml {

	private String servletName;
	
	private String servletClass;

	public String getServletName() {

		return servletName;
	}

	public void setServletName(String servletName) {

		this.servletName = servletName;
	}

	public String getServletClass() {

		return servletClass;
	}

	public void setServletClass(String servletClass) {

		this.servletClass = servletClass;
	}

}
