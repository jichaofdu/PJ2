import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.sound.midi.*;
import javax.swing.*;
/**
 * @author ����
 * BigPanel ����Ϸ���еĽ���
 */
public class BigPanel extends JPanel implements Serializable{
	/**
	 * nowPlayer ��ʾ ��ǰ���е����
	 * whosTurnZhongzhuan ���������ģ���ܵص�ǰ���
	 * timeZhongZhuan ���������ģ�鴫�ݵ���ʱ��Ϣ
	 */
	String nowPlayer = "green";
	JLabel whosTurnZhongzhuan = new JLabel("  ");
	JPanel whosTurnPanelZhongzhuan = new JPanel();
	int[] timeZhongzhuan = new int[1];
	public String getNowPlayer() {
		return nowPlayer;
	}
	/**
	 * @param nowPlayer ��ʾ��ǰ���е����
	 * �˷������ڸı���ʾ���е������Ϣ
	 */
	public void setNowPlayer(String nowPlayer) {
		this.nowPlayer = nowPlayer;
		if(nowPlayer.equals("green")){
			whosTurnZhongzhuan.setText("                             ��ǰ���Ϊ��ɫ");
		}else{
			whosTurnZhongzhuan.setText("                             ��ǰ���Ϊ��ɫ");
		}
	}
	public BigPanel(){
		SmallLeftPanel smallLeftPanel = new SmallLeftPanel();
		SmallRightQiPanPanel smallRightPanel = new SmallRightQiPanPanel();;
        setLayout(new BorderLayout(5,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        smallLeftPanel.setSize(800,720);
        add(smallLeftPanel,BorderLayout.WEST);
		add(smallRightPanel,BorderLayout.EAST);
		setBackground(Color.BLACK);
	}
	/**
	 * @author ����
	 *SmallLeftPanel ������Ϸ�������벿�֣��ṩ�������ܣ������������֡�����ʱ���������
	 */
	public class SmallLeftPanel extends JPanel implements Serializable{
		public int time ; 	
		public SmallLeftPanel(){
			setLayout(new GridLayout(4,1,5,5));
			WhosTurnPanel whosTurnPanel = new WhosTurnPanel();
			TimePanel timePanel = new TimePanel();
			RadioPanel radioPanel = new RadioPanel();
			NowTime nowTime = new NowTime();
			setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
			add(whosTurnPanel);
			add(timePanel);
			add(radioPanel);
			add(nowTime);
			setBackground(Color.BLUE);
			whosTurnPanelZhongzhuan = whosTurnPanel;
		}
		public int getTime() {
			return time;
		}
		/**
		 * @param time ���ĵ�ǰ����ʱʱ������
		 */
		public void setTime(int time) {
			this.time = time;
		}
		/**
		 * @author ����
		 * WhosTurnPanel ������SmallLeftPanel��������ʾ��ǰ���е��������һ��
		 */
		public  class WhosTurnPanel extends JPanel implements Serializable{
			/**
			 * color:Color ȱʡ������ɫ
			 * timeWhos:int ��ǰӦ����ʾ��ʱ��
			 * whosLabel:JLabel ��Panel�ı���
			 * wanJia:JLabel ������ʾ��ǰ���
			 */
			private Color color = Color.WHITE;
			private int timeWhos = time;
			private JLabel whosLabel = new JLabel("                            ��ǰ���е����:                            ");
			private JLabel wanJia = new JLabel("                             ��ǰ���Ϊ��ɫ");
			public WhosTurnPanel(){
				whosTurnZhongzhuan = wanJia;
				add(whosLabel);
				setSize(300,400);
				setLayout(new GridLayout(2,1,2,2));
				add(wanJia);
				setBackground(Color.GREEN);
			}
		}
		/**
		 * @author ����
		 *TimePanel ������SmallLeftPanel���ڽ��е���ʱ������ʱ�䲻��ʱ���о�ʾ
		 */
		public class TimePanel extends JPanel implements Serializable{
			/**
			 * timePanelTime:int ��ǰʣ���ʱ��
			 * timeLabel:int ��Panel��Ҫ��ʾ��ʣ��ʱ���������Ϣ
			 */
			private int[] timePanelTime =  new int[1];
			JLabel timeLabel = new JLabel("-------");
			public TimePanel(){
				timeZhongzhuan = timePanelTime;
				timePanelTime[0] = 30;
				add(timeLabel);
				Timer timer = new Timer(1000,new TimerListener());
				timer.start();
			}
		private class TimerListener implements ActionListener,Serializable{
				public void actionPerformed(ActionEvent e){
					timePanelTime[0]--;
					timeLabel.setText("   ʣ��ʱ���ǣ�" + timePanelTime[0]);
					setBackground(Color.WHITE);
					if(timePanelTime[0] < 20){
						setBackground(Color.ORANGE);
					}
					if(timePanelTime[0] < 10){
						setBackground(Color.RED);
					}
					/**
					 * ��ʱ�������ı���ҽ�ɫ
					 */
					if(timePanelTime[0] < 1){
						timePanelTime[0] = 31;
						if(nowPlayer.equals("red")){
							setNowPlayer("green");
							whosTurnPanelZhongzhuan.setBackground(Color.GREEN);
						}else{
							setNowPlayer("red");
							whosTurnPanelZhongzhuan.setBackground(Color.RED);
						}
					}
					System.out.println("����ʱ  " + timePanelTime[0]);
				}
			}
		}
		public class NowTime extends JPanel implements Serializable{
			private JLabel label = new JLabel("    ��ǰʱ��");
			private String string = "ȱʡʱ��";
			public NowTime(){
				setBackground(Color.YELLOW);
				Date now = new Date(System.currentTimeMillis()); 
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//���Է�����޸����ڸ�ʽ
				String hehe = dateFormat.format( now ); 
				System.out.println(hehe); 
				Calendar c = Calendar.getInstance();//���Զ�ÿ��ʱ���򵥶���
				int year = c.get(Calendar.YEAR); 
				int month = c.get(Calendar.MONTH) + 1; 
				int date = c.get(Calendar.DATE); 
				int hour = c.get(Calendar.HOUR_OF_DAY); 
				int minute = c.get(Calendar.MINUTE); 
				int second = c.get(Calendar.SECOND); 
				string = year + "/" + month + "/" + date + " " +hour + ":" +minute + ":" + second; 
				label.setText(string);
				add(label);
				} 	
		}
		/**
		 * @author ����
		 *RadioPanel ������SmallLeftPanel,���ڱ������ֵĲ�����ֹͣ
		 */
		public class RadioPanel extends JPanel implements Serializable{
			private JLabel label = new JLabel("                     �������֣��ٳ��ϵ�����");
			Sequencer sequencer;
			AudioClip playMusic;
			public RadioPanel(){
				add(label);
				JButton buttonStart = new JButton("��ʼ����");
				JButton buttonStop = new JButton("ֹͣ����");
				setLayout(new GridLayout(3,1,10,10));
				add(buttonStart);
				add(buttonStop);
				setBackground(Color.PINK);
				/**
				 * ���˰�ť��ʼ���ű�������
				 */
				buttonStart.addActionListener(new ActionListener(){///��������ô����
					public void actionPerformed(ActionEvent e){
						try{
							File cdFile = new File("music/music.wav");
							playMusic = Applet.newAudioClip(cdFile.toURI().toURL());
							playMusic.loop();
						}catch(Exception a){
							a.printStackTrace();
						}
					}
					});
				/**
				 * ���˰�ťֹͣ��������
				 */
				buttonStop.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						playMusic.stop();
					}
				});
			}
		}
	}
	/**
	 * @author toshiba
	 *SmallRightPanel ��Ϸ��������
	 */
	public class SmallRightQiPanPanel extends JPanel implements Serializable{
		private BasicQiZi[][] allQiZi = new BasicQiZi[17][17];
		int[] shunXu = new int[1];
		BasicQiZi[] zanshi = new BasicQiZi[1]; 
		public SmallRightQiPanPanel(){
			shunXu[0] = 0;
			ImageIcon icon = new ImageIcon("picture/mainTable.jpg");
			JLabel table = new JLabel(icon);
			table.setBounds(0,0,getWidth(),getHeight());
			setSize(600,600);
			for(int i = 0;i < 17;i++){
				for(int j = 0;j < 17;j++){
					allQiZi[i][j] = new BasicQiZi("default");
					allQiZi[i][j].addMouseListener(new BasicQiZiListener());
					allQiZi[i][j].setB(i);
					allQiZi[i][j].setA(j);
				}
			}
			/**
			 * ���²����Ƕ����ӽ��г�ʼ�����������Ӱڷ���������
			 */
			allQiZi[4][0].setBounds(285,10,28,28);
			allQiZi[4][1].setBounds(264,43,28,28);
			allQiZi[5][1].setBounds(306,43,28,28);
			allQiZi[4][2].setBounds(285 - 2*21,10 + 33*2,28,28);
			allQiZi[5][2].setBounds(285,10 + 33*2,28,28);
			allQiZi[6][2].setBounds(285 + 2*21,10 + 33*2,28,28);
			allQiZi[4][3].setBounds(285 - 3*22,10 + 34*3,28,28);
			allQiZi[5][3].setBounds(285 - 1*21,10 + 34*3,28,28);
			allQiZi[6][3].setBounds(285 + 1*21,10 + 34*3,28,28);
			allQiZi[7][3].setBounds(285 + 3*22,10 + 34*3,28,28);
			allQiZi[12][16].setBounds(285,562-1,28,28);
			allQiZi[11][15].setBounds(285 - 21 *1,562 - 33*1-1,28,28);
			allQiZi[12][15].setBounds(285 + 21,562 - 33*1-1,28,28);
			allQiZi[10][14].setBounds(285 - 2*21,562 - 2*33-1,28,28);
			allQiZi[11][14].setBounds(285 - 0*21,562 - 2*33-1,28,28);
			allQiZi[12][14].setBounds(285 + 2*21,562 - 2*33-1,28,28);
			allQiZi[9][13].setBounds(285 - 3*21 - 2,562 - 3*33-1,28,28);
			allQiZi[10][13].setBounds(285 - 1*21,562 - 3*33-1,28,28);
			allQiZi[11][13].setBounds(285 + 1*21,562 - 3*33-1,28,28);
			allQiZi[12][13].setBounds(285 + 3*21 + 2,562 - 3*33-1,28,28);
			allQiZi[4][0].setWanjia("green");
			allQiZi[4][1].setWanjia("green");
			allQiZi[5][1].setWanjia("green");
			allQiZi[4][2].setWanjia("green");
			allQiZi[5][2].setWanjia("green");
			allQiZi[6][2].setWanjia("green");
			allQiZi[4][3].setWanjia("green");
			allQiZi[5][3].setWanjia("green");
			allQiZi[6][3].setWanjia("green");
			allQiZi[7][3].setWanjia("green");
			allQiZi[9][13].setWanjia("red");
			allQiZi[10][13].setWanjia("red");
			allQiZi[11][13].setWanjia("red");
			allQiZi[12][13].setWanjia("red");
			allQiZi[10][14].setWanjia("red");
			allQiZi[11][14].setWanjia("red");
			allQiZi[12][14].setWanjia("red");
			allQiZi[12][15].setWanjia("red");
			allQiZi[11][15].setWanjia("red");
			allQiZi[12][16].setWanjia("red");
			for(int i = 0;i < 17;i++){
				for(int j = 0;j < 17;j++){
					table.add(allQiZi[i][j]);
				}
			}
			int k = 0;
			for(int j = 4;j < 9;j++){
				for(int i = k;i < (13 - k) + 4;i++){
					allQiZi[i][j].setWanjia("emptyPosition");
					allQiZi[i][j].setBounds(25 + i*43-k*21+1,148 + 34*(j-4),28,28);
					if(i < 12 - k){
						table.add(allQiZi[i][j]);
					}
				}
				k++;
			}
			for(int f = 4;f < 14;f++){
				allQiZi[f][9].setWanjia("emptyPosition");
				allQiZi[f][9].setBounds(90 + 43*(f-4) + 1 ,320,28,28);
			}
			for(int f = 4;f < 15;f++){
				allQiZi[f][10].setWanjia("emptyPosition");
				allQiZi[f][10].setBounds(69 + 43*(f-4) + 1 ,355,28,28);
			}
			for(int f = 4;f < 16;f++){
				allQiZi[f][11].setWanjia("emptyPosition");
				allQiZi[f][11].setBounds(48 + 43*(f-4) + 1 ,392,28,28);
			}
			for(int f = 4;f < 17;f++){
				allQiZi[f][12].setWanjia("emptyPosition");
				allQiZi[f][12].setBounds(27 + 43*(f-4) ,425,28,28);
			}
			/**
			 * ���ӷ��ý�������table���̼���
			 */
			add(table);
		}
		/**
		 * ������ӵĶ�̬
		 * @author ����
		 *
		 */
		private class BasicQiZiListener extends MouseAdapter implements  Serializable{
			public void mouseClicked(MouseEvent e){
				System.out.println("��������������");
				BasicQiZi thisQiZi = ((BasicQiZi)(e.getSource()));
				BasicQiZi zanshiQiZi = null;
				int b = thisQiZi.getB();
				int a = thisQiZi.getA();
				if(shunXu[0] % 2 == 0){	
					if(thisQiZi.getWanjia().equals(nowPlayer)){
						zanshi[0] = thisQiZi;
						if(!(thisQiZi.getWanjia().equals("default"))&(!thisQiZi.getWanjia().equals("emptyPosition"))&(!thisQiZi.getWanjia().equals("position"))){
							basicTiao(allQiZi,b,a);
							jianGeAndLianTiao(allQiZi, b, a);
						}else{
						}
					}else{
					}
				}else{
					if(thisQiZi.getWanjia().equals("red")|thisQiZi.getWanjia().equals("green")){
						for(int i = 0;i < 17;i++){
							for(int j = 0;j <17;j++){
								if(allQiZi[i][j].getWanjia().equals("position")){
									allQiZi[i][j].setWanjia("emptyPosition");
								}
							}
						}
					}
					if(thisQiZi.getWanjia().equals("position")){
						thisQiZi.setWanjia(nowPlayer);
						for(int i = 0;i < 17;i++){
							for(int j = 0;j <17;j++){
								if(allQiZi[i][j].getWanjia().equals("position")){
									allQiZi[i][j].setWanjia("emptyPosition");
								}
							}
						}
						zanshi[0].setWanjia("emptyPosition");
						if(nowPlayer.equals("green")){
							setNowPlayer("red");
							System.out.println("��ǰ����Ѿ�ת��Ϊ��ɫ");
							whosTurnZhongzhuan.setText("                             ��ǰ���Ϊ��ɫ");
							timeZhongzhuan[0] = 31;
							whosTurnPanelZhongzhuan.setBackground(Color.RED);
							
						}
						else{
							setNowPlayer("green");
							System.out.println("��ǰ����Ѿ�ת��Ϊ��ɫ");
							whosTurnZhongzhuan.setText("                             ��ǰ���Ϊ��ɫ");
							timeZhongzhuan[0] = 31;
							whosTurnPanelZhongzhuan.setBackground(Color.GREEN);
						}
					}
				}
				shunXu[0]++;
				/**
				 * ÿ���й�һ��֮�����Ӯ�����ж�
				 */
				win(allQiZi);
			}
			/**
			 * ����������ڽ�����Ӯ���ж�
			 * @param allQiZi ����Ϸ�����Ӷ���ļ���
			 */
			public void win(BasicQiZi[][] allQiZi){
				if(			
				(allQiZi[4][0].getWanjia().equals("red"))&
				(allQiZi[4][1].getWanjia().equals("red"))&
				(allQiZi[5][1].getWanjia().equals("red"))&
				(allQiZi[4][2].getWanjia().equals("red"))&
				(allQiZi[5][2].getWanjia().equals("red"))&
				(allQiZi[6][2].getWanjia().equals("red"))&
				(allQiZi[4][3].getWanjia().equals("red"))&
				(allQiZi[5][3].getWanjia().equals("red"))&
				(allQiZi[6][3].getWanjia().equals("red"))&
				(allQiZi[7][3].getWanjia().equals("red"))
						){
					JOptionPane.showMessageDialog(null,"��ɫ���Ӯ������Ϸ");
				}
				if(
						(allQiZi[9][13].getWanjia().equals("green"))&
						(allQiZi[10][13].getWanjia().equals("green"))&
						(allQiZi[11][13].getWanjia().equals("green"))&
						(allQiZi[12][13].getWanjia().equals("green"))&
						(allQiZi[10][14].getWanjia().equals("green"))&
						(allQiZi[11][14].getWanjia().equals("green"))&
						(allQiZi[12][14].getWanjia().equals("green"))&
						(allQiZi[12][15].getWanjia().equals("green"))&
						(allQiZi[11][15].getWanjia().equals("green"))&
						(allQiZi[12][16].getWanjia().equals("green"))			
						){
					JOptionPane.showMessageDialog(null,"��ɫ���Ӯ������Ϸ");
				}
			}
		}
		/**
		 * �����������ʵ�ֻ���Ų��
		 * @param table ��Ϸ�����ӵļ���
		 * @param b �����ӵ�������
		 * @param a �����ӵĺ�����
		 */
	    public void basicTiao(BasicQiZi[][] table, int b, int a){
			try{
				if(table[b-1][a-1].getWanjia().equals("emptyPosition")){
					table[b-1][a-1].setWanjia("position");
				}	
			}catch(ArrayIndexOutOfBoundsException e){	
			}
			try{	
				if(table[b-1][a].getWanjia().equals("emptyPosition")){
					table[b-1][a].setWanjia("position");	
				}
			}catch(ArrayIndexOutOfBoundsException e){	
			}
			try{
				if(table[b][a-1].getWanjia().equals("emptyPosition")){
					table[b][a-1].setWanjia("position");
				}
			}catch(ArrayIndexOutOfBoundsException e){	
			}
			try{
				if(table[b][a+1].getWanjia().equals("emptyPosition")){
					table[b][a+1].setWanjia("position");
				} 
			}catch(ArrayIndexOutOfBoundsException e){	
			}
			try{
				if(table[b+1][a].getWanjia().equals("emptyPosition")){
					table[b+1][a].setWanjia("position");
				}    
			}catch(ArrayIndexOutOfBoundsException e){	
			}
			try{
				if(table[b+1][a+1].getWanjia().equals("emptyPosition")){
					table[b+1][a+1].setWanjia("position");
				}	
			}catch(ArrayIndexOutOfBoundsException e){	
			}
	    }
	    /**
	     * �����������ʵ�ּ����������
	     * @param table ��Ϸ�����ӵļ���
	     * @param b ��ǰ���ӵ�������
	     * @param a ��ǰ���ӵĺ�����
	     */
		public void jianGeAndLianTiao(BasicQiZi[][] table, int b, int a){
			try{
				if((!(table[b-1][a-1].getWanjia().equals("default")))&(!(table[b-1][a-1].getWanjia().equals("emptyPosition")))&(!table[b-1][a-1].getWanjia().equals("position"))&table[b-2][a-2].getWanjia().equals("emptyPosition")){
					table[b-2][a-2].setWanjia("position");
					jianGeAndLianTiao(table, b-2, a-2);
				}
			}catch(ArrayIndexOutOfBoundsException e){	
			}
			try{
				if((!(table[b+1][a+1].getWanjia().equals("default")))&(!(table[b+1][a+1].getWanjia().equals("emptyPosition")))&(!table[b+1][a+1].getWanjia().equals("position"))&table[b+2][a+2].getWanjia().equals("emptyPosition")){
					table[b+2][a+2].setWanjia("position");
					jianGeAndLianTiao(table, b+2, a+2);
				}
			}catch(ArrayIndexOutOfBoundsException e){	
			}
			try{
				if((!(table[b][a-1].getWanjia().equals("default")))&(!(table[b][a-1].getWanjia().equals("emptyPosition")))&(!table[b][a-1].getWanjia().equals("position"))&table[b][a-2].getWanjia().equals("emptyPosition")){
					table[b][a-2].setWanjia("position");
					jianGeAndLianTiao(table, b, a-2);
			}	
			}catch(ArrayIndexOutOfBoundsException e){
			}
			try{
				if((!(table[b][a+1].getWanjia().equals("default")))&(!(table[b][a+1].getWanjia().equals("emptyPosition")))&(!table[b][a+1].getWanjia().equals("position"))&table[b][a+2].getWanjia().equals("emptyPosition")){
					table[b][a+2].setWanjia("position");
					jianGeAndLianTiao(table, b, a+2);
				}	
			}catch(ArrayIndexOutOfBoundsException e){
			}
			try{
				if((!(table[b+1][a].getWanjia().equals("default")))&(!(table[b+1][a].getWanjia().equals("emptyPosition")))&(!table[b+1][a].getWanjia().equals("position"))&table[b+2][a].getWanjia().equals("emptyPosition")){
					table[b+2][a].setWanjia("position");
					jianGeAndLianTiao(table, b+2,a);
				}
			}catch(ArrayIndexOutOfBoundsException e){	
			}
			try{
				if((!(table[b-1][a].getWanjia().equals("default")))&(!(table[b-1][a].getWanjia().equals("emptyPosition")))&(!table[b-1][a].getWanjia().equals("position"))&table[b-2][a].getWanjia().equals("emptyPosition")){
					table[b-2][a].setWanjia("position");
					jianGeAndLianTiao(table, b-2, a);
				}
			}catch(ArrayIndexOutOfBoundsException e){
			}
		}
		/**
		 * @author toshiba
		 *BasicQiZi ������ʾ���ӣ��Լ������ӵ��Բ��ֲ���
		 */
		public class BasicQiZi extends JLabel implements Serializable{
			/**
			 * QIZI_COLOR ϵ�У�������ʾ��ͬ���ӵ���ɫ����ʾ��ͬ���ӵ�״̬��
			 * wanjia ��ʾ����������״̬
			 * b:int ��ʾ���ӵ�������
			 * a:int ��ʾ���ӵĺ�����
			 */
			public final ImageIcon QIZI_GREEN =  new ImageIcon("picture/qizi/GreenQiZi12.png");
			public final ImageIcon QIZI_BLUE =  new ImageIcon("picture/qizi/BlueQiZi12.png");
			public final ImageIcon QIZI_DEEPBLUE =  new ImageIcon("picture/qizi/DeepBlueQiZi12.png");
			public final ImageIcon QIZI_RED =  new ImageIcon("picture/qizi/RedQiZi12.png");
			public final ImageIcon QIZI_YELLOW =  new ImageIcon("picture/qizi/YellowQiZi12.png");
			public final ImageIcon QIZI_PURPLE =  new ImageIcon("picture/qizi/PurpleQiZi12.png");
			public final ImageIcon QIZI_DEFAULT =  new ImageIcon("picture/qizi/defaultQiZi2.png");
			private  String wanjia;
			private int b = -1;
			private int a = -1;
			public int getB() {
				return b;
			}
			/**
			 * @param b ��ʾҪ��ɵ�����
			 */
			public void setB(int b) {
				this.b = b;
			}
			public int getA() {
				return a;
			}
			/**
			 * @param a ��ʾҪ��ɵ�����
			 */
			public void setA(int a) {
				this.a = a;
			}
			public BasicQiZi(String wanjia){
				this.wanjia = wanjia;
				if(wanjia.equals("green")){
					setIcon(QIZI_GREEN);
				}
				else if(wanjia.equals("blue")){
					setIcon(QIZI_BLUE);
				}
				else if(wanjia.equals("deepblue")){
					setIcon(QIZI_DEEPBLUE);
				}
				else if(wanjia.equals("red")){
					setIcon(QIZI_RED);
				}
				else if(wanjia.equals("yellow")){
					setIcon(QIZI_YELLOW);
				}
				else if(wanjia.equals("purple")){
					setIcon(QIZI_PURPLE);
				}
				else if(wanjia.equals("position")){
					setIcon(QIZI_DEFAULT);
				}else{
					setIcon(null);
				}
			}
			public String getWanjia() {
				return wanjia;
			}
			/**
			 * ����������ڸı����ӵ�״̬
			 * @param wanjia ��ʾ����Ҫ��ɵ�״̬
			 */
			public void setWanjia(String wanjia) {
				this.wanjia = wanjia;
				if(wanjia.equals("green")){
					setIcon(QIZI_GREEN);
				}
				else if(wanjia.equals("blue")){
					setIcon(QIZI_BLUE);
				}
				else if(wanjia.equals("deepblue")){
					setIcon(QIZI_DEEPBLUE);
				}
				else if(wanjia.equals("red")){
					setIcon(QIZI_RED);
				}
				else if(wanjia.equals("yellow")){
					setIcon(QIZI_YELLOW);
				}
				else if(wanjia.equals("purple")){
					setIcon(QIZI_PURPLE);
				}
				else if(wanjia.equals("position")){
					setIcon(QIZI_DEFAULT);
				}else{
					setIcon(null);
				}
			}
		}
	}
}
