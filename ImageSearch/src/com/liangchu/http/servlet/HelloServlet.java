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


//BufferedImage	im	=	ImageIO.read( new File("/home/hadoop/Desktop/image/1.jpg") );
//FqImage imh	=	new FqImage(im);
//imh.setColorJu();

//String[] cmd={"/bin/sh","-c","xterm -hold -e hadoop fs -cat /user/hadoop/jpegDB2012/*"};
//String cmd="hadoop fs -cat /user/hadoop/jpegDB2013/*";
String[] cmd={"/bin/sh","-c","xterm -hold -e hadoop jar ImageSearch.jar   /user/hadoop/jpegDB01.hib   /user/hadoop/resultDistance21"};
Process proc =Runtime.getRuntime().exec(cmd,null,new File("/usr/local/hadoop/"));

BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

// BufferedInputStream bis = new

// BufferedInputStream(proc.getInputStream());

String text = null;

while ((text = br.readLine()) != null) {

    System.out.println(text);
}
//br.close();
out.println("</body></html>"); 
out.flush();   
    }
}