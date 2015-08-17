/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.hibernate;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.imageio.spi.ServiceRegistry;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.mapping.PersistentClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Administrator
 */
public class HibernateUtil {

    static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);
    static ServiceRegistry serviceRegistry;
    static Configuration cfg;
    static final SessionFactory sessionFactory;

    static {
        try {
            // 引导 hibernate 日志配置 see \documentation\topical\html\logging\Logging.html
            System.setProperty("org.jboss.logging.provider", "slf4j");
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            Configuration configuration = new Configuration().configure();
            // 添加拦截器
            configuration.setInterceptor(new DirtyFindInterceptor());
//以下这两句就是4.3的新用法
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            StandardServiceRegistryImpl registry = (StandardServiceRegistryImpl) builder.build();
            // 绑定映射
            configuration.buildMappings();
            // 修改配置
            for (Iterator<PersistentClass> iterator = configuration.getClassMappings(); iterator.hasNext();) {
                PersistentClass next = iterator.next();
                try {
                    Class<?> cls = Class.forName(next.getClassName());
                    if (cls != null && (cls.getSuperclass().equals(OrmChangeLoggerModel.class) || OrmAutoLogger.hasAutoProxy(cls))) {
                        // 设置动态更新
                        next.setDynamicUpdate(true);
                    }
                } catch (Exception e) {
                    log.error("Error Config AutoLogger...", e);
                }
            }
            sessionFactory = configuration.buildSessionFactory(registry);
            // 添加事件
            EventListenerRegistry eventListenerRegistry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(
                    EventListenerRegistry.class);
            eventListenerRegistry.setListeners(EventType.POST_LOAD, new MyPostLoadEventListener());
        } catch (Throwable ex) {
            // Log the exception. 
            log.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * 初始化Hibernate工具
     */
    public static void initialize() {
    }

    /**
     * 获得会话工厂
     *
     * @return 会话工厂
     */
    static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * 打开会话
     *
     * @return Hibernate 数据会话
     */
    public static Session openSession() {
        DirtyFindInterceptor interceptor = new DirtyFindInterceptor(null);
        Session session = getSessionFactory().withOptions().interceptor(interceptor).openSession();
        interceptor.setSessionImplementor((SessionImplementor) session);
        return session;
    }

    /**
     * 打开会话
     *
     * @return Hibernate 数据会话
     */
    public static StatelessSession openStatelessSession() {
        StatelessSession session = getSessionFactory().withStatelessOptions().openStatelessSession();
        return session;
    }

    /**
     * 更新模块对象
     *
     * @param objs 对象集合
     * @return 是否更新成功
     */
    public static boolean update(Object... objs) {
        return insertOrUpdate(true, objs);
    }

    /**
     * 插入模块对象到数据库
     *
     * @param objs 对象集合
     * @return 是否插入成功
     */
    public static boolean insert(Object... objs) {
        return insertOrUpdate(false, objs);
    }

    /**
     * 从数据库删除模块对象
     *
     * @param objs 对象集合
     * @return 是否删除成功
     */
    public static boolean delete(Object... objs) {
        return usingStatelessSession(session -> {
            for (Object o : objs) {
                if (o instanceof OrmChangeLoggerModelInterface) {
                    o = ((OrmChangeLoggerModelInterface) o).original();
                }
                if (o != null) {
                    session.delete(o);
                }
            }
        });
    }

    /**
     * 尝试初始化对象
     *
     * @param entity 实体
     * @param obj 对象
     * @return 初始化是否成功
     */
    static public boolean tryInitialize(Object entity, Object obj) {
        if (!Hibernate.isInitialized(obj)) {
            Object entity_ = entity instanceof OrmChangeLoggerModelInterface ? ((OrmChangeLoggerModelInterface) entity).original() : entity;
            try {
                Hibernate.initialize(obj);
            } catch (Exception e) {
                usingSession(s -> {
                    s.update(entity_);
                    Hibernate.initialize(obj);
                });
            }
        }
        return true;
    }

    /**
     * 插入或者更新对象列表
     *
     * @param update true 为 更新 false 为插入
     * @param objs 数据实体模块
     * @return 操作是否成功
     */
    public static boolean insertOrUpdate(boolean update, Object[] objs) {
        boolean result = false;
        List<Object> sessionObject = new LinkedList<>();
        List<Object> statelessSessionObject = new LinkedList<>();

        for (Object obj : objs) {
            if (obj instanceof OrmChangeLoggerModelInterface) {
                obj = ((OrmChangeLoggerModelInterface) obj).original();
            }
            if (update && obj instanceof OrmChangeLoggerModelInterface) {
                sessionObject.add(obj);
            } else {
                statelessSessionObject.add(obj);
            }
        }

        // 静态会话更新列表不等于空
        if (!statelessSessionObject.isEmpty()) {
            usingStatelessSession(session -> {
                statelessSessionObject.stream().forEach((o) -> {
                    if (update) {
                        session.update(o);
                    } else {
                        session.insert(o);
                    }
                });
            });
        }
        // 更新跟踪会话
        if (!sessionObject.isEmpty()) {
            usingSession(session -> {
                sessionObject.stream().forEach(obj -> {
                    if (update) {
                        session.update(obj);
                    } else {
                        session.save(obj);
                    }
                });
            });
        }
        return result;
    }

    static boolean usingSession(Consumer<Session> action) {
        if (getSessionFactory() != null) {
            Session session = openSession();
            try {
                Transaction transaction = session.beginTransaction();
                action.accept(session);
                transaction.commit();
                return true;
            } catch (Exception e) {
                log.error("Hibernate Handler Objests Error:", e);
                return false;
            } finally {
                session.close();
            }
        }
        return false;
    }

    static boolean usingStatelessSession(Consumer<StatelessSession> action) {
        if (getSessionFactory() != null) {
            StatelessSession session = getSessionFactory().openStatelessSession();
            try {
                Transaction transaction = session.beginTransaction();
                action.accept(session);
                transaction.commit();
                return true;
            } catch (Exception e) {
                log.error("Hibernate Handler Objests Error:", e);
                return false;
            } finally {
                session.close();
            }
        }
        return false;
    }

}
