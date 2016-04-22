/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.test;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Administrator
 */
public class Slf4jTest {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		final Logger logger = LoggerFactory.getLogger(Slf4jTest.class);

		logger.warn("提示一条警告信息");

		logger.info("提示一条普通消息");

		logger.error("提示一条错误消息");
		
		Date date = new Date(1444891165L * 1000);
		System.out.println(date);
		System.out.println(System.currentTimeMillis());
	}

}
