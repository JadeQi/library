package cn.itcast.estore.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import cn.itcast.estore.dao.OrderDao;
import cn.itcast.estore.domain.Order;
import cn.itcast.estore.domain.OrderItem;
import cn.itcast.estore.utils.JDBCUtils;

/**
 * 订单管理的业务层类
 */
public class OrderService {

	/**
	 * 业务层保存订单的方法:
	 * 
	 * @param order
	 */
	public void save(Order order) {
		Connection connection = JDBCUtils.getConnection();
		try {
			connection.setAutoCommit(false);
			// 保存订单:
			OrderDao orderDao = new OrderDao();
			orderDao.save(connection, order);
			// 保存订单项:
			for (OrderItem orderItem : order.getOrderItems()) {
				orderDao.save(connection, orderItem);
			}
			// 事务提交:
			DbUtils.commitAndCloseQuietly(connection);
		} catch (SQLException e) {
			e.printStackTrace();
			DbUtils.rollbackAndCloseQuietly(connection);
		}
	}

	/**
	 * 业务层根据用户的ID查询订单的方法:
	 * 
	 * @param uid
	 * @return
	 */
	public List<Order> findByUid(String uid) {
		OrderDao orderDao = new OrderDao();
		return orderDao.findByUid(uid);
	}

	/**
	 * 业务层根据订单ID查询订单的方法:
	 * 
	 * @param oid
	 * @return
	 */
	public Order findByOid(String oid) {
		OrderDao orderDao = new OrderDao();
		return orderDao.findByOid(oid);
	}

	/**
	 * 业务层修改订单的方法:
	 * 
	 * @param order
	 */
	public void update(Order order) {
		OrderDao orderDao = new OrderDao();
		orderDao.update(order);
	}

	/**
	 * 业务层查询所有订单的方法:
	 * @return
	 */
	public List<Order> findAll() {
		OrderDao orderDao = new OrderDao();
		return orderDao.findAll();
	}

	/**
	 * 业务层按状态查询订单的方法:
	 * @param state
	 * @return
	 */
	public List<Order> findByState(int state) {
		OrderDao orderDao = new OrderDao();
		return orderDao.findByState(state);
	}

	/**
	 * 业务层根据订单id异步加载订单项的方法:
	 * @param oid
	 * @return
	 */
	public List<OrderItem> showDetail(String oid) {
		OrderDao orderDao = new OrderDao();
		return orderDao.showDetail(oid);
	}

}
