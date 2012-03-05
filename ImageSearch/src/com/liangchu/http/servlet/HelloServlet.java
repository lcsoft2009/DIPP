package com.liangchu.http.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.liangchu.image.FqImage;
import com.liangchu.hbase.*;
@SuppressWarnings("serial")
public class HelloServlet extends HttpServlet {
	private String greeting = "Image search";
	 File tmpDir = new File("/home/hadoop/Desktop/imageSearch/tmp");//初始化上传文件的临时存放目录
	 File saveDir = new File("/home/hadoop/Desktop/imageSearch/");//初始化上传文件后的保存目录
	 File fullFile=null;
	public HelloServlet() {
		 if(!tmpDir.isDirectory())
		        tmpDir.mkdir();
		    if(!saveDir.isDirectory())
		        saveDir.mkdir();
	}

	public HelloServlet(String greeting) {
		this.greeting = greeting;
		 if(!tmpDir.isDirectory())
		        tmpDir.mkdir();
		    if(!saveDir.isDirectory())
		        saveDir.mkdir();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		   try {  
	            // Create a factory for disk-based file items  
	            DiskFileItemFactory factory = new DiskFileItemFactory();  
	  
	  
	            // Create a new file upload handler  
	            ServletFileUpload upload = new ServletFileUpload(factory);  
	  
	  
	            List<FileItem> items = upload.parseRequest(request);// 得到所有的文件  
	            Iterator<FileItem> i = items.iterator();  
	            while (i.hasNext()) {  
	                FileItem fi = (FileItem) i.next();  
	                String fileName = fi.getName();  
	                if (fileName != null) {  
	                     fullFile = new File(fi.getName());  
	                    File savedFile = new File(saveDir, fullFile.getName());  
	                    fi.write(savedFile);  
	                }               
	            }   
	            System.out.println(saveDir+"/"+fullFile.getName());
	        } catch (Exception e) {  
	            // 可以跳转出错页面  
	            e.printStackTrace();  
	        }  
	        
	        
	        
	        
	
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("<h1>" + greeting + "</h1>");
		response.getWriter().println(
				"session=" + request.getSession(true).getId());

		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		out.println("<head><title>Image Search In Hadoop</title></head>");
		out.println("<body>");

		out.println("<pre>http://<em>your.server.name</em>/IBMWebAs/samples/index.aspl</pre>");

		long time1 = System.currentTimeMillis();

		BufferedImage im = ImageIO.read(new File(saveDir+"/"+fullFile.getName()));

		FqImage imh = new FqImage(im);

	    imh.setColorJu();

		

		
		String[] url = new String[100];
		
		 String tablename = "dis";
         String[] familys = {"id", "data"};
         try {
			HBaseDB.creatTable(tablename, familys);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HTable table = new HTable("imageDB");
		Scan s = new Scan();
		ResultScanner ss = table.getScanner(s);
		int counter = 0;
		for (Result r : ss) {
		
			 KeyValue[] kv=r.raw();
			 
			 double[] hsv = new double[10];
			 double[] imageDis = new double[100];
				
				double[] colorJuH = new double[4];
				double[] colorJuS = new double[4];
				double[] colorJuV = new double[4];
				Get get = new Get(kv[0].getRow());
				get.addColumn(Bytes.toBytes("url"), Bytes.toBytes(""));
				url[counter] = Bytes.toString(table.get(get).list().get(0).getValue());
			
				System.out.println(Bytes.toString(table.get(get).list().get(0).getValue()));
				Get get1 = new Get(kv[0].getRow());
				get1.addColumn(Bytes.toBytes("hsv"), Bytes.toBytes("h1"));	
				hsv[0] = Double.parseDouble(new String(table.get(get1).list().get(0).getValue()));
                System.out.println(hsv[0]);
				
				Get get2 = new Get(kv[0].getRow());
				get2.addColumn(Bytes.toBytes("hsv"), Bytes.toBytes("h2"));	
				hsv[1] = Double.parseDouble(new String(table.get(get2).list().get(0).getValue()));
			
				Get get3 = new Get(kv[0].getRow());
				get3.addColumn(Bytes.toBytes("hsv"), Bytes.toBytes("h3"));	
				hsv[2] = Double.parseDouble(new String(table.get(get3).list().get(0).getValue()));
				
				
				Get get4 = new Get(kv[0].getRow());
				get4.addColumn(Bytes.toBytes("hsv"), Bytes.toBytes("s1"));	
				hsv[3] = Double.parseDouble(new String(table.get(get4).list().get(0).getValue()));
				
				Get get5 = new Get(kv[0].getRow());
				get5.addColumn(Bytes.toBytes("hsv"), Bytes.toBytes("s2"));	
				hsv[4] = Double.parseDouble(new String(table.get(get5).list().get(0).getValue()));
				
				Get get6 = new Get(kv[0].getRow());
				get6.addColumn(Bytes.toBytes("hsv"), Bytes.toBytes("s3"));	
				hsv[5] = Double.parseDouble(new String(table.get(get6).list().get(0).getValue()));
				
				Get get7 = new Get(kv[0].getRow());
				get7.addColumn(Bytes.toBytes("hsv"), Bytes.toBytes("v1"));	
				hsv[6] = Double.parseDouble(new String(table.get(get7).list().get(0).getValue()));
				
				Get get8 = new Get(kv[0].getRow());
				get8.addColumn(Bytes.toBytes("hsv"), Bytes.toBytes("v2"));	
				hsv[7] = Double.parseDouble(new String(table.get(get8).list().get(0).getValue()));
				
				Get get9 = new Get(kv[0].getRow());
				get9.addColumn(Bytes.toBytes("hsv"), Bytes.toBytes("v3"));	
				hsv[8] = Double.parseDouble(new String(table.get(get9).list().get(0).getValue()));
				
		
			//	for(int i=0;i<9;i++)
			//	System.out.println(hsv[i]);
				
	//		System.out.println(hsv[0]);
			for (int i = 0; i < hsv.length; i++) {
				if (i >= 1 && i <= 3) {
					colorJuH[i] = hsv[i];
				}
				if (i > 3 && i <= 6) {
					colorJuS[i - 3] = hsv[i];
				}
				if (i > 6 && i <= 9) {
					colorJuV[i - 6] = hsv[i];
				}
			}

			imageDis[counter] = dColorJu(imh, colorJuH, colorJuS, colorJuV);
	//		System.out.println(imageDis[counter]);
			try {
				HBaseDB.addRecord("dis",String.valueOf(imageDis[counter]),"id","",url[counter]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//	out.println("<p>id=" + id[counter] + ",		Distance="+ imageDis[counter] + "</p>");
			counter++;
			System.out.println(counter);
		}

		long time2 = System.currentTimeMillis();
		double diff = (time2 - time1) / 1000.0;
		HTable table2 = new HTable("dis");
		Scan ds = new Scan();
		ResultScanner dis = table2.getScanner(ds);
		 for(Result r:dis){
      	 KeyValue[] kv=r.raw();
      	 System.out.println("ssssssssss     "+kv.length);
      	out.println("<p>"+new String(kv[0].getRow())+" ---------> "+new String(kv[0].getValue())+"</p>");
		 }
		out.println("<p>Search Time: " + diff + "seconds </p>");
		System.out.println("Search Time: " + diff + "seconds ");
	        
	        
	        
	        
	        
	        
	}

	public double dColorJu(FqImage imh, double[] colorJuH, double[] colorJuS,
			double[] colorJuV) {
		double temph = 0.0;
		double temps = 0.0;
		double tempv = 0.0;
		int i;
		double wh = 8.0;
		double ws = 3.01;
		double wv = 3.01;
		for (i = 1; i <= 3; i++) {
			temph += ((colorJuH[i] - imh.colorJuH[i]) * (colorJuH[i] - imh.colorJuH[i]));
			temps += ((colorJuS[i] - imh.colorJuS[i]) * (colorJuS[i] - imh.colorJuS[i]));
			tempv += ((colorJuV[i] - imh.colorJuV[i]) * (colorJuV[i] - imh.colorJuV[i]));
		}
		return Math.sqrt(temph * wh + temps * ws + tempv * wv);
	}
	public void init() throws ServletException {  
	    if(!saveDir.isDirectory())  
	        saveDir.mkdirs();  
	} 
}
