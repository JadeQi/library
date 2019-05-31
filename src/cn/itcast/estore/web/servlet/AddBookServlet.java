package cn.itcast.estore.web.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.estore.domain.Book;
import cn.itcast.estore.service.BookService;
import cn.itcast.estore.utils.UUIDUtils;
/**
 * 添加图书的Servlet:(文件上传)
 */
public class AddBookServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.创建磁盘问卷星工厂:
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
		// 2.创建核心解析类:
		ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
		// 3.解析request:
		// 创建一个Map集合,用于保存页面提交数据.
		Map<String,String> map = new HashMap<String,String>();
		// 封装一个Book对象.
		Book book = new Book();
		try {
			List<FileItem> list = servletFileUpload.parseRequest(request);
			String fileName = null;
			// 遍历List集合:
			for (FileItem fileItem : list) {
				// 判断文件项 是否是文件上传项
				if(fileItem.isFormField()){
					// 普通项
					// 获得普通项的名称:
					String name = fileItem.getFieldName();
					// 获得普通项的值:
					String value = fileItem.getString("UTF-8");
					map.put(name, value);
				}else{
					// 文件上传项:
					// 获得文件名:
					fileName = fileItem.getName();
					// 获得代表文件内容的输入流:
					InputStream is = fileItem.getInputStream();
					// 写入到硬盘中:
					String path = this.getServletContext().getRealPath("/book_img");
					// 创建输出流:
					OutputStream os = new FileOutputStream(path+"\\"+fileName);
					// 两个流的对接:
					int len = 0;
					byte[] b = new byte[1024];
					while((len = is.read(b))!=-1){
						os.write(b, 0, len);
					}
					is.close();
					os.close();
				}
			}
			// 封装Book对象.
			BeanUtils.populate(book, map);
			book.setBid(UUIDUtils.getUUID());
			book.setIsdel(0); 
			book.setImage("book_img/"+fileName);
			// 调用业务层完成保存的操作:
			BookService bookService = new BookService();
			bookService.save(book);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//	页面跳转
		request.getRequestDispatcher("/adminBookServlet?method=findAll").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
