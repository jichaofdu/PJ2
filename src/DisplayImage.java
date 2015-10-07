import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DisplayImage extends JFrame{
	public DisplayImage(){
		add(new ImageCanvas());
	}
	public static void main(String[] args){
		JFrame frame = new DisplayImage();
		frame.setTitle("DisplayImage");
		frame.setSize(300,200);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	class ImageCanvas extends JPanel{
		ImageIcon imageIcon = new ImageIcon("picture/mainTable.jpg");
		Image table = imageIcon.getImage();
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			if(table != null){
				g.drawImage(table, 0, 0,getWidth(),getHeight(),this);
				System.out.println("背景已经贴上了");
			}
		}
	}
}
