/*
 * @(#)TicTacToe.java	1.12 06/02/22
 * 
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

/*
 * @(#)TicTacToe.java	1.12 06/02/22
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.*;
import java.applet.*;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.imageio.ImageIO;

public class Test1 extends Applet {

	Image im1;

	public void init() {
		im1 = getImage(getCodeBase(), "images/1.jpg");
		
		try{
			BufferedImage im	=	ImageIO.read(new File("d:\\image\\f.jpg"));
			FqImage	imh		=	new FqImage(im);
		}catch( Exception e ){
			e.printStackTrace();
		}
		
	}

	public void paint(Graphics g) {
		
		g.drawImage(im1, 0, 0, this);
	}

}



///////////////////////////////////////////////////////////////////////////////////////////////

class FqImage {

	public static final double SOBELDOOR = 80.00;


	public BufferedImage	imh;
	public int 				height;
	public int 				width;
	public Point[][]		points;
	public double[][]		sobels;
	public int[][]			edge;
	public double[]			colorJuH	=	new double[4];
	public double[]			colorJuS	=	new double[4];
	public double[]			colorJuV	=	new double[4];
	public double[]			huJu		=	new double[8];
	
	

	public double getAbsColorDistance( FqImage other ){
		int width	=	FqMath.min(this.width, other.width);
		int	height	=	FqMath.min(this.height, other.height);
		
		int	sum	=	0;
		for( int i = 0 ; i < width ; i++ ){
			for( int j = 0 ; j < height ; j++ ){
				sum += this.points[i][j].pointDistance(other.points[i][j]);
			}
		}
		return	sum;
	}
	
	public double f( int i , int j ){
		double	temp	=	this.points[i][j].getGray();
		return	temp;
	}
	
	public void toSobel(){
		double	fx , fy;
		double	sobel;
		int		i , j;
		for( i = 1 ; i < this.width - 1 ; i++ ){
			for( j = 1 ; j < this.height - 1 ; j++ ){
				fx	=	(	f(i-1,j-1) + 2*f(i-1,j) + f(i-1,j+1)	)	-	(	f(i+1,j-1) + 2*f(i+1,j) + f(i+1,j+1)	);
				fy	=	(	f(i-1,j-1) + 2*f(i,j-1) + f(i+1,j-1)	)	-	(	f(i-1,j+1) + 2*f(i,j+1) + f(i+1,j+1)	);
				this.sobels[i][j]	=	FqMath.max( Math.abs(fx), Math.abs(fy) );			
			}
		}
	}

	public void toEdge(){
		int	i , j;
		for( i = 0 ; i < this.width ; i++ ){
			for( j = 0 ; j < this.height ; j++ ){
				if( this.sobels[i][j] > SOBELDOOR )
					this.edge[i][j] = 1;
				else
					this.edge[i][j] = 0;
			}
		}
	}
		
	public FqImage cutSmallest(){
		final double EDGEDOOR = 4;
		
		int	i , j;
		int left	=	0;
		int	right	=	0;
		int	top		=	0;
		int	bottom	=	0;
		int	count	=	0;
		
		
		for( i = 0 ; i < this.width ; i++ ){
			count = 0;
			for( j = 0 ; j < this.height ; j++ ){
				if( this.edge[i][j] == 1 )
					count++;
			}
			if( count >= EDGEDOOR ){
				left = i;
				break;
			}
		}
		
		for( i = this.width - 1 ; i >= 0 ; i-- ){
			count = 0;
			for( j = 0 ; j < this.height ; j++ ){
				if( this.edge[i][j] == 1 )
					count++;
			}
			if( count >= EDGEDOOR ){
				right = i;
				break;
			}
		}
		
		for( j = 0 ; j < this.height ; j++ ){
			count = 0;
			for( i = 0 ; i < this.width ; i++ ){
				if( this.edge[i][j] == 1 )
					count++;
			}
			if( count >= EDGEDOOR ){
				top = j;
				break;
			}
		}
		
		for( j = this.height - 1 ; j >= 0 ; j-- ){
			count = 0;
			for( i = 0 ; i < this.width ; i++ ){
				if( this.edge[i][j] == 1 )
					count++;
			}
			if( count >= EDGEDOOR ){
				bottom = j;
				break;
			}
		}
		
		BufferedImage subImh	=	this.imh.getSubimage(left, top, right-left , bottom-top);
		return new FqImage(subImh);
	
	
	}
	
	public void setColorJu(){
		int		i , j;
		int 	k;
		double	sumh , sums , sumv;
		
		this.colorJuH[1]	=	0.0;
		this.colorJuS[1]	=	0.0;
		this.colorJuV[1]	=	0.0;
		
//		System.out.println(this.width * this.height);
		for( k = 1 ; k <= 3 ; k++ ){
			sumh = 0.0;
			sums = 0.0;
			sumv = 0.0;
			for( i = 0 ; i < this.width ; i++ ){
				for( j = 0 ; j < this.height ; j++ ){
//					double	temp = this.points[i][j].getHHSV();
//					temp -= this.colorJuH[1];
//					temp = Math.pow(temp, k);
//					sumh += temp;
					sumh += Math.pow(	( this.points[i][j].getHHSV() - this.colorJuH[1] )	,	k	);
					sums += Math.pow(	( this.points[i][j].getSHSV() - this.colorJuS[1] )	,	k	);
					sumv += Math.pow(	( this.points[i][j].getVHSV() - this.colorJuV[1] )	,	k	);
				}
			}
			
			
//			System.out.println(k+":");
			
			this.colorJuH[k] = sumh / ( this.width * this.height );				
			if( k == 2 )
				this.colorJuH[k]	=	Math.sqrt(this.colorJuH[k]);
			if( k == 3 )
				this.colorJuH[k]	=	Math.cbrt(this.colorJuH[k]);
			
			
//			System.out.println(colorJuH[k]);
			
			this.colorJuS[k] = sums / ( this.width * this.height );
			if( k == 2 )
				this.colorJuS[k]	=	Math.sqrt(this.colorJuH[k]);
			if( k == 3 )
				this.colorJuS[k]	=	Math.cbrt(this.colorJuH[k]);
			
			this.colorJuV[k] = sumv / ( this.width * this.height );
			if( k == 2 )
				this.colorJuV[k]	=	Math.sqrt(this.colorJuH[k]);
			if( k == 3 )
				this.colorJuV[k]	=	Math.cbrt(this.colorJuH[k]);
			
//			System.out.println("sumh"+sumh+","+"sums"+sums+","+"sumv"+sumv);
		}
	}
	
	public double dColorJu( FqImage imh ){
		double	temph	=	0.0;
		double 	temps 	= 	0.0;
		double 	tempv 	= 	0.0;
		int		i;
		double	wh		=	5.0;
		double	ws		=	0.01;
		double	wv		=	0.01;
		for( i = 1 ; i <= 3 ; i++ ){
			temph += (	( this.colorJuH[i] - imh.colorJuH[i] ) * ( this.colorJuH[i] - imh.colorJuH[i] )	);
			temps += (	( this.colorJuS[i] - imh.colorJuS[i] ) * ( this.colorJuS[i] - imh.colorJuS[i] )	);
			tempv += (	( this.colorJuV[i] - imh.colorJuV[i] ) * ( this.colorJuV[i] - imh.colorJuV[i] ) );
		}
		return	Math.sqrt( temph * wh + temps * ws + tempv * wv );
	}
	
	public double m( int p , int q ){
		int 	x , y;
		double 	sum = 0;
		for( x = 0 ; x < this.width ; x++ )
			for( y = 0 ; y < this.height ; y++ )
				sum += Math.pow(x,p) * Math.pow(y,q) * this.edge[x][y];
		return sum;
	}
	
	public double miu( int p , int q ){
		int		x , y;
		double 	x0 , y0;
		double	sum = 0;
		x0 = m(1,0) / m(0,0);
		y0 = m(0,1) / m(0,0);		
		for( x = 0 ; x < this.width ; x++ )
			for( y = 0 ; y < this.height ; y++ )
				sum += Math.pow((x-x0), p) * Math.pow((y-y0), q) * this.edge[x][y];
		return sum;
	}
	
	public double eda( int p , int q ){
		int g = p+q+2;
		return miu(p,q) / Math.sqrt( Math.pow( miu(0,0), g ) );
	}
		
	
	public void toHuJu(){
		double	eda20 = eda(2,0);
		double	eda02 = eda(0,2);
		double 	eda22 = eda(2,2);
		double 	eda11 = eda(1,1);
		double	eda30 = eda(3,0);
		double	eda03 = eda(0,3);
		double	eda21 = eda(2,1);
		double	eda12 = eda(1,2);
		
		this.huJu[1]	=	eda20 + eda02;
		this.huJu[2]	=	(eda20-eda02) * (eda20-eda02)	+	4 * eda11 * eda11;
		this.huJu[3]	=	(eda30-3*eda12) * (eda30-3*eda12)	+	(eda03-3*eda21) * (eda03-3*eda21);
		this.huJu[4]	=	(eda30+eda12) * (eda30+eda12)	+	(eda21+eda03) * (eda21+eda03);
		this.huJu[5]	= 	(eda30-3*eda12) * (eda30+eda12) * (	(eda30+eda12)*(eda30+eda12) - 3*(eda21+eda03)*(eda21+eda03)	)
							+	(3*eda21-eda03) * (eda21+eda03) * (	3*(eda30+eda12)*(eda30+eda12) - (eda21+eda03)*(eda21+eda03)	);
		this.huJu[6]	=	(eda20-eda02) * (	(eda30+eda12)*(eda30+eda12) - (eda21+eda03)*(eda21+eda03)	)	+
							4 * eda11 * (eda30+eda12) * (eda21+eda03);
		this.huJu[7]	=	(3*eda21-eda03) * (eda30+eda12) * (	(eda30+eda12)*(eda30+eda12) - 3*(eda21+eda03)*(eda21+eda03)	)	+
							(3*eda12-eda30) * (eda03+eda21) * (	3*(eda30+eda12)*(eda30+eda12) - (eda21+eda03)*(eda21+eda03)	);							
	}
	
	public double dHuJu( FqImage imh ){
		int			i;
		double[]	wHu	=	new double[8];
		wHu[1]		=	1.0;
		wHu[2]		=	1.0;
		wHu[3]		=	1.0;
		wHu[4]		=	1.0;
		wHu[5]		=	1.0;
		wHu[5]		=	1.0;
		wHu[6]		=	1.0;
		wHu[7]		=	1.0;
		double	sum	=	0.0;
		for( i = 1 ; i <= 7 ; i++ )
			sum += wHu[i]	*	( this.huJu[i] - imh.huJu[i] ) * ( this.huJu[i] - imh.huJu[i] );
		return	Math.sqrt(sum) * 10000;
	}
	
	public FqImage( BufferedImage imh ){
		this.imh	=	imh;
		this.height	=	imh.getHeight(null);
		this.width	=	imh.getWidth(null);
		this.points	=	new Point[width][height];
		this.sobels	=	new double[width][height];
		this.edge	=	new int[width][height];

//			ColorModel 		colorModel	=	this.imh.getColorModel(); 
//			WritableRaster 	raster		=	this.imh.getRaster();
			
		int	i,j;
		for( i = 0 ; i < this.width ; i++ ){
			for( j = 0 ; j < this.height ; j++ ){
				this.points[i][j]	=	new Point();
				this.points[i][j].setRGB( imh.getRGB(i, j) );
				this.points[i][j].toRed();
				this.points[i][j].toGreen();
				this.points[i][j].toBlue();
//					this.points[i][j].setRed(	colorModel.getRed(raster.getDataElements(i, j, null))	);
//					this.points[i][j].setGreen(	colorModel.getGreen(raster.getDataElements(i, j, null))	);
//					this.points[i][j].setBlue(	colorModel.getBlue(raster.getDataElements(i, j, null))	);
				this.points[i][j].toGray();
				this.points[i][j].toHSV();
			}
		}
		this.toSobel();
		this.toEdge();
		this.setColorJu();
		this.toHuJu();
	}	
}



//////////////////////////////////////////////////////////////////////////////////////////

class Point {
	private int		alpha;
	private int		red;
	private int		green;
	private int 	blue;
	private int 	rgb;
	private double	hHSV;
	private double	sHSV;
	private double	vHSV;
	private double 	gray;
	
	public Point(){
		this.alpha	=	0;
		this.red	=	0;
		this.green	=	0;
		this.blue	=	0;
		this.rgb	=	0;
		this.hHSV	=	0;
		this.sHSV	=	0;
		this.vHSV	=	0;
		this.gray	=	0;
	}
	
	public void toHSV( int red , int green , int blue ){
		double 	maxRGB	=	FqMath.max( red , green , blue );
		double	minRGB	=	FqMath.min( red , green , blue );
		double	itemp	=	maxRGB;
		double	temp	=	maxRGB	-	minRGB;
		
		if( maxRGB == minRGB ){
			this.hHSV	=	0;
			this.sHSV	=	0;
			this.vHSV	=	maxRGB / 255;
			return;
		}
		
		double	rtemp	=	( itemp - red )	/ temp;
		double	gtemp	=	( itemp - green ) / temp;
		double	btemp	=	( itemp - blue ) / temp;
		
		
		
		this.vHSV	=	itemp / 255;
		this.sHSV	=	temp / itemp;
		if( red == maxRGB ){
			if( green == minRGB )
				this.hHSV	=	5 + btemp;
			else
				this.hHSV	=	1 - gtemp;
		}
		else if( green == maxRGB ){
			if( blue == minRGB )
				this.hHSV	=	1 + rtemp;
			else
				this.hHSV	=	3 - btemp;
		}
		else if( blue == maxRGB ){
			if( red == minRGB )
				this.hHSV	=	3 + gtemp;
			else
				this.hHSV	=	5 - rtemp;
		}
		this.hHSV	*=	60;
		
	}
	
	public void toHSV(){
		this.toHSV( this.red , this.green ,this.blue );
	}
	
	public void toGray( int red , int green , int blue ){
		this.gray = red * 0.3 + green * 0.59 + blue * 0.11;
	}
	
	public void toGray(){
		this.toGray( this.red , this.green , this.blue );
	}
	
	public void toAlpha( int rgb ){
		int temp	=	0xff000000;
		
		rgb	=	rgb & temp;
		this.alpha	=	rgb >> 24;
	}
	
	public void toAlpha(){
		this.toAlpha(this.rgb);
	}
	
	public void toRed( int rgb ){
		this.red	=	( rgb >> 16 ) & 0xff;
	}
	
	public void toRed(){
		this.toRed(this.rgb);
	}
	
	public void toGreen( int rgb ){
		this.green	=	( rgb >> 8 ) & 0xff;
	}
	
	public void toGreen(){
		this.toGreen(this.rgb);
	}
	
	public void toBlue( int rgb ){
		this.blue	=	rgb & 0xff;
	}
	
	public void toBlue(){
		this.toBlue(this.rgb);
	}
	
	public double pointDistance( Point other ){
		double h1		=	this.hHSV * Math.PI / 360;
		double h2		=	other.hHSV * Math.PI / 360;
		
		double temp1	=	this.vHSV - other.vHSV; 
		double temp2	=	this.sHSV*Math.sin(h1) - other.sHSV*Math.sin(h2);
		double temp3	=	this.sHSV*Math.cos(h1) - other.sHSV*Math.cos(h2);		
		
		return	Math.sqrt(temp1*temp1 + temp2*temp2 + temp3*temp3);			
	}
	
	public int getAlpha(){
		return	this.alpha;
	}
	
	public void setRed( int red ){
		this.red	=	red;
	}
	
	public int getRed(){
		return	this.red;
	}
	
	public void setGreen( int green ){
		this.green	=	green;
	}
	
	public int getGreen(){
		return	this.green;
	}
	
	public void setBlue( int blue ){
		this.blue	=	blue;
	}
	
	public int getBlue(){
		return	this.blue;
	}
	
	public void setRGB( int rgb ){
		this.rgb	=	rgb;
	}
	
	public int getRGB(){
		return	this.rgb;
	}
	
	public double getGray(){
		return	this.gray;
	}
	
	public double getHHSV(){
		return	this.hHSV;
	}
	
	public double getSHSV(){
		return 	this.sHSV;
	}
	
	public double getVHSV(){
		return	this.vHSV;
	}
	
}



///////////////////////////////////////////////////////////////////////////////


class FqMath {
	public static int max( int a , int b ){
		return	a > b ? a : b;
	}
	
	public static int max( int a , int b , int c ){
		int temp	=	max( a , b );
		return	temp > c ? temp : c;
	}
	
	public static double max( double a , double b ){
		return	a > b ? a : b;
	}
	
	public static int min( int a , int b ){
		return	a < b ? a : b;
	}
	
	public static int min( int a , int b , int c ){
		int temp	=	min( a , b );
		return	temp < c ? temp : c;
	}

}
