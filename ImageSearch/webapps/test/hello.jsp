<!DOCTYPE html>

<html>
    <head>
        <title>ImageSearch</title>

        <link rel="stylesheet" media="screen" href="@{'/public/stylesheets/main.css'}">
      
        <link rel="shortcut icon" type="image/png" href="@{'/public/images/favicon.png'}">
        <script src="@{'/public/javascripts/jquery-1.6.4.min.js'}" type="text/javascript" charset="${_response_encoding}"></script>

    </head>
    <body>
    <br><br><br>
<h1 align="center">Imager Search</h1>
<br>

<form  action="/myServlet" method="post" enctype ="multipart/form-data" runat="server"> 
<p align="center">
<input type="file" name="UpLoadImage" id="File1" runat="server"/> 
<input type="submit" name="btn" value="Search" id="btn" />
</p>
</form>
   
    </body>
</html>
