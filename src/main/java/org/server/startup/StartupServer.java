/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.startup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import org.server.core.StandardServerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Administrator
 */
public class StartupServer implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(StartupServer.class);

	private final String clsName;

	public StartupServer(String clsName) {
		this.clsName = clsName;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		StartupServer startupServer = null;
		if (args.length > 0) {
			startupServer = new StartupServer(args[0]);
		}

		if (startupServer != null) {
			startupServer.run();
		} else {
			logger.error("无法启动对应服务！参数不正确！");
		}
	}

	@Override
	public void run() {
		try {
			// 要启动的类名
			Class<?> c = Class.forName(clsName);

			// 构造对象
			Constructor<?> constructor = c.getConstructor();

			// 标准服务结构
			StandardServerInterface server = null;

			if (constructor != null) {
				server = (StandardServerInterface) constructor.newInstance();
			}

			if (server != null) {
				// 开始运行服务
				server.start();

				logger.info("服务已经完成启动...如需操作请输入命令.");

				// 输入器
				Scanner sc = new Scanner(System.in);
				try {
					// 处理输入命令
					while (true) {
						server.executeCommand(sc.nextLine().trim());
					}
				} finally {
					sc.close();
				}

			}
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error("加载运行服务失败：", e);
		}
	}

}
