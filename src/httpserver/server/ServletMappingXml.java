package httpserver.server;

import java.util.ArrayList;
import java.util.List;

/**
 * ��װ web.xml �� <servlet-mapping> ������Ϣ
 * @author Gwof
 *
 */
public class ServletMappingXml {

	private String servletName;
	
	private List<String> urlPattern = new ArrayList<>();

	public String getServletName() {

		return servletName;
	}

	public void setServletName(String servletName) {

		this.servletName = servletName;
	}

	public List<String> getUrlPattern() {

		return urlPattern;
	}

	public void setUrlPattern(List<String> urlPattern) {

		this.urlPattern = urlPattern;
	}
	
}
