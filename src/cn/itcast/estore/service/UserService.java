package cn.itcast.estore.service;

import cn.itcast.estore.dao.UserDao;
import cn.itcast.estore.domain.User;
import cn.itcast.estore.utils.MailUtils;
import cn.itcast.estore.utils.UUIDUtils;

/**
 * 用户管理的业务层的类
 */
public class UserService {

	/**
	 * 业务层用户注册的方法:
	 * @param user
	 */
	public void regist(User user) {
		/**
		 * 1.完成数据库插入的操作.
		 * 2.发送一封激活邮件.
		 */
		// 1.数据库插入的操作:
		UserDao userDao = new UserDao();
		// 补全数据:
		user.setUid(UUIDUtils.getUUID());
		user.setState(0); // 0:用户未激活	1:用户已激活
		String code = UUIDUtils.getUUID()+UUIDUtils.getUUID();// 激活码
		user.setCode(code);
		userDao.save(user);
		
		// 2.发送一封激活邮件:
		MailUtils.sendMail(user.getEmail(), code);
	}

	/**
	 * 业务层根据用户名查询用户的方法
	 * @param username
	 * @return
	 */
	public User findByUsername(String username) {
		UserDao userDao = new UserDao();
		return userDao.findByUsername(username);
	}

	/**
	 * 业务层根据激活码查询用户
	 * @param code
	 * @return
	 */
	public User findByCode(String code) {
		UserDao userDao = new UserDao();
		return userDao.findByCode(code);
	}

	/**
	 * 业务层修改用户的方法
	 * @param user
	 */
	public void update(User user) {
		UserDao userDao = new UserDao();
		userDao.update(user);
	}

	/**
	 * 业务层用户登录的方法
	 * @param user
	 * @return
	 */
	public User login(User user) {
		UserDao userDao = new UserDao();
		return userDao.login(user);
	}

}
