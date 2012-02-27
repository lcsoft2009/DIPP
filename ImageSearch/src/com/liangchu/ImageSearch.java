package com.liangchu;

import hipi.image.FloatImage;
import hipi.image.ImageHeader;
import hipi.imagebundle.mapreduce.ImageBundleInputFormat;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import com.liangchu.image.FqImage;
public class ImageSearch extends Configured implements Tool{

	public static class MyMapper extends Mapper<ImageHeader, FloatImage, IntWritable, Text>
	{
		
		private Path path;
		private FileSystem fileSystem;
		private Configuration conf;
		public void setup(Context jc) throws IOException
		{
			conf = jc.getConfiguration();
			fileSystem = FileSystem.get(conf);
			path = new Path( conf.get("imageSearch.outdir"));
			fileSystem.mkdirs(path);
		}
		public void map(ImageHeader key, FloatImage value, Context context) throws IOException, InterruptedException{
			if (value != null) {
	
				BufferedImage bufferedImage = new BufferedImage(value.getWidth(), value.getHeight(), BufferedImage.TYPE_INT_RGB);
				float[] data = value.getData();
				int[] rgb = new int[value.getWidth() * value.getHeight()];
				for (int i = 0; i < value.getWidth() * value.getHeight(); i++)
				{
					int r = Math.min(Math.max((int)(data[i * 3] * 255), 0), 255);
					int g = Math.min(Math.max((int)(data[i * 3 + 1] * 255), 0), 255);
					int b = Math.min(Math.max((int)(data[i * 3 + 2] * 255), 0), 255);
					rgb[i] = r << 16 | g << 8 | b;
				}
				bufferedImage.setRGB(0, 0, value.getWidth(), value.getHeight(), rgb, 0, value.getWidth());
				FqImage imh	=	new FqImage(bufferedImage);
				imh.setColorJu();

				
				
				 HTable table = new HTable(conf, "colorJu");
	             Scan s = new Scan();
	             ResultScanner ss = table.getScanner(s);
	    
	             double[] hsv = new double[10];
	             int[] id=new int[100];
	             double[] imageDis=new double[100];
	             int counter=0;
	             for(Result r:ss){
	            	   System.out.println("counter="+counter);
	            	 KeyValue[] kv=r.raw();
	            	    System.out.println("id["+counter+"]="+id[0]);
	            	 id[counter]= Integer.parseInt(new String(kv[0].getRow()));
	            	 for(int i=0;i<kv.length;i++){
				            hsv[i]=Double.parseDouble(new String(kv[i].getValue()));
				        }


			        double[] colorJuH = new double[4];
			        double[] colorJuS = new double[4];
			        double[] colorJuV = new double[4];
			      
			       for(int i=0;i<hsv.length;i++)
			        {
			        	if(i>=1 && i<=3)
			        	{
			        	colorJuH[i]=hsv[i];
			        	}
			        	if(i>3 && i<=6)
			        	{
			        		colorJuS[i-3]=hsv[i];
			        	}
			        	if(i>6 && i<=9)
			        	{
			        		colorJuV[i-6]=hsv[i];
			        	}
			        }
			       counter++;
			       
			       imageDis[counter]=dColorJu(imh,colorJuH,colorJuS,colorJuV);
			        
			}
	             for(int i=0;i<counter;i++)
	             {           	 
	            	 System.out.println("id="+id[i]);
	            	 context.write(new IntWritable(id[i]), new Text(String.valueOf(imageDis[i])));
	             }
		}
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
	public static class MyReducer extends Reducer<IntWritable, Text, IntWritable, Text> {
		// Just the basic indentity reducer... no extra functionality needed at this time
		private FileSystem fileSystem;
		private Path path;
		private ImageHeader head;
		private Configuration conf;
		
		public void setup(Context jc) throws IOException
		{
			conf = jc.getConfiguration();
			fileSystem = FileSystem.get(conf);
			path = new Path( conf.get("imageSearch.outdir"));
			fileSystem.mkdirs(path);
		}
		
		public void reduce(IntWritable key, Iterable<Text> values, Context context)
		throws IOException, InterruptedException
		{
			for (Text value : values) {
				context.write(key, value);
			}
		}
		
	}
		public int run(String[] args) throws Exception
		{	

			// Read in the configurations
			if (args.length < 1)
			{
				System.out.println("Usage: imageSearch  <outputdir> ");
				System.exit(0);
			}


			// Setup configuration
			Configuration conf = new Configuration();

			// set the dir to output the jpegs to
			String inputPath = args[0];
			String outputPath = args[1];
			
			conf.setStrings("imageSearch.outdir", outputPath);

			Job job = new Job(conf, "imageSearch");
			job.setJarByClass(ImageSearch.class);
			job.setMapperClass(MyMapper.class);
			job.setReducerClass(MyReducer.class);

			// Set formats     
			job.setOutputKeyClass(IntWritable.class);
			job.setOutputValueClass(Text.class);
			job.setInputFormatClass(ImageBundleInputFormat.class);
			// Set out/in paths

			FileOutputFormat.setOutputPath(job, new Path(outputPath));
			FileInputFormat.setInputPaths(job, new Path(inputPath));

			job.setNumReduceTasks(1);
			System.exit(job.waitForCompletion(true) ? 0 : 1);
			return 0;
		}
		public static void removeDir(String path, Configuration conf) throws IOException {
			Path output_path = new Path(path);

			FileSystem fs = FileSystem.get(conf);

			if (fs.exists(output_path)) {
				fs.delete(output_path, true);
			}
		}
		public static void main(String[] args) throws Exception {
			int res = ToolRunner.run(new ImageSearch(), args);
			System.exit(res);
		}
	}

