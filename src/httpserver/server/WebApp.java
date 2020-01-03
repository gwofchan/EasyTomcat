package httpserver.server;

import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 配置文件类
 * @author Gwof
 *
 */
public class WebApp {

	private static ServletContext context;
	
	static {
		context = new ServletContext();
		Map<String,String> servletMap = context.getServletMap();
		Map<String,String> mappingMap = context.getMappingMap();
		
		try {
			//工厂模式,获取xml解析器，再从xml文件中读取流
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sax = factory.newSAXParser();
			
			WebHandler handler = new WebHandler();
			//发起解析
			sax.parse(Thread
					.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("httpserver/server/web.xml"),
					handler);
			
			// 注册 servlet
			List<ServletXml> servletXmlList = handler.getServletXmlList();
			for (ServletXml servletXml : servletXmlList) {
				servletMap.put(servletXml.getServletName(), servletXml.getServletClass());
			}
			
			// 注册 mapping
			List<ServletMappingXml> mappingXmlList = handler.getMappingXmlList();
			for (ServletMappingXml mapping : mappingXmlList) {
				List<String> urls = mapping.getUrlPattern();
				for (String url : urls) {
					mappingMap.put(url, mapping.getServletName());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  通过请求 url 获取对应的 Servlet 对象
	 * @param url
	 * @return
	 */
	public static String getServletClass(String url) {
		if (url == null || "".equals(url.trim())) {
			return null;
		}
		
		String servletName = context.getMappingMap().get(url);
		String servletClass = context.getServletMap().get(servletName);
		return servletClass;
	}
}
