package com.zmeng.ad.adDemo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.googlecode.protobuf.format.JsonFormat;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.AdTracking;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.ZmAdResponse;

/**
 * @author fubaokui
 * @date 2018/08/03
 */
public class ScreenSimulator implements Runnable {

	private static final int REQUEST_INTERVAL = 15;

	private static final String START_TRACKING = "0";
	private static final String END_TRACKING = "1";

	private static final int RETRY_TIMES = 3;

	public void run() {
		boolean flag = true;
		while (flag) {
			// 1.请求广告
			ZmtAPIV3.ZmAdResponse zmAdResponse = AdService.getAd();
			System.out.println(JsonFormat.printToString(zmAdResponse));
			if (zmAdResponse != null && zmAdResponse.getErrorCode() == 0) {
				// 2.回调开始播放监播url(可选)
				callbackStartPlay(zmAdResponse);
				// 3.播放广告
				play(zmAdResponse);
				// 4.回调结束播放监播url(可选)
				callbackEndPlay(zmAdResponse);
				// 5.回调计费urls
				callbackBilling(zmAdResponse);
			} else {
				// 没有广告返回就sleep一下
				try {
					TimeUnit.SECONDS.sleep(REQUEST_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private void callbackStartPlay(ZmAdResponse zmAdResponse) {
		String url = findTrackingUrlByEvent(zmAdResponse, START_TRACKING);
		int httpStatucCode = callBack(url);
		System.out.printf("requestId:%s,callbackStartPlay,httpStatucCode:%d,url:%s\n",
				zmAdResponse.getRequestId(), httpStatucCode, url);
	}

	// 返回httpStatucCode
	private int callBack(String winNoticeUrl) {
		int retryTimes = RETRY_TIMES;
		int httpStatusCode = 200;
		while (retryTimes > 0) {
			try {
				httpStatusCode = doGet(winNoticeUrl);
			} catch (Exception e) {
				httpStatusCode = 500;
			}
			if (httpStatusCode < 400) {
				return httpStatusCode;
			}
			retryTimes--;
		}
		return httpStatusCode;
	}

	public int doGet(String winNoticeUrl) throws Exception {
		URL url = new URL(winNoticeUrl);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setConnectTimeout(5000);
		urlConnection.setReadTimeout(5000);
		urlConnection.setRequestMethod("GET");
		urlConnection.connect();
		return urlConnection.getResponseCode();
	}

	private String findTrackingUrlByEvent(ZmAdResponse zmAdResponse, String event) {
		List<AdTracking> adTrackings = zmAdResponse.getAdTrackingList();
		if (adTrackings == null || adTrackings.isEmpty()) {
			return null;
		}
		for (AdTracking adTracking : adTrackings) {
			if (event.equals(adTracking.getTrackingEvent())) {
				return adTracking.getTrackingUrl(0);
			}
		}
		return null;
	}


	private void play(ZmAdResponse zmAdResponse) {
		try {
			String materiaUrl = zmAdResponse.getMaterialSrc();
			System.out.printf("requestId:%s,play,materialUrl:%s\n", zmAdResponse.getRequestId(),
					materiaUrl);

			// 模拟广告播放
			int duration = zmAdResponse.getDuration();
			TimeUnit.SECONDS.sleep(duration);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void callbackEndPlay(ZmAdResponse zmAdResponse) {
		String url = findTrackingUrlByEvent(zmAdResponse, END_TRACKING);
		int httpStatucCode = callBack(url);
		System.out.printf("requestId:%s,callbackEndPlay,httpStatucCode:%d,url:%s\n",
				zmAdResponse.getRequestId(), httpStatucCode, url);
	}

	private void callbackBilling(ZmAdResponse zmAdResponse) {
		// winNoticeUrls里面的url都需要回调
		List<String> winNoticeUrls = zmAdResponse.getWinNoticeUrlList();
		if (winNoticeUrls == null || winNoticeUrls.isEmpty()) {
			return;
		}
		for (String winNoticeUrl : winNoticeUrls) {
			int httpStatusCode = callBack(winNoticeUrl);
			System.out.printf(
					"requestId:%s,callbackBilling,httpStatusCode:%d,winNoticeUrl:%s\n",
					zmAdResponse.getRequestId(), httpStatusCode, winNoticeUrl);
		}
	}

}
