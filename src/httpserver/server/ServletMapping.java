package httpserver.server;

/**
 * pojo类
 * @author Gwof
 *
 */
public class ServletMapping {

	private String servletName;
	
	private String servletClass;

	private String url;

	public ServletMapping(String servletName, String url, String clazz) {
		this.servletName = servletName;
		this.url = url;
		this.servletClass = clazz;
	}

	public String getServletName() {
		return servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClazz() {
		return servletClass;
	}

	public void setClazz(String clazz) {
		this.servletClass = clazz;
	}
}
