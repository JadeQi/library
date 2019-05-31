package cn.itcast.estore.domain;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车的实体
 */
public class Cart {
	private Map<String,CartItem> map = new LinkedHashMap<String,CartItem>();
	private Double total = 0d; // 总计
/*	public Map<String, CartItem> getMap() {
		return map;
	}*/
	/**
	 * 提供返回Map中的所有的value的值的方法
	 * @return
	 */
	public Collection<CartItem> getCartItems(){
		return map.values();
	}
	
	public Double getTotal() {
		return total;
	}
	
	/**
	 * 将购物项添加到购物车:
	 */
	public void addCart(CartItem cartItem){
		// 获得购物项中的图书的id:
		String bid = cartItem.getBook().getBid();
		// 判断购物项是否已经在购物车中:
		if(map.containsKey(bid)){
			// 如果已经存在:将原有的数量+现在买的数量.总计=总计+现在购买的购物项的小计的值.
			CartItem _cartItem = map.get(bid);
			_cartItem.setCount(_cartItem.getCount()+cartItem.getCount());
		}else{
			// 如果不存在:新添加一个购物项到购物车.总计=总计+现在购买的购物项的小计的值.
			map.put(bid, cartItem);
		}
		total += cartItem.getSubtotal();
	}
	
	/**
	 * 从购物车中移除购物项
	 */
	public void removeCart(String bid){
		// 将购物项从购物车中移除:
		// CartItem cartItem = map.get(bid);
		CartItem cartItem = map.remove(bid);
		// 总计等于总计减去移除的购物项的小计
		total -= cartItem.getSubtotal();
	}
	
	/**
	 * 清空购物车:
	 */
	public void clearCart(){
		// 将购物车的购物项的集合置为空.
		map.clear();
		// 将总计设置为0
		total = 0d;
	}
}
