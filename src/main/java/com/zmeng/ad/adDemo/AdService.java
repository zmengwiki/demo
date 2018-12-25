package com.zmeng.ad.adDemo;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.zmeng.rinascimento.erasmus.common.basic.AdRequestUtil;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3;

/**
 * @author fubaokui
 * @date 2018/08/03
 */
public class AdService {

	// 测试服务地址
	final static String url = "http://123.56.176.83:8082/erasmus/zmt/api/ad/getAd.do";

	public static ZmtAPIV3.ZmAdResponse getAd() {
		// 构建请求参数
		ZmtAPIV3.ZmAdRequest zmAdRequest = AdRequestUtil.buildAdRequst();
		return sendPost(url, zmAdRequest.toByteArray());
	}

	public static ZmtAPIV3.ZmAdResponse sendPost(String url, byte[] params) {
		BufferedOutputStream out = null;
		BufferedReader br = null;
		try {
			URL realURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realURL.openConnection();
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/x-protobuf");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输入流
			out = new BufferedOutputStream(conn.getOutputStream());
			// 发送请求参数
			out.write(params);
			out.flush();
			InputStream inputStream = conn.getInputStream();
			byte[] responseByte = inputStreamTOByte(inputStream);
			if (responseByte.length > 0) {
				return ZmtAPIV3.ZmAdResponse.parseFrom(responseByte);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static byte[] inputStreamTOByte(InputStream in) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int count = -1;
		while ((count = in.read(data, 0, 1024)) != -1)
			outStream.write(data, 0, count);
		return outStream.toByteArray();
	}


}

