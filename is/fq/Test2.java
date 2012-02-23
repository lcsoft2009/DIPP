package fq;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.*;
import java.applet.*;

public class Test2 extends Applet {

	Image im1;

	public void init() {
		im1 = getImage(getCodeBase(), "images/1.jpg");
	}

	public void paint(Graphics g) {
		
		g.drawImage(im1, 0, 0, this);
	}

}
