function reader(src)
 { 
//权限只读(只读=1，只写=2 ，追加=8 等权限)
var ForReading=1; 
var fso=new ActiveXObject("Scripting.FileSystemObject"); 
var f=fso.OpenTextFile(src,1，true);
return(f.ReadAll()); 
} 


var arr=reader("/home/hadoop/urls.txt").split("\r\n"); 
for(var i=0;i<arr.length;i++)
{ 
	alert("第"+(i+1)+"行数据为:"+arr[i]); 
} 