package httpserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

@SuppressWarnings({"ALL", "AlibabaAvoidManuallyCreateThread"})
public class Server {

	private ServerSocket server;
	
	private boolean isShutdown = false;
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}

	/**
	 * 启动服务器
	 */
	public void start() {
		try {
			server = new ServerSocket(8083);
			// 接收数据
			this.receiveData();
		} catch (IOException e) {
			this.stop();
		}
	}
	
	/**
	 * 接收数据
	 */
	@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
	private void receiveData() {
		try {
			ThreadPoolManager threadPoolManager = new ThreadPoolManager();
			threadPoolManager.init();
			ExecutorService pool = threadPoolManager.getThreadPoolExecutor();
			while(!isShutdown) {
				pool.execute(new Dispatcher(this.server.accept()));
			}
		} catch (IOException e) {
			this.stop();
		}
	}

	/**
	 * 关闭服务器
	 */
	public void stop() {
		isShutdown = true;
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
