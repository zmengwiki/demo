package com.zmeng.ad.adDemo;

/**
 * 
 * @author fubaokui
 * @date 2017年10月19日 下午5:12:46
 */
public class Main {

	public static void main(String[] args) throws Exception {
		new Thread(new ScreenSimulator()).start();
	}

}
