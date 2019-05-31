package cn.itcast.estore.domain;
/**
 * 购物项的实体:
 */
public class CartItem {
	private Book book;
	private Integer count;
	// private Double subtotal;
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getSubtotal() {
		return count * book.getPrice();
	}
/*	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}*/
	
	
}
