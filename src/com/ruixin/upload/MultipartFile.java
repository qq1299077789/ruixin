package com.ruixin.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.ruixin.config.HandleException;
import com.ruixin.util.PropertiesUtil;
import com.ruixin.util.StringUtil;
/**
 * @author ruixin
 * 文件上传类
 */
public class MultipartFile {

	//文件名
	private String fileName;
	//文件重定义路径
	private String filePath;
	//文件自定义路径
	private String baseDir;
	private HttpServletRequest request;
	private DiskFileItemFactory factory;
	//存储保存文件后的路径
	private List<String> filePaths=new ArrayList<String>(); 
	private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
	
	public MultipartFile(HttpServletRequest request){
		this.request=request;
		this.baseDir=PropertiesUtil.getProperty("file.uploadPath");
		if(StringUtil.isBlank(this.baseDir)){
			this.baseDir="/upload";
		}
		this.filePath=this.baseDir+File.pathSeparator+dateFormat.format(new Date());
		//获得磁盘文件条目工厂  
		this.factory = new DiskFileItemFactory();
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getRealPath(){
		return request.getRealPath(baseDir);
	}
	
	/**
	 * 获取文件上传Stream
	 * @throws IOException 
	 * @throws FileUploadException 
	 */
	@SuppressWarnings("deprecation")
	public FileItemIterator getFileItemIterator() throws FileUploadException, IOException {
		// 获取文件需要上传到的路径
		filePath = request.getRealPath(baseDir);
		File file=new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		factory.setRepository(file);
		// 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
		factory.setSizeThreshold(1024 * 1024);
		// 高水平的API文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);
		FileItemIterator fileItemIterator = upload.getItemIterator(request);
		return fileItemIterator;
	}
	
	/**
	 * 文件上传
	 * @param keepOriginalName 是否保持原文件名
	 * @throws IOException 
	 * @throws FileUploadException 
	 */
	public List<String> upload(boolean keepOriginalName) throws FileUploadException, IOException{
        FileItemIterator fileItemIterator = getFileItemIterator();
        while (fileItemIterator.hasNext()) {
        	FileItemStream fileItemStream=fileItemIterator.next();
        	//处理图片，电影等文件
        	if(!fileItemStream.isFormField()){
       			String value = fileItemStream.getName();
        		//索引到最后一个反斜杠  
        		int start = value.lastIndexOf("\\");  
        		//截取上传文件的字符串名字，加1是去掉反斜杠
        		fileName = value.substring(start+1);        			
        		if(keepOriginalName){
        			fileName = UUID.randomUUID()+fileName.substring(fileName.lastIndexOf("."));
        		}
                String path=uploadFile(fileItemStream);
                filePaths.add(path);
        	}
		}
        return filePaths;
	}
	
	/**
	 * 文件上传
	 * @param fileItemStream
	 * @throws IOException
	 */
	public String uploadFile(FileItemStream fileItemStream) throws IOException{
		String path=filePath+File.pathSeparator+fileName;
		OutputStream out=null;
		InputStream in=null;
		try{
			out = new FileOutputStream(new File(filePath,fileName));  
			in=fileItemStream.openStream();
			int length = 0 ;  
			byte [] buf = new byte[1024];
			while((length = in.read(buf))!=-1){  
				//在buf数组中取出数据写到(输出流)磁盘上  
				out.write(buf, 0, length);  
			} 
		}catch(Exception e){
			HandleException.getInstance().handler("文件上传错误", e);
		}finally {
			if(out!=null){
				out.close();
			}
			if(in!=null){
				in.close();
			}
		}
		return path;
	}
}
