package com.liangchu.hbase;
import java.io.IOException; 

import org.apache.hadoop.hbase.HBaseConfiguration; 
import org.apache.hadoop.hbase.client.Get; 
import org.apache.hadoop.hbase.client.HTable; 
import org.apache.hadoop.hbase.client.Put; 
import org.apache.hadoop.hbase.client.Result; 
import org.apache.hadoop.hbase.client.ResultScanner; 
import org.apache.hadoop.hbase.client.Scan; 
import org.apache.hadoop.hbase.util.Bytes; 

// Class that has nothing but a main. 
// Does a Put, Get and a Scan against an hbase table. 
public class HbaseClient { 
public static void main(String[] args) throws IOException { 
//这个就是我们连接数据库的关键，其实就是相当于jdbc，只不过他读取的配置文件就是咱们放在CLASSPATH中的那个两个xml 
HBaseConfiguration config = new HBaseConfiguration(); 
//确定你要往哪个表存数据，就是这个't1' 
HTable table = new HTable(config, "t1"); 
                //你存入数据的row 
Put p = new Put(Bytes.toBytes("myfirstrow")); 
                //将你要存入的数据“yes”，存入到“f1”这个列簇的“perfect”下 
p.add(Bytes.toBytes("f1"), Bytes.toBytes("perfect"), Bytes.toBytes("yes")); 
                //执行，相当于commit 
table.put(p); 

//*************************************************************************************::
                //Get方法，相当于sql中的select 
Get g = new Get(Bytes.toBytes("myfirstrow")); 
Result r = table.get(g); 
byte[] value = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("perfect")); 

String valueStr = Bytes.toString(value); 
                //打印出来的结果当然是“yes” 
System.out.println("GET: " + valueStr); 

//Scan这个方法其实是为了查询数据库的结构，因为我们可以通过HAdmin去对结构进行修改。 
Scan s = new Scan(); 
s.addColumn(Bytes.toBytes("f1"), Bytes 
.toBytes("perfect")); 
ResultScanner scanner = table.getScanner(s); 
try { 

for (Result rr = scanner.next(); rr != null; rr = scanner.next()) { 

System.out.println("Found row: " + rr); 
} 


} finally { 

scanner.close(); 
} 
} 
} 