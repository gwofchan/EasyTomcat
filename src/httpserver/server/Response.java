package httpserver.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author gwofchan
 */
public class Response {
	// 常量
//	private static final String BLANK = " ";
//	private static final String RN = "\r\n";
//
//	// 存储头信息
//	private StringBuilder headerInfo;
	// 存储正文信息
//	private StringBuilder contentInfo;


	private SelectionKey selectionKey;


	public Response(SelectionKey selectionKey) throws IOException {
		this.selectionKey = selectionKey;
	}

	public void write(String content) throws IOException{
			//  拼接相应数据包
			StringBuffer httpResponse = new StringBuffer();
			httpResponse.append("HTTP/1.1 200 OK\n")
					.append("Content-type:text/html\n")
					.append("\r\n")
					.append("<html><body>")
					.append(content)
					.append("</body></html>");

			// 转换为ByteBuffer
			ByteBuffer bb = ByteBuffer.wrap(httpResponse.toString().getBytes(StandardCharsets.UTF_8));
			//  从契约获取通道
			SocketChannel channel = (SocketChannel) selectionKey.channel();
			//  向通道中写入数据
			long len = channel.write(bb);
			if (len == -1){
				selectionKey.cancel();
			}
			bb.flip();
			channel.close();
			selectionKey.cancel();
		}
	
	/**
	 * 设置头信息
	 * @param code
	 */
//	public void setHeaderInfo(int code) {
//		// 响应头信息
//		headerInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
//
//		if ("200".equals(code)) {
//			headerInfo.append("OK");
//
//		} else if ("404".equals(code)) {
//			headerInfo.append("NOT FOUND");
//
//		} else if ("500".equals(code)) {
//			headerInfo.append("SERVER ERROR");
//		}
//
//		headerInfo.append(RN);
//		headerInfo.append("Content-Length:").append(len).append(RN);
//		headerInfo.append("Content-Type:text/html").append(RN);
//		headerInfo.append("Date:").append(new Date()).append(RN);
//		headerInfo.append("Server:nginx/1.12.1").append(RN);
//		headerInfo.append(RN);
//	}
//

	
//	/**
//	 * 返回客户端
//	 * @param code
//	 * @throws IOException
//	 */
//	public void pushToClient(int code) throws IOException {
//		// 设置头信息
//		this.setHeaderInfo(code);
//		bw.append(headerInfo.toString());
//		// 设置正文
//		bw.append(contentInfo.toString());
//
//		bw.flush();
//		flush
// public void flush() throws IOException
// 刷新此输出流并强制写出所有缓冲的输出字节。
// flush 的常规协定是：如果此输出流的实现已经缓冲了以前写入的任何字节，则调用此方法指示应将这些字节立即写入它们预期的目标。
// 如果此流的预期目标是由基础操作系统提供的一个抽象（如一个文件），
// 则刷新此流只能保证将以前写入到流的字节传递给操作系统进行写入，
// 但不保证能将这些字节实际写入到物理设备（如磁盘驱动器）。
// OutputStream 的 flush 方法不执行任何操作。
// 指定者： 接口 Flushable 中的 flush
// 抛出： IOException - 如果发生 I/O 错误。

//	}
	
	

}
