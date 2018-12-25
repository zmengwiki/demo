/**
 * 
 */
package com.zmeng.rinascimento.erasmus.common.basic;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.protobuf.ByteString;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.AdSlot;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.Device;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.Device.DeviceType;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.Device.OsType;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.Gps;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.Network;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.Network.ConnectionType;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.Network.OperatorType;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.Size;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.UdId;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.Version;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.ZmAdRequest;
import com.zmeng.rinascimento.erasmus.common.basic.ZmtAPIV3.ZmAdRequest.Builder;

/**
 * @author fubaokui
 * @date 2017年10月19日 下午5:13:27
 */
public class AdRequestUtil {

	public static ZmAdRequest buildAdRequst() {
		ZmtAPIV3.ZmAdRequest.Builder requestBuilder = ZmtAPIV3.ZmAdRequest.newBuilder();
		requestBuilder.setRequestId(Long.toString(System.currentTimeMillis())
				+ RandomStringUtils.randomAlphanumeric(19)); // [0-9a-zA-Z]{32},保证每次请求唯一
		requestBuilder.setChannelId("test"); // 请使用众盟分配的channelId
		requestBuilder.setScreenId("00E06F11FF64"); // 测试环境不校验；正式环境必须使用在众盟备案的屏id
		requestBuilder.setToken("8UQ8JSTzaE35M5TWsawnJWWfDfjo5ZN2V5JlZAR2CCMzV30lVzOWmwd-RDJMUxVX"); // 请使用众盟分配的token

		// 必填
		// 构建Network请求参数
		builderNetwork(requestBuilder);

		// 必填
		// 构建Device请求参数
		builderDevice(requestBuilder);

		// 必填
		// 构建Adslot请求参数
		builderAdslot(requestBuilder);

		// 选填
		// 构建GPS请求参数
		builderGps(requestBuilder);

		return requestBuilder.build();
	}

	private static void builderGps(Builder jPadBuilder) {
		jPadBuilder.setGps(Gps.newBuilder().setCoordinateType(Gps.CoordinateType.GCJ02)
				.setLatitude(116.33912).setLongitude(39.99303375).build());
	}

	private static void builderNetwork(Builder jPadBuilder) {
		Network.Builder networkBuilder = Network.newBuilder();
		networkBuilder.setCellularId("").setOperatorType(OperatorType.CHINA_MOBILE)
				.setConnectionType(ConnectionType.CELL_4G).setIpv4("127.0.0.1");
		jPadBuilder.setNetwork(networkBuilder.build());
	}

	private static void builderDevice(Builder jPadBuilder) {
		Device.Builder builder = Device.newBuilder();
		builder.setUdid(UdId.newBuilder().setAndroidId("3a717c76d46db29c")
				.setImei("357789049271270").setMac("00:E0:4C:36:0A:B4")).build();
		builder.setOsType(OsType.ANDROID);
		builder.setDeviceType(DeviceType.OUTDOOR_SCREEN);
		builder.setOsVersion(Version.newBuilder().setMajor(4).setMinor(4).setMicro(4).build());
		builder.setVendor(ByteString.copyFromUtf8("rockchip"));
		builder.setModel(ByteString.copyFromUtf8("rk3188"));
		builder.setScreenSize(Size.newBuilder().setWidth(1080).setHeight(1920).build());
		jPadBuilder.setDevice(builder.build());
	}

	private static void builderAdslot(Builder jPadBuilder) {
		AdSlot.Builder builder = AdSlot.newBuilder();
		builder.setType(1).setAdslotSize(Size.newBuilder().setWidth(1080).setHeight(1920));
		jPadBuilder.setAdslot(builder.build());
	}

}
