package com.scs.multiplayerplatformer;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ssmith.android.framework.MyEvent;


public class MainWindow extends JFrame implements MouseListener, KeyListener, MouseMotionListener, WindowListener, MouseWheelListener {

	public BufferStrategy bs;
	private MainThread thread;

	public MainWindow(MainThread t) {
		super();

		thread = t;

		if (Statics.FULL_SCREEN) {
			this.setUndecorated(true);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
			device.setFullScreenWindow(this);

		} else {
			this.setSize((int)Statics.SCREEN_WIDTH, (int)Statics.SCREEN_HEIGHT+25); // 25 since we've pushed image down to take into account insets of Window
		}

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.addWindowListener(this);
		this.addMouseWheelListener(this);

		// Set icon
		try {
			ClassLoader cl = this.getClass().getClassLoader();
			InputStream is = cl.getResourceAsStream("icon.png");
			if (is != null) {
				BufferedImage bi = ImageIO.read(is);
				this.setIconImage(bi);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.setVisible(true);
		this.setResizable(false);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(Statics.NAME);

		this.createBufferStrategy(2);
		bs = this.getBufferStrategy();

	}


	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_F1) {
			// Take screenshot
			try {
				Rectangle r = null;
				if (Statics.FULL_SCREEN) {
					r = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
				} else {
					Insets insets = this.getInsets();
					r = new Rectangle(this.getX() + insets.left, this.getY() + insets.top, this.getWidth() - insets.left-insets.right, this.getHeight() - insets.top-insets.bottom);
				}
				BufferedImage image = new Robot().createScreenCapture(r);
				String filename = "StellarForces_Screenshot_" + System.currentTimeMillis() + ".png";
				ImageIO.write(image, "png", new File(filename));
				Statics.p("Screenshot saved as " + filename);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
			return;
		}
		thread.module.onKeyDown(ke.getKeyCode(), ke);
	}


	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.thread.onBackPressed();
		} else {
			thread.module.onKeyUp(ke.getKeyCode(), ke);
		}

	}


	@Override
	public void keyTyped(KeyEvent ke) {
		// For some reason this method desn't get values for the KeyEvent!?
		//throw new RuntimeException("Do not use - keycode is " + ke.getKeyCode());
	}


	@Override
	public void mouseClicked(MouseEvent me) {
		if (me.getButton() == 3) {
			this.thread.onBackPressed();
		} else {
			thread.addEvent(new MyEvent(me));
		}
	}


	@Override
	public void mouseEntered(MouseEvent me) {

	}


	@Override
	public void mouseExited(MouseEvent me) {

	}


	@Override
	public void mousePressed(MouseEvent me) {
		thread.addEvent(new MyEvent(me));

	}


	@Override
	public void mouseReleased(MouseEvent me) {
		thread.addEvent(new MyEvent(me));

	}


	@Override
	public void mouseDragged(MouseEvent me) {
		thread.addEvent(new MyEvent(me));

	}


	@Override
	public void mouseMoved(MouseEvent me) {

	}


	@Override
	public void windowActivated(WindowEvent arg0) {

	}


	@Override
	public void windowClosed(WindowEvent arg0) {
	}


	@Override
	public void windowClosing(WindowEvent arg0) {
		this.thread.setRunning(false);
		//UpdateServerThread.stop_now = true;
		this.dispose();

	}


	@Override
	public void windowDeactivated(WindowEvent arg0) {

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {

	}


	@Override
	public void windowIconified(WindowEvent arg0) {

	}


	@Override
	public void windowOpened(WindowEvent arg0) {

	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe) {
		/*if (thread.module != null) {
			thread.module.mouseWheelMoved(mwe);
		}*/
	}

}
