
import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
/**
 * @author 冀超
 * TQ2Main 是游戏进行的主程序
 */
public class TQ2Main extends JFrame implements Serializable{
	public TQ2Main(){}
	BigPanel bigPanel = new BigPanel();
	static TQ2Main gui = new TQ2Main();
	public static void main(String[] args){
		gui.go();
	}
	/**
	 * go 游戏的进行
	 */
	public void go(){
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("菜单");
		JMenuItem caidanMenuItem1 = new JMenuItem("新建");
		JMenuItem caidanMenuItem2 = new JMenuItem("打开");
		JMenuItem caidanMenuItem3 = new JMenuItem("另存为");
		JMenuItem caidanMenuItem4 = new JMenuItem("退出");
		JMenu helpMenu = new JMenu("帮助");
		JMenuItem helpMenuItem1 = new JMenuItem("用户手册");
		JMenuItem helpMenuItem2 = new JMenuItem("关于");
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		fileMenu.add(caidanMenuItem1);
		fileMenu.add(caidanMenuItem2);
		fileMenu.add(caidanMenuItem3);
		fileMenu.add(caidanMenuItem4);
		helpMenu.add(helpMenuItem1);
		helpMenu.add(helpMenuItem2);
		caidanMenuItem1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BigPanel newBigPanel = new BigPanel();
				removeAll();
				add(newBigPanel);
			}
		});
		caidanMenuItem2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser fileOpen = new JFileChooser();
				fileOpen.showOpenDialog(null);
				try {
					FileInputStream file = new FileInputStream(fileOpen.getSelectedFile());
					ObjectInputStream os = new ObjectInputStream(file);
					Object one = os.readObject();
					BigPanel newObject = (BigPanel)one;
					bigPanel = newObject;
					JOptionPane.showMessageDialog(null,"读取成功");
					os.close();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "读取失败");
				}
			}
		});
		
		caidanMenuItem3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showSaveDialog(null);
				try {
					FileOutputStream file = new FileOutputStream(fileChooser.getSelectedFile());
					ObjectOutputStream os = new ObjectOutputStream(file);
					os.writeObject(bigPanel);
					os.close();
					JOptionPane.showMessageDialog(null,"存储成功");
				} catch (Exception e1) {
					JOptionPane.showInternalMessageDialog(null,"文件存储失败");
				}
				
			}
		});
		caidanMenuItem4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null, "您退出了游戏");
				System.exit(0);
			}
		});
		helpMenuItem1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				MethodOfTiaoQi methodOfTiaoQi = new MethodOfTiaoQi();
				methodOfTiaoQi.open();
			}
		});
		helpMenuItem2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				HelpJOptionPane help = new HelpJOptionPane();
				help.openHelp();
			}
		});
		/**
		 * 开始构建游戏界面
		 */
		JFrame frame = new JFrame();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("跳棋程序");

		frame.add(bigPanel);
		frame.setSize(900,700);
		frame.setJMenuBar(menuBar);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	/**
	 * @author 冀超
	 *MethodOfTiaoQi 本类用于显示帮助中跳棋的玩法
	 */
	public class MethodOfTiaoQi implements Serializable{
		/**
		 * explain:String 写的是游戏方法
		 */
		String explain = "跳棋的玩法是：\n       “相邻跳”：\n棋子的移动可以一步步在有直线连接的相邻六个方向进行，如果相邻位置上有任何方的一个棋子，\n该位置直线方向下一个位置是空的，则可以直接跳到该空位上，跳的过程中，只要相同条  \n“等距跳”：棋子的移动可以一步步在有直线连接的相邻六个方向进行，如果在和同一直线上的任意一个空位所构成的线段中，\n只有一个并且位于该线段中间的任何方的棋子，则可以直接跳到那个空位上，跳的过程中，\n只要相同条件满足就可以连续进行";
		public MethodOfTiaoQi(){}
		public void open(){
				JOptionPane.showMessageDialog(null,explain);			
		}
	}
	/**
	 * @author 冀超
	 * HelpJOptionPane 用于显示关于版本信息
	 */
	public class HelpJOptionPane implements Serializable{
		/**
		 * help:String 记录版本信息
		 */
		String help = "版本：其实只有这一个版本啦，不用太在意                \n"
					        + "作者：复旦大学13级软件工程      冀超            \n";
		public HelpJOptionPane(){}
		public void openHelp(){
			JOptionPane.showMessageDialog(null,help);
		}
	}
}
