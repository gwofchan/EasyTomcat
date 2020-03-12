package httpserver.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * @author gwofchan
 */
@SuppressWarnings({"ALL", "AlibabaUndefineMagicConstant"})
public class Request {

	private static final String CHARSET = "GBK";
	// 常量（回车+换行）
	private static final String RN = "\r\n";
	private static final String GET = "get";
	private static final String POST = "post";
	// 请求方式
	private String method;
	// 请求 url
	private String url;
	// 请求参数
	private Map<String, List<String>> parameterMap;

	/**
	 * 解析监听地址 拆解后存入parameterMap
	 * @param selectionKey
	 * @throws IOException
	 */
	public Request(SelectionKey selectionKey) throws IOException{
		this.parameterMap = new HashMap<>();
		String httpRequest= "";
		//  从契约获取通道
		SocketChannel channel = (SocketChannel) selectionKey.channel();
		//  从堆内存中获取内存
		ByteBuffer bb = ByteBuffer.allocate(16*1024);
		try {
			//  从通道中读取数据到ByteBuffer容器中
			int length = channel.read(bb);
			if (length < 0){
				//  取消该契约
				selectionKey.cancel();
			}else {
				//  将ByteBuffer转为String
				httpRequest = new String(bb.array()).trim();
				//  获取请求头
				// 1.获取请求方式
				String firstLine = httpRequest.substring(0, httpRequest.indexOf(RN));
				int index = firstLine.indexOf("/");
				this.method = firstLine.substring(0,index).trim();

				String urlStr = firstLine.substring(index,firstLine.indexOf("HTTP/1.1")).trim();
				String parameters = "";
				if (GET.equalsIgnoreCase(this.method)) {
					if (urlStr.contains("?")) {
						String[] arr = urlStr.split("\\?");
						this.url = arr[0];
						parameters = arr[1];
					} else {
						this.url = urlStr;
					}

				} else if (POST.equalsIgnoreCase(this.method)) {
					this.url = urlStr;
					parameters = httpRequest.substring(httpRequest.lastIndexOf(RN)).trim();
				}

				StringTokenizer token = new StringTokenizer(parameters, "&");
				while(token.hasMoreTokens()) {
					// keyValue 格式：username=aaa 或 username=
					String keyValue = token.nextToken();
					String[] kv = keyValue.split("=");
					if (kv.length == 1) {
						kv = Arrays.copyOf(kv, 2);
						kv[1] = null;
					}

					String key = kv[0].trim();
					String value = kv[1] == null ? null : this.decode(kv[1].trim(), CHARSET);

					if (!this.parameterMap.containsKey(key)) {
						this.parameterMap.put(key, new ArrayList<>());
					}

					this.parameterMap.get(key).add(value);
				}
			}
			bb.flip();
		}
			catch (IOException e) {
			return;
		}

	}


	/**
	 * 根据参数名获取多个参数值
	 * @param name
	 * @return
	 */
	public String getParameterValues(String name) {
		List<String> values = null;
		if ((values = this.parameterMap.get(name)) == null) {
			return null;
		}
		// (Java8) 使用join方法链接字符串
		String str=String.join(",",values.toArray(new String[0]));
		return str;
	}
	
	/**
	 * 根据参数名获取唯一参数值
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		String values = this.getParameterValues(name);
		if (values == null) {
			return null;
		}
		return values;
	}

	
	/**
	 * 解码中文
	 * @param value
	 * @param code
	 * @return
	 */
	private String decode(String value, String charset) {
		try {
			return URLDecoder.decode(value, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getUrl() {
		return url;
	}
	public String getMethod() {
		return method;
	}
	
 }
