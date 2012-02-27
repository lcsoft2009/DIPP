package com.liangchu.http.servlet;
import com.liangchu.image.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liangchu.image.FqImage;

import java.io.*;
import java.util.*;
@SuppressWarnings("serial")
public class HelloServlet extends HttpServlet
{
    private String greeting="Image search";
    public HelloServlet(){}
    public HelloServlet(String greeting)
    {
        this.greeting=greeting;
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {	
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>"+greeting+"</h1>");
        response.getWriter().println("session=" + request.getSession(true).getId());
        

response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
PrintWriter out = response.getWriter();
out.println("<head><title>Just a basic servlet</title></head>");
out.println("<body>");
out.println("<h1>Just a basic servlet</h1>");
out.println ("<p>For instructions on running those samples on your WebSphere应用服务器 open the page:");
out.println("<pre>http://<em>your.server.name</em>/IBMWebAs/samples/index.aspl</pre>");


BufferedImage	im	=	ImageIO.read( new File("/home/hadoop/Desktop/image/1.jpg") );
FqImage imh	=	new FqImage(im);
imh.setColorJu();

//String[] cmd={"/bin/sh","-c","xterm -hold -e hadoop fs -cat /user/hadoop/jpegDB2012/*"};
String cmd="hadoop fs -cat /user/hadoop/jpegDB2013/*";
//String  cmd="hadoop jar Im2Gray.jar   /user/hadoop/jpegDB3.hib   /user/hadoop/jpegDB1022";
Process proc =Runtime.getRuntime().exec(cmd,null,new File("/usr/local/hadoop/"));

BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

// BufferedInputStream bis = new

// BufferedInputStream(proc.getInputStream());

String text = null;

while ((text = br.readLine()) != null) {

   // System.out.println(text);

    String[] strcolorJu=text.split(" ");
    double[] colorJuH = new double[4];
    double[] colorJuS = new double[4];
    double[] colorJuV = new double[4];
  
   for(int i=0;i<strcolorJu.length;i++)
    {
    	if(i>=1 && i<=3)
    	{
    	colorJuH[i]=Double.parseDouble(strcolorJu[i]);
    	}
    	if(i>3 && i<=6)
    	{
    		colorJuS[i-3]=Double.parseDouble(strcolorJu[i]);
    	}
    	if(i>6 && i<=9)
    	{
    		colorJuV[i-6]=Double.parseDouble(strcolorJu[i]);
    	}
    }
    double s=dColorJu(imh,colorJuH,colorJuS,colorJuV);
    out.println("<p>"+s+"</p>"); 
    System.out.println(s);
}
//br.close();
out.println("</body></html>"); 
out.flush();   
    }
    public double dColorJu( FqImage imh, double[] colorJuH,double[] colorJuS,double[] colorJuV){
		double	temph	=	0.0;
		double 	temps 	= 	0.0;
		double 	tempv 	= 	0.0;
		int		i;
		double	wh		=	8.0;
		double	ws		=	3.01;
		double	wv		=	3.01;
		for( i = 1 ; i <= 3 ; i++ ){
			temph += (	( colorJuH[i] - imh.colorJuH[i] ) * ( colorJuH[i] - imh.colorJuH[i] )	);
			temps += (	( colorJuS[i] - imh.colorJuS[i] ) * ( colorJuS[i] - imh.colorJuS[i] )	);
			tempv += (	( colorJuV[i] - imh.colorJuV[i] ) * ( colorJuV[i] - imh.colorJuV[i] ) );
		}
		return	Math.sqrt( temph * wh + temps * ws + tempv * wv );
	}
}