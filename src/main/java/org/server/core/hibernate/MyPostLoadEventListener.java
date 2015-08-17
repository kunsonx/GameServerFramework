/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.hibernate;

import java.util.List;
import org.hibernate.event.spi.PostLoadEvent;

/**
 * 载入事件监听器
 *
 * @author Hxms
 */
public class MyPostLoadEventListener extends org.hibernate.event.internal.DefaultPostLoadEventListener {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2984390455582202548L;

	@Override
    public void onPostLoad(PostLoadEvent event) {
        super.onPostLoad(event); //To change body of generated methods, choose Tools | Templates.
        if (event.getEntity() instanceof OrmChangeLoggerModelInterface) {
            List<String> updateFileds = ((OrmChangeLoggerModelInterface) event.getEntity()).readNeedUpdateValName();// 读取需要更新的字段
            updateFileds.clear();// 清理需要更新的字段
        }
    }
}
