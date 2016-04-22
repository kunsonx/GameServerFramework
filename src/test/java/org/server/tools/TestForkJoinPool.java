package org.server.tools;

//import static org.junit.Assert.*;

import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

public class TestForkJoinPool {

	@Test
	public void test() throws InterruptedException {
		ForkJoinPool executeService = new ForkJoinPool(1,
				ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

		for (int i = 0; i < 10; i++) {
			executeService.execute(() -> {

				try {
					Thread.sleep(15);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println(Thread.currentThread().getName());

			});
		}

		System.out.println(Thread.currentThread().getName());

		Thread.sleep(150);
	}

}
