<!DOCTYPE html>

<html>
    <head>
        <title>#{get 'title' /}</title>

        <link rel="stylesheet" media="screen" href="@{'/public/stylesheets/main.css'}">
      
        <link rel="shortcut icon" type="image/png" href="@{'/public/images/favicon.png'}">
        <script src="@{'/public/javascripts/jquery-1.6.4.min.js'}" type="text/javascript" charset="${_response_encoding}"></script>

    </head>
    <body>
    <br><br><br>
<h1 align="center">Imager</h1>
<br>

<form  method="POST" enctype ="multipart/form-data" runat="server"> 
<p align="center">
<input id="File1" runat="server" name="UpLoadFile" type="file" /> 
<input type="button" name="btn" value="test" id="btn" />
<input type="submit" name="Button1" value="UpLoad" id="Button1" />
</p>
</form>
   
    </body>
</html>
