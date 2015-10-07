
import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
/**
 * @author ����
 * TQ2Main ����Ϸ���е�������
 */
public class TQ2Main extends JFrame implements Serializable{
	public TQ2Main(){}
	BigPanel bigPanel = new BigPanel();
	static TQ2Main gui = new TQ2Main();
	public static void main(String[] args){
		gui.go();
	}
	/**
	 * go ��Ϸ�Ľ���
	 */
	public void go(){
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("�˵�");
		JMenuItem caidanMenuItem1 = new JMenuItem("�½�");
		JMenuItem caidanMenuItem2 = new JMenuItem("��");
		JMenuItem caidanMenuItem3 = new JMenuItem("���Ϊ");
		JMenuItem caidanMenuItem4 = new JMenuItem("�˳�");
		JMenu helpMenu = new JMenu("����");
		JMenuItem helpMenuItem1 = new JMenuItem("�û��ֲ�");
		JMenuItem helpMenuItem2 = new JMenuItem("����");
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
					JOptionPane.showMessageDialog(null,"��ȡ�ɹ�");
					os.close();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "��ȡʧ��");
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
					JOptionPane.showMessageDialog(null,"�洢�ɹ�");
				} catch (Exception e1) {
					JOptionPane.showInternalMessageDialog(null,"�ļ��洢ʧ��");
				}
				
			}
		});
		caidanMenuItem4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null, "���˳�����Ϸ");
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
		 * ��ʼ������Ϸ����
		 */
		JFrame frame = new JFrame();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("�������");

		frame.add(bigPanel);
		frame.setSize(900,700);
		frame.setJMenuBar(menuBar);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	/**
	 * @author ����
	 *MethodOfTiaoQi ����������ʾ������������淨
	 */
	public class MethodOfTiaoQi implements Serializable{
		/**
		 * explain:String д������Ϸ����
		 */
		String explain = "������淨�ǣ�\n       ������������\n���ӵ��ƶ�����һ��������ֱ�����ӵ���������������У��������λ�������κη���һ�����ӣ�\n��λ��ֱ�߷�����һ��λ���ǿյģ������ֱ�������ÿ�λ�ϣ����Ĺ����У�ֻҪ��ͬ��  \n���Ⱦ����������ӵ��ƶ�����һ��������ֱ�����ӵ���������������У�����ں�ͬһֱ���ϵ�����һ����λ�����ɵ��߶��У�\nֻ��һ������λ�ڸ��߶��м���κη������ӣ������ֱ�������Ǹ���λ�ϣ����Ĺ����У�\nֻҪ��ͬ��������Ϳ�����������";
		public MethodOfTiaoQi(){}
		public void open(){
				JOptionPane.showMessageDialog(null,explain);			
		}
	}
	/**
	 * @author ����
	 * HelpJOptionPane ������ʾ���ڰ汾��Ϣ
	 */
	public class HelpJOptionPane implements Serializable{
		/**
		 * help:String ��¼�汾��Ϣ
		 */
		String help = "�汾����ʵֻ����һ���汾��������̫����                \n"
					        + "���ߣ�������ѧ13���������      ����            \n";
		public HelpJOptionPane(){}
		public void openHelp(){
			JOptionPane.showMessageDialog(null,help);
		}
	}
}
