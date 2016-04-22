package org.server.core.filterchain;

/**
 * 过滤器任务
 * 
 * @author Hxms
 *
 */
public interface FilterTask {

	public int status();

	public void status(int newStatus);
}
