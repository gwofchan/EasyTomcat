package httpserver.server;



import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


/**
 * @author gwofchan
 */
public class Dispatcher implements Runnable {

	// 请求对象
	private Request request;
	// 响应对象
	private Response response;
	private String servletClass;

	public Dispatcher(Request myRequest, Response myResponse,String servletClass) {
			response = myResponse;
			request = myRequest;
			this.servletClass = servletClass;
	}

	@Override
	public void run() {
		try {
			// 通过反射实例化控制器对象
				Servlet servlet = (Servlet) Class.forName(this.servletClass).getDeclaredConstructor().newInstance();
				// 处理请求
				servlet.service(this.request, this.response);
				System.out.println(this.servletClass);

			} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException | IOException ex) {
			ex.printStackTrace();
		}
	}
}
