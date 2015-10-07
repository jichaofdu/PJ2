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
 * @author 冀超
 * BigPanel 是游戏进行的界面
 */
public class BigPanel extends JPanel implements Serializable{
	/**
	 * nowPlayer 表示 当前进行的玩家
	 * whosTurnZhongzhuan 用于向各个模块攒地当前玩家
	 * timeZhongZhuan 用于向各个模块传递倒计时信息
	 */
	String nowPlayer = "green";
	JLabel whosTurnZhongzhuan = new JLabel("  ");
	JPanel whosTurnPanelZhongzhuan = new JPanel();
	int[] timeZhongzhuan = new int[1];
	public String getNowPlayer() {
		return nowPlayer;
	}
	/**
	 * @param nowPlayer 表示当前进行的玩家
	 * 此方法用于改变提示栏中的玩家信息
	 */
	public void setNowPlayer(String nowPlayer) {
		this.nowPlayer = nowPlayer;
		if(nowPlayer.equals("green")){
			whosTurnZhongzhuan.setText("                             当前玩家为绿色");
		}else{
			whosTurnZhongzhuan.setText("                             当前玩家为红色");
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
	 * @author 冀超
	 *SmallLeftPanel 整个游戏界面的左半部分，提供辅助功能，包括背景音乐。倒计时。玩家提醒
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
		 * @param time 更改当前倒计时时间数字
		 */
		public void setTime(int time) {
			this.time = time;
		}
		/**
		 * @author 冀超
		 * WhosTurnPanel 隶属于SmallLeftPanel，用于提示当前进行的玩家是哪一方
		 */
		public  class WhosTurnPanel extends JPanel implements Serializable{
			/**
			 * color:Color 缺省背景颜色
			 * timeWhos:int 当前应该显示的时间
			 * whosLabel:JLabel 本Panel的标题
			 * wanJia:JLabel 文字提示当前玩家
			 */
			private Color color = Color.WHITE;
			private int timeWhos = time;
			private JLabel whosLabel = new JLabel("                            当前进行的玩家:                            ");
			private JLabel wanJia = new JLabel("                             当前玩家为绿色");
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
		 * @author 冀超
		 *TimePanel 隶属于SmallLeftPanel用于进行倒计时，并在时间不足时进行警示
		 */
		public class TimePanel extends JPanel implements Serializable{
			/**
			 * timePanelTime:int 当前剩余的时间
			 * timeLabel:int 本Panel上要显示的剩余时间的文字信息
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
					timeLabel.setText("   剩余时间是：" + timePanelTime[0]);
					setBackground(Color.WHITE);
					if(timePanelTime[0] < 20){
						setBackground(Color.ORANGE);
					}
					if(timePanelTime[0] < 10){
						setBackground(Color.RED);
					}
					/**
					 * 在时间结束后改变玩家角色
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
					System.out.println("倒计时  " + timePanelTime[0]);
				}
			}
		}
		public class NowTime extends JPanel implements Serializable{
			private JLabel label = new JLabel("    当前时间");
			private String string = "缺省时间";
			public NowTime(){
				setBackground(Color.YELLOW);
				Date now = new Date(System.currentTimeMillis()); 
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
				String hehe = dateFormat.format( now ); 
				System.out.println(hehe); 
				Calendar c = Calendar.getInstance();//可以对每个时间域单独修
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
		 * @author 冀超
		 *RadioPanel 隶属于SmallLeftPanel,用于背景音乐的播放与停止
		 */
		public class RadioPanel extends JPanel implements Serializable{
			private JLabel label = new JLabel("                     背景音乐：操场上的夏天");
			Sequencer sequencer;
			AudioClip playMusic;
			public RadioPanel(){
				add(label);
				JButton buttonStart = new JButton("开始播放");
				JButton buttonStop = new JButton("停止播放");
				setLayout(new GridLayout(3,1,10,10));
				add(buttonStart);
				add(buttonStop);
				setBackground(Color.PINK);
				/**
				 * 按此按钮开始播放背景音乐
				 */
				buttonStart.addActionListener(new ActionListener(){///这里是怎么回事
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
				 * 按此按钮停止播放音乐
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
	 *SmallRightPanel 游戏的主界面
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
			 * 以下部分是对棋子进行初始化，并将棋子摆放在棋盘上
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
			 * 棋子放置结束，将table棋盘加上
			 */
			add(table);
		}
		/**
		 * 监控棋子的动态
		 * @author 冀超
		 *
		 */
		private class BasicQiZiListener extends MouseAdapter implements  Serializable{
			public void mouseClicked(MouseEvent e){
				System.out.println("鼠标点在了棋子上");
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
							System.out.println("当前玩家已经转变为红色");
							whosTurnZhongzhuan.setText("                             当前玩家为红色");
							timeZhongzhuan[0] = 31;
							whosTurnPanelZhongzhuan.setBackground(Color.RED);
							
						}
						else{
							setNowPlayer("green");
							System.out.println("当前玩家已经转变为绿色");
							whosTurnZhongzhuan.setText("                             当前玩家为绿色");
							timeZhongzhuan[0] = 31;
							whosTurnPanelZhongzhuan.setBackground(Color.GREEN);
						}
					}
				}
				shunXu[0]++;
				/**
				 * 每进行过一轮之后对输赢进行判断
				 */
				win(allQiZi);
			}
			/**
			 * 这个方法用于进行输赢的判断
			 * @param allQiZi 是游戏中棋子对象的集合
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
					JOptionPane.showMessageDialog(null,"红色玩家赢得了游戏");
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
					JOptionPane.showMessageDialog(null,"绿色玩家赢得了游戏");
				}
			}
		}
		/**
		 * 这个方法用于实现基本挪动
		 * @param table 游戏中棋子的集合
		 * @param b 是棋子的纵坐标
		 * @param a 是棋子的横坐标
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
	     * 这个方法用于实现间隔跳与连跳
	     * @param table 游戏中棋子的集合
	     * @param b 当前棋子的纵坐标
	     * @param a 当前棋子的横坐标
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
		 *BasicQiZi 用于显示棋子，以及对棋子的以部分操作
		 */
		public class BasicQiZi extends JLabel implements Serializable{
			/**
			 * QIZI_COLOR 系列，用于显示不同棋子的颜色来表示不同棋子的状态；
			 * wanjia 表示棋子所属的状态
			 * b:int 表示棋子的纵坐标
			 * a:int 表示棋子的横坐标
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
			 * @param b 表示要变成的数字
			 */
			public void setB(int b) {
				this.b = b;
			}
			public int getA() {
				return a;
			}
			/**
			 * @param a 表示要变成的数字
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
			 * 这个方法用于改变棋子的状态
			 * @param wanjia 表示棋子要变成的状态
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
