package httpserver.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;



/**
 * @author gwofchan
 */
public class Server {

	private int port = 8083;
	private Map<String, String> urlServletMap = new HashMap<String, String>();

	private Selector selector;

	public Server() {
	}

	public Server(int port) {
		this.port = port;
	}

	public void start() throws IOException {
		//  初始化映射关系
		initServletMapping();

		// 启动Selector
		selector = SelectorProvider.provider().openSelector();
		// 启动Channel
		ServerSocketChannel ssc = ServerSocketChannel.open();
		// 配置非阻塞选择
		ssc.configureBlocking(false);

		// 监听端口
		InetSocketAddress isa = new InetSocketAddress(port);
		ssc.socket().bind(isa);

		// 将Channel绑定到Selector上，并选择准备模式为Accept，此处可能会失败，后续可再次开启
		SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);

		System.out.println("MyTomcat is started...");

		ConcurrentLinkedQueue<Request> requestList = new ConcurrentLinkedQueue<>();
		ConcurrentLinkedQueue<Response> responseList = new ConcurrentLinkedQueue<>();
		ThreadPoolManager threadPoolManager = new ThreadPoolManager();
		threadPoolManager.init();
		ExecutorService pool = threadPoolManager.getThreadPoolExecutor();
		while (true) {
			//  等待Channel准备数据
			selector.select();
			Set readyKeys = selector.selectedKeys();
			Iterator i = readyKeys.iterator();

			while (i.hasNext()) {
				SelectionKey sk = (SelectionKey) i.next();
				//  从集合中移除，防止重复处理
				i.remove();

				//  如果键的接收状态未正常打开，再次尝试打开
				if (sk.isAcceptable()) {
					doAccept(sk);
					// 可读
				} else if (sk.isValid() && sk.isReadable()) {
					requestList.add(getRequest(sk));
					//  切换准备状态
					sk.interestOps(SelectionKey.OP_WRITE);
					//  可写
				} else if (sk.isValid() && sk.isWritable()) {
					responseList.add(getResponse(sk));
					//  切换准备状态
					sk.interestOps(SelectionKey.OP_READ);
				}

				//  等待一对请求和响应均准备好时处理
				if (!requestList.isEmpty() && !responseList.isEmpty()) {
					 Request myRequset=requestList.poll();
					String clazz=urlServletMap.get(myRequset.getUrl());
					if(Objects.nonNull(clazz)) {
						pool.execute(new Dispatcher(myRequset,responseList.poll(),clazz));
					}
					else {
						responseList.poll().write("404");
					}


				}
			}
		}
	}

	/**
	 * 如果没有正常开启接收模式
	 * 尝试开启接收模式
	 * @param selectionKey
	 */
	private void doAccept(SelectionKey selectionKey) {
		ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
		SocketChannel clientChannel;
		try {
			clientChannel = server.accept();
			clientChannel.configureBlocking(false);

			SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从通道中获取请求并进行包装
	 *
	 * @param selectionKey
	 * @return
	 * @throws IOException
	 */
	private Request getRequest(SelectionKey selectionKey) throws IOException {
		return new Request(selectionKey);    //  包装request
	}

	/**
	 * 从通道中获取响应并进行包装
	 *
	 * @param selectionKey
	 * @return
	 */
	private Response getResponse(SelectionKey selectionKey) throws IOException {

		return new Response(selectionKey);     //  包装response
	}


	/**
	 * 初始化Servlet的映射对象
	 */
	private void initServletMapping() {
		for (ServletMapping servletMapping : ServletMappingConfig.servletMappingList) {
			this.urlServletMap.put(servletMapping.getUrl(), servletMapping.getClazz());
		}
	}

	public static void main(String[] args) throws IOException {
		new Server().start();
	}

}
