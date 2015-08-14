/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate 赃数据寻找拦截器
 *
 * @author Hxms
 */
public class DirtyFindInterceptor extends EmptyInterceptor {

    static final Logger log = LoggerFactory.getLogger(DirtyFindInterceptor.class);

    SessionImplementor sessionImplementor;// 会话实现

    /**
     * 构造新藏数据拦截器
     */
    public DirtyFindInterceptor() {
    }

    /**
     * 构造新藏数据拦截器
     *
     * @param sessionImplementor 会话实现类
     */
    public DirtyFindInterceptor(SessionImplementor sessionImplementor) {
        this.sessionImplementor = sessionImplementor;
    }

    public SessionImplementor getSessionImplementor() {
        return sessionImplementor;
    }

    public void setSessionImplementor(SessionImplementor sessionImplementor) {
        this.sessionImplementor = sessionImplementor;
    }

    @Override
    public Object instantiate(String entityName, EntityMode entityMode, Serializable id) {
        if (sessionImplementor != null) {// 会话实现不为空
            EntityPersister entityPersister = sessionImplementor.getFactory().getEntityPersister(entityName);// 获得实体持续程序
            Object result = entityPersister.instantiate(id, sessionImplementor);// 实例化对象
            if (OrmAutoLogger.hasAutoProxy(result.getClass())) {// 检测自动注册代理
                Object res = OrmAutoLogger.instantiate(result.getClass());// 创建代理类
                if (res != null) {
                    entityPersister.getEntityTuplizer().setIdentifier(res, id, sessionImplementor);// 设置主键
                    result = res;
                }
            }
            if (result instanceof OrmChangeLoggerModelInterface) {// 实体类型是记录修改模块
                result = ((OrmChangeLoggerModelInterface) result).proxy();// 生成代理类
            }
            return result;
        }
        return super.instantiate(entityName, entityMode, id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getEntityName(Object object) {
        if (sessionImplementor != null) {// 会话实现不为空
            return OrmAutoLogger.getEntityName(object.getClass());
        }
        return super.getEntityName(object); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (entity instanceof OrmChangeLoggerModelInterface) {
            List<String> fields = ((OrmChangeLoggerModelInterface) entity).readNeedUpdateValName();
            synchronized (fields) {
                List<Integer> idxs = new ArrayList<>(propertyNames.length);
                for (int i = 0; i < propertyNames.length; i++) {
                    String propertyName = propertyNames[i];
                    for (String field : fields) {
                        if (propertyName.equalsIgnoreCase(field)) {
                            idxs.add(i);
                            break;
                        }
                    }
                }
                fields.clear();
                // 生成序号
                // if (!idxs.isEmpty()) {
                int[] indexs = new int[idxs.size()];
                for (int i = 0; i < indexs.length; i++) {
                    indexs[i] = idxs.get(i);
                }
                return indexs;
                //    }
            }
        }
        return super.findDirty(entity, id, currentState, previousState, propertyNames, types); //To change body of generated methods, choose Tools | Templates.
    }
}
