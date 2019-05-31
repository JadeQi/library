package cn.itcast.estore.web.servlet;

import java.io.File;
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

/**
 * 修改图书的Servlet:
 */
public class UpdateBookServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.创建磁盘文件项工厂.
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
		// 2.创建核心解析类:
		ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
		// 3.解析request:
		// 创建一个Map集合用于接收普通项的数据:
		Map<String,String> map = new HashMap<String,String>();
		// 创建图书的对象:
		Book book = new Book();
		String fileName = null;
		try {
			List<FileItem> list = servletFileUpload.parseRequest(request);
			// 遍历集合:
			for (FileItem fileItem : list) {
				// 判断文件项是普通项还是文件上传项:
				if(fileItem.isFormField()){
					// 普通项:
					// 获得普通项名称:
					String name = fileItem.getFieldName();
					// 获得 普通项的值:
					String value = fileItem.getString("UTF-8");
					// 将数据存放到Map集合中.
					map.put(name, value);
				}else{
					// 文件上传项:
					// 获得文件名称:
					fileName = fileItem.getName();
					if(fileName != null && !"".equals(fileName)){
						// 获得book_img的磁盘绝对路径:
						String path = this.getServletContext().getRealPath("/book_img");
						// 删除原有图片:
						// 获得原有图片的路径:
						String image = map.get("image");
						// 获得 / 的位置:
						int idx = image.lastIndexOf("/");
						// 获得原文件名
						String fName = image.substring(idx + 1);
						File file = new File(path+"\\"+fName);
						if(file.exists()){
							file.delete();
						}
						// 上传新图片:
						// 获得文件输入流:
						InputStream is = fileItem.getInputStream();
						// 创建一个输出流:
						OutputStream os = new FileOutputStream(path+"\\"+fileName);
						// 两个流对接:
						int len = 0;
						byte[] b = new byte[1024];
						while((len = is.read(b))!=-1){
							os.write(b, 0, len);
						}
						is.close();
						os.close();
					}
				}
			}
			// 封装数据:
			BeanUtils.populate(book, map);
			// 如果选择图片:需要重新设置图片路径.
			if(fileName != null && !"".equals(fileName)){
				book.setImage("book_img/"+fileName);
			}
			// 调用业务层:
			BookService bookService = new BookService();
			bookService.update(book);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 页面跳转:
		request.getRequestDispatcher("/adminBookServlet?method=findAll").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
