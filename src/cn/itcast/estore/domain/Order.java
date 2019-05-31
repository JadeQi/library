package cn.itcast.estore.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单的实体
 */
public class Order {
	private String oid;
	private Double total;
	private Date ordertime;
	private Integer state; // 1:未付款   2:已付款,但未发货   3:已经发货,没有确认收货     4:已经确认收货,订单结束
	private String address;
	
	private User user; // 订单所属的用户.
	// 表示一个订单中有多个订单项:
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	
}
