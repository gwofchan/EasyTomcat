package httpserver.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

public class Response {
	// 常量
	private static final String BLANK = " ";
	private static final String RN = "\r\n";
	// 响应内容长度
	private int len;
	// 存储头信息
	private StringBuilder headerInfo;
	// 存储正文信息
	private StringBuilder contentInfo;
	// 输出流
	private BufferedWriter bw;
	
	public Response() {
		this.headerInfo = new StringBuilder();
		this.contentInfo = new StringBuilder();
		this.len = 0;
	}
	
	public Response(OutputStream os) {
		this();
		this.bw = new BufferedWriter(new OutputStreamWriter(os));
	}
	
	/**
	 * 设置头信息
	 * @param code
	 */
	private void setHeaderInfo(int code) {
		// 响应头信息
		headerInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
		
		if ("200".equals(code)) {
			headerInfo.append("OK");
			
		} else if ("404".equals(code)) {
			headerInfo.append("NOT FOUND");
			
		} else if ("500".equals(code)) {
			headerInfo.append("SERVER ERROR");
		}
		
		headerInfo.append(RN);
		headerInfo.append("Content-Length:").append(len).append(RN);
		headerInfo.append("Content-Type:text/html").append(RN);
		headerInfo.append("Date:").append(new Date()).append(RN);
		headerInfo.append("Server:nginx/1.12.1").append(RN);
		headerInfo.append(RN);
	}
	
	/**
	 * 设置正文
	 * @param content
	 * @return
	 */
	public Response print(String content) {
		contentInfo.append(content);
		len += content.getBytes().length;
		return this;
	}
	
	/**
	 * 设置正文
	 * @param content
	 * @return
	 */
	public Response  println(String content) {
		contentInfo.append(content).append(RN);
		len += (content + RN).getBytes().length;
		return this;
	}
	
	/**
	 * 返回客户端
	 * @param code
	 * @throws IOException
	 */
	public void pushToClient(int code) throws IOException {
		// 设置头信息
		this.setHeaderInfo(code);
		bw.append(headerInfo.toString());
		// 设置正文
		bw.append(contentInfo.toString());
		
		bw.flush();
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

	}
	
	
	public void close() {
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
