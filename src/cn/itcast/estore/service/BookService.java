package cn.itcast.estore.service;

import java.util.List;

import cn.itcast.estore.dao.BookDao;
import cn.itcast.estore.domain.Book;

/**
 * 图书管理的业务层的类
 */
public class BookService {

	/**
	 * 业务层查询所有图书的方法:
	 * 
	 * @return
	 */
	public List<Book> findAll() {
		BookDao bookDao = new BookDao();
		return bookDao.findAll();
	}

	/**
	 * 业务层根据分类的ID查询图书的方法:
	 * 
	 * @param cid
	 * @return
	 */
	public List<Book> findByCid(String cid) {
		BookDao bookDao = new BookDao();
		return bookDao.findByCid(cid);
	}

	/**
	 * 业务层根据图书的ID查询图书的方法
	 * 
	 * @param bid
	 * @return
	 */
	public Book findByBid(String bid) {
		BookDao bookDao = new BookDao();
		return bookDao.findByBid(bid);
	}

	/**
	 * 业务层保存图书的方法:
	 * 
	 * @param book
	 */
	public void save(Book book) {
		BookDao bookDao = new BookDao();
		bookDao.save(book);
	}

	/**
	 * 业务层修改图书的方法:
	 * 
	 * @param book
	 */
	public void update(Book book) {
		BookDao bookDao = new BookDao();
		bookDao.update(book);
	}

	/**
	 * 业务层查询所有已经下架的图书的方法:
	 * @return
	 */
	public List<Book> findAllPushDown() {
		BookDao bookDao = new BookDao();
		return bookDao.findAllPushDown();
	}

}
