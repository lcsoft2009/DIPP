<html>
<head>
<%@ page language="java" import="java.io.*" %>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>This page for response</title>
</head>
<body>
    <%
    try {
       if (request.getContentLength() > 297) {
           InputStream in = request.getInputStream();
           File f = new File("d:/temp", "test.txt");
           FileOutputStream o = new FileOutputStream(f);
           byte b[] = new byte[1024];
           int n;
           while ((n = in.read(b)) != -1) {
               o.write(b, 0, n);
           }
           o.close();
           in.close();
           out.print("File upload success!");
           } else {
              out.print("No file!");
           }
       } catch (IOException e) {
           out.print("upload error.");
           e.printStackTrace();
       }
    %>
    </body>
</html>