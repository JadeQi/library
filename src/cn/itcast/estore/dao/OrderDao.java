package cn.itcast.estore.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.estore.domain.Book;
import cn.itcast.estore.domain.Order;
import cn.itcast.estore.domain.OrderItem;
import cn.itcast.estore.utils.JDBCUtils;

/**
 * 订单管理中的DAO的类
 */
public class OrderDao {

	/**
	 * DAO中保存订单的方法:
	 * 
	 * @param connection
	 * @param order
	 */
	public void save(Connection connection, Order order) {
		QueryRunner queryRunner = new QueryRunner();
		String sql = "insert into orders values (?,?,?,?,?,?)";
		Object[] params = { order.getOid(), order.getTotal(),
				order.getOrdertime(), order.getState(), order.getAddress(),
				order.getUser().getUid() };
		try {
			queryRunner.update(connection, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * DAO中保存订单项的方法:
	 * 
	 * @param connection
	 * @param orderItem
	 */
	public void save(Connection connection, OrderItem orderItem) {
		QueryRunner queryRunner = new QueryRunner();
		String sql = "insert into orderitem values (?,?,?,?,?)";
		Object[] params = { orderItem.getItemid(), orderItem.getCount(),
				orderItem.getSubtotal(), orderItem.getBook().getBid(),
				orderItem.getOrder().getOid() };
		try {
			queryRunner.update(connection, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * DAO中根据用户的ID查询订单的方法:
	 * 
	 * @param uid
	 * @return
	 */
	public List<Order> findByUid(String uid) {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where uid = ? order by ordertime desc";
		List<Order> list;
		try {
			list = queryRunner.query(sql, new BeanListHandler<Order>(
					Order.class), uid);
			// 遍历list集合:
			for (Order order : list) {
				sql = "SELECT * FROM orderitem o,book b WHERE o.bid = b.bid AND o.oid=?";
				List<Map<String, Object>> oList = queryRunner.query(sql,
						new MapListHandler(), order.getOid());
				// 获得list集合中的每个MAP.每个MAP就是一条记录.
				for (Map<String, Object> map : oList) {
					Book book = new Book();
					BeanUtils.populate(book, map);

					OrderItem orderItem = new OrderItem();
					BeanUtils.populate(orderItem, map);
					orderItem.setBook(book);
					orderItem.setOrder(order);

					order.getOrderItems().add(orderItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return list;
	}

	/**
	 * DAO中根据订单ID查询订单的方法:
	 * 
	 * @param oid
	 * @return
	 */
	public Order findByOid(String oid) {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where oid = ?";
		Order order;
		try {
			order = queryRunner.query(sql, new BeanHandler<Order>(Order.class),
					oid);
			// 查询订单中的订单项:
			sql = "select * from orderitem o , book b where o.bid = b.bid and o.oid = ?";
			List<Map<String, Object>> oList = queryRunner.query(sql,
					new MapListHandler(), oid);
			for (Map<String, Object> map : oList) {
				Book book = new Book();
				BeanUtils.populate(book, map);

				OrderItem orderItem = new OrderItem();
				BeanUtils.populate(orderItem, map);
				orderItem.setBook(book);
				orderItem.setOrder(order);

				order.getOrderItems().add(orderItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return order;
	}

	/**
	 * DAO中修改订单的方法:
	 * 
	 * @param order
	 */
	public void update(Order order) {
		String sql = "update orders set total = ?,ordertime = ?,state= ?,address=? where oid = ?";
		Object[] params = { order.getTotal(), order.getOrdertime(),
				order.getState(), order.getAddress(), order.getOid() };
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		try {
			queryRunner.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * DAO中查询所有订单的方法:不是异步查询
	 * @return
	 */
	/*public List<Order> findAll() {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders order by ordertime desc";
		List<Order> list;
		try {
			list = queryRunner.query(sql, new BeanListHandler<Order>(Order.class));
			for (Order order : list) {
				sql = "select * from orderitem o,book b where o.bid = b.bid and o.oid = ?";
				List<Map<String,Object>> oList = queryRunner.query(sql, new MapListHandler(), order.getOid());
				for (Map<String, Object> map : oList) {
					Book book = new Book();
					BeanUtils.populate(book, map);
					
					OrderItem orderItem = new OrderItem();
					BeanUtils.populate(orderItem, map);
					orderItem.setOrder(order);
					orderItem.setBook(book);
					
					order.getOrderItems().add(orderItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return list;
	}*/
	
	/**
	 * DAO中查询所有订单的方法
	 */
	public List<Order> findAll(){
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders order by ordertime desc";
		List<Order> list;
		try {
			list = queryRunner.query(sql, new BeanListHandler<Order>(Order.class));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return list;
	}

	/**
	 * DAO中根据状态查询订单的方法:
	 * @param state
	 * @return
	 */
	/*public List<Order> findByState(int state) {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where state = ? order by ordertime desc";
		List<Order> list;
		try {
			list = queryRunner.query(sql, new BeanListHandler<Order>(Order.class),state);
			for (Order order : list) {
				sql = "select * from orderitem o,book b where o.bid = b.bid and o.oid = ?";
				List<Map<String,Object>> oList = queryRunner.query(sql, new MapListHandler(), order.getOid());
				for (Map<String, Object> map : oList) {
					Book book = new Book();
					BeanUtils.populate(book, map);
					
					OrderItem orderItem = new OrderItem();
					BeanUtils.populate(orderItem, map);
					orderItem.setOrder(order);
					orderItem.setBook(book);
					
					order.getOrderItems().add(orderItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return list;
	}*/
	public List<Order> findByState(int state) {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orders where state = ? order by ordertime desc";
		List<Order> list;
		try {
			list = queryRunner.query(sql, new BeanListHandler<Order>(Order.class),state);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return list;
	}

	/**
	 * DAO中根据订单ID异步加载订单项的方法:
	 * @param oid
	 * @return
	 */
	public List<OrderItem> showDetail(String oid) {
		List<OrderItem> list = new ArrayList<OrderItem>();
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from orderitem o,book b where o.bid = b.bid and o.oid = ?";
		try {
			List<Map<String,Object>> oList = queryRunner.query(sql, new MapListHandler(), oid);
			for (Map<String, Object> map : oList) {
				Book book = new Book();
				BeanUtils.populate(book, map);
				
				OrderItem orderItem = new OrderItem();
				BeanUtils.populate(orderItem, map);
				orderItem.setBook(book);
				list.add(orderItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
