package cn.itcast.estore.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.estore.domain.Book;
import cn.itcast.estore.utils.JDBCUtils;

/**
 * 图书管理的DAO的类
 */
public class BookDao {

	/**
	 * DAO中的查询所有图书的方法
	 * 
	 * @return
	 */
	public List<Book> findAll() {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from book where isdel = ?"; // isdel 0:上架 1:下架
		List<Book> list;
		try {
			list = queryRunner.query(sql,
					new BeanListHandler<Book>(Book.class), 0);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return list;
	}

	/**
	 * DAO中的根据CID查询图书的方法
	 * 
	 * @param cid
	 * @return
	 */
	public List<Book> findByCid(String cid) {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from book where cid = ? and isdel = ?";
		List<Book> list;
		try {
			list = queryRunner.query(sql,
					new BeanListHandler<Book>(Book.class), cid, 0);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return list;
	}

	/**
	 * DAO中根据图书的ID查询图书的方法:
	 * 
	 * @param bid
	 * @return
	 */
	public Book findByBid(String bid) {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from book where bid = ?";
		Book book;
		try {
			book = queryRunner.query(sql, new BeanHandler<Book>(Book.class),
					bid);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return book;
	}

	/**
	 * DAO中保存图书的方法:
	 * 
	 * @param book
	 */
	public void save(Book book) {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "insert into book values (?,?,?,?,?,?,?)";
		Object[] params = { book.getBid(), book.getBname(), book.getPrice(),
				book.getAuthor(), book.getImage(), book.getCid(),
				book.getIsdel() };
		try {
			queryRunner.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * DAO中的修改图书的方法:
	 * 
	 * @param book
	 */
	public void update(Book book) {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "update book set bname = ?,price = ?,author=?,image = ?,cid=?,isdel=? where bid = ?";
		Object[] params = { book.getBname(), book.getPrice(), book.getAuthor(),
				book.getImage(), book.getCid(), book.getIsdel(), book.getBid() };
		try {
			queryRunner.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * DAO中查询所有已经下架的图书的方法
	 * @return
	 */
	public List<Book> findAllPushDown() {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from book where isdel = ?";
		List<Book> list;
		try {
			list = queryRunner.query(sql, new BeanListHandler<Book>(Book.class), 1);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return list;
	}

}
