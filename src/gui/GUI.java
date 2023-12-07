package gui;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import gamelogic.ComputerComponent;
import gamelogic.Game;
import model.Chess;
import model.ChessBoard;
import model.Location;
import model.Operation;

	public class GUI extends JFrame implements MouseListener,MouseMotionListener {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	
	boolean start=false;//if a piece is being dragged flag turns to true
	boolean recordflag=false; //if record button is clicked flag turns to true
	boolean replayflag=false; //if replay button is clicked flag turns to true
	int computerplayer=-1;//-1 means human-to-human mode
    Game game=new Game();
    Operation op0=new Operation();
    Chess origin;
    boolean flag=false;
    int player=-1;
    int lx;
    int ly;
    static Queue<Operation> queue = new LinkedList<>();
    Operation opQueue;
    static Queue<Operation> paintQueue = new LinkedList<>();
    
   
	BufferedImage image;

	 {
	 try {
	 image = ImageIO.read(new File("./img/background.jpeg"));
	 } catch (IOException e) {
	  e.printStackTrace();
	 }
	 }

	 class myJPanel extends JPanel{


     /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	@Override
	 public void paint(Graphics g) {
	
	  Graphics2D g2 = (Graphics2D)g;

	  g2.drawImage(image,0,0,1000,750,null);
	  g2.setColor(Color.CYAN);

	  g2.fill3DRect(50, 50, 600, 600, true);


	  g2.setColor(Color.black);
	
	  g2.setStroke(new BasicStroke(3));
	  

	  for(int i=1;i<8;i++) 
	  { if(i==4)continue; 
	  g2.drawLine(350-Math.abs(4-i)*100,100*i-50,350+Math.abs(4-i)*100,100*i-50 );
	  }
	  for(int i=1;i<8;i++) 
	  { if(i==4)continue; 
	  g2.drawLine(100*i-50,350+Math.abs(4-i)*100,100*i-50,350-Math.abs(4-i)*100 );
	  }
	  g2.drawLine(50,350,250,350);
	  g2.drawLine(350,250,350,50);
	  g2.drawLine(450,350,650,350);
	  g2.drawLine(350,650,350,450);

	  g2.setColor(new Color(176, 166, 100, 254));
	  g2.fillRect(26, 26, 649, 23);
	  g2.fillRect(26, 49, 23, 625);
	  g2.fillRect(49, 652, 625, 23);
	  g2.fillRect(652, 49, 23, 603);
	  g2.setColor(Color.black);
	  for(Location l:Location.values()) 
	  {
		  g2.fillOval(l.getColumn()*100-50-5, l.getRow()*100-50-5, 10, 10);
	  }

	g2.setStroke(new BasicStroke(5));
	  g2.drawLine(25,25,675, 25);
	  g2.drawLine(25,675,675, 675);
	  g2.drawLine(25,25,25, 675);
	  g2.drawLine(675,25,675, 675);
      g2.setColor(Color.white);
	  g2.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
      g2.drawString(game.msg, 700, 100);
      if(!replayflag) {
    	  for(Chess c:game.csbd.chessonboard) {
    		  if(flag&&c.l.getColumn()==origin.l.getColumn()&&c.l.getRow()==origin.l.getRow())continue;
    		  if(c.player==Chess.PLAYER_B)
    			  g2.setColor(Color.BLUE);
    		  else if(c.player==Chess.PLAYER_R)
    			  g2.setColor(Color.RED);
    		  g2.fillOval(c.l.getColumn()*100-50-20, c.l.getRow()*100-50-20, 40, 40);
    	 
    	  }
    	  if(flag) {
    		  if(op0.player==Chess.PLAYER_B)
    			  g2.setColor(Color.BLUE);
    		  else if(op0.player==Chess.PLAYER_R)
    			  g2.setColor(Color.RED);
    		  g2.fillOval(lx-40, ly-40, 40, 40);
    	  }
      }else {
    	  	  int rowStart = 0;
			  int colStart = 0;
			  int rowFinish = 0;
			  int colFinish = 0;
			  //Operation opQueue = queue.remove();
			  Location lstart = opQueue.lstart;
			  Location lfinish = opQueue.lfinish;
			  System.out.println("Repaint:"+opQueue);
			  Queue<Operation> paintQueue1 = new LinkedList<>(paintQueue);
			  //paintQueue1 = paintQueue;
			  Iterator<Operation> iterator = paintQueue1.iterator();
			  while(iterator.hasNext()) {
				  Operation opArr = new Operation();
				  opArr = paintQueue1.remove();
				  System.out.println("Repaint opArr:"+opArr);
				  System.out.println("Repaint paintQueue:"+paintQueue);
				  switch(opArr.optype) {
				  case 0: {
				  colStart = getCol(lstart);
				  rowStart = getRow(lstart);
					
				  if(opArr.player == 0) {
					  g2.setColor(Color.RED);
				  }else if(opArr.player == 1) {
					  g2.setColor(Color.BLUE);
				  }
				  g2.fillOval(rowStart*100-50-20, colStart*100-50-20, 40, 40);
				  
				  break;
				  
				  //replayPlace(opQueue,(Graphics2D) getGraphics()); 
				  }
				  case 1:{
				  rowStart = getRow(lstart);
				  rowFinish = getRow(lfinish);
				  colStart = getCol(lstart);
				  colFinish = getCol(lfinish);
				  g2.setColor(Color.white);
				  g2.fillOval(rowStart*100-50-20, colStart*100-50-20, 40, 40);
				  repaint();
				  if(opArr.player == 0) {
					g2.setColor(Color.RED);
				  }else if(opArr.player == 1) {
						g2.setColor(Color.BLUE);
				  }
				  g2.fillOval(rowFinish*100-50-20, colFinish*100-50-20, 40, 40);
				  
				  break;
				  
				  //replayMove(opQueue, (Graphics2D) getGraphics()); 
				  
				  }
				  case 2: {
				  colStart = getCol(lstart);
				  rowFinish = getRow(lstart);
				  g2.setColor(Color.WHITE);
				  g2.fillOval(rowStart*100-50-20, colFinish*100-50-20, 40, 40);
				  break;
				  
				  //replayRemove(opQueue, (Graphics2D) getGraphics()); break;
			  }
			  default: break;
			  }
			  }
			  
			  if(queue ==null) {
				  replayflag = false;
				  game.init();
				  computerplayer=-1;
			   		recordflag=false;
			   		game.init();
			        repaint();     
			  }
		
		  
	  }
	}
	 }
	 

    
  
	 public void init(){
		
		 myJPanel canvas =new myJPanel();
	  canvas.setEnabled(false);
	  
		 addMouseListener(this);
		 addMouseMotionListener(this);
		 JMenuBar bar = new JMenuBar();
	 	 JMenu menu = new JMenu("Mode:Human to Human(Default)");
	 	JMenuItem item1= new JMenuItem("Human to Human(Default)");
	 	item1.addActionListener(new ActionListener(){  
	   	  public void actionPerformed(ActionEvent e){  
	            computerplayer=-1;
	            game.init();
	            menu.setText("Mode:Human to Human");
	          repaint();
	           
	         }  
	     });
	 	JMenuItem item2= new JMenuItem("Automated Red Player");
		item2.addActionListener(new ActionListener(){  
		   	  public void actionPerformed(ActionEvent e){  
		   		computerplayer=Operation.PLAYER_R;
		   		   game.init();
		            menu.setText("Mode:Automated Red Player");
		            ComputerComponent.Operate(computerplayer,game);
		            repaint();
		         
		         }  
		     });
	 	JMenuItem item3= new JMenuItem("Automated Blue Player");
		item3.addActionListener(new ActionListener(){  
		   	  public void actionPerformed(ActionEvent e){  
		   		computerplayer=Operation.PLAYER_B;
		   		game.init();
		            menu.setText("Mode:Automated Blue Player");
		           repaint();
		         }  
		     });
	     menu.add(item1);
	     menu.add(item2);
	     menu.add(item3);
	     bar.setBounds(700, 150, 200, 50);
	     bar.add(menu);
	     

      JButton b_reset=new JButton("Reset"); 
      b_reset.setBounds(700, 250, 200, 50);
      
      JButton b_record=new JButton("Record"); 
      b_record.setBounds(700, 350, 200, 50);
      
      JButton b_replay=new JButton("Replay"); 
      b_replay.setBounds(700, 450, 200, 50);
      
 	 b_reset.addActionListener(new ActionListener(){  
   	  public void actionPerformed(ActionEvent e){
   		computerplayer=-1;
   		menu.setText("Mode:Human to Human");
   		b_record.setText("Record");
   		recordflag=false;
   		replayflag=false;
        game.init();
        repaint();            
       }  
     }); 
 	 
 	
    
     b_record.addActionListener(new ActionListener(){  
     	  public void actionPerformed(ActionEvent e){
     		  recordflag=true;
     		  queue.clear();
     		  b_record.setText("Recording in progress");            
           }  
       }); 
     
     b_replay.addActionListener(new ActionListener(){  
    	  public void actionPerformed(ActionEvent e){
    		  if(game.checkFinished()){
    			  replayflag=true;
    			  recordflag=false;
    			  b_record.setText("Record");
        		  b_replay.setText("Click for next");  
        		  //repaint();
        		  System.out.println("Q:"+queue);
        		  if(queue == null) {
    				  //replayflag=false;
    				  b_replay.setText("Done!");
    				  //repaint();
    				  //game.init();
    				  
    			  }
        		  opQueue = queue.remove();
        		  paintQueue.add(opQueue);
        			  System.out.println("Q:"+opQueue);
        			  System.out.println("Replay Flag:"+replayflag);
        			  System.out.println("Record Flag:"+recordflag);
        			  //repaint();
        			  recordflag=false;
        			  repaint();
        			  
        		  
//        		      		  
    		  }else {
    			  replayflag=false;
        		  b_replay.setText("Only finished game can be replayed");  
    		  }
    		            
          }  
      }); 
     
    
    add(bar);
    add(b_reset);
    add(b_record);
    add(b_replay);
    add(canvas);
      
       
	 setResizable(false);
	
	 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	 setBounds(0,0,1000,750);
	
	 setVisible(true);  
     
	 }


	 public static void main(String[] args) {
	 GUI d=new GUI();
     d.init();   
     d.game.init();
      
	 }


	@Override
	public void mouseClicked(MouseEvent e) {
		if(!replayflag) {
		int x=e.getX();
		int y=e.getY();

		Location lc=null;
		for(Location l:Location.values()) {
			if(x>l.getColumn()*100-50-40&&x<l.getColumn()*100-50+40&&y>l.getRow()*100-20-40&&y<l.getRow()*100-20+40)
				{lc=l;break;}
		}
		if(lc==null)return;
		Operation op=Game.msgToOp(game.msg);
		op.lstart=lc;
		System.out.println("Replay Flag:"+replayflag);
		System.out.println("Record Flag:"+recordflag);
		System.out.print("Clicked"+op+"\n");
		if(recordflag) {queue.add(op);};
		if(op.optype==Operation.MOVE)return;
		if(op.optype==Operation.PLACE)game.toDo(op);
		if(op.optype==Operation.EAT) {game.toEat(op);}
       if(op.optype!=Operation.EAT) game.checkEatable(op);
   	 game.checkFinished();
    ComputerComponent.Operate(computerplayer,game);
    Operation lstop=game.csbd.opList.get(game.csbd.opList.size()-1);
    if(lstop.player==computerplayer)if(game.checkEatable(lstop))
   {ComputerComponent.Operate(computerplayer,game);}
	 game.checkFinished();
   	repaint();
		}
		
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		if(Game.msgToOp(game.msg).optype!=Operation.MOVE)return;
		int x=e.getX();
		int y=e.getY();
		 int count=0;
		Location lc=null;
		for(Location l:Location.values()) {
			if(x>l.getColumn()*100-50-40&&x<l.getColumn()*100-50+40&&y>l.getRow()*100-20-40&&y<l.getRow()*100-20+40)
				{lc=l;count++;break;}
		}
		if(count==0) {flag=false;op0=new Operation();repaint();return;}
		for(Chess c: game.csbd.chessonboard) {
			if(c.l.getColumn()==lc.getColumn()&&c.l.getRow()==lc.getRow())
			{  
				origin=c;
				count++;
				if(Game.msgToOp(game.msg).player!=c.player){flag=false;op0=new Operation();repaint();return;}
			}
		}
		if(count==1){flag=false;op0=new Operation();repaint();return;}
		  op0.player=Game.msgToOp(game.msg).player;
		  op0.optype=Operation.MOVE;
		op0.lstart=lc;
		flag=true;
	
		
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		if(!flag)return;
		if(Game.msgToOp(game.msg).optype!=Operation.MOVE)return;
		int x=e.getX();
		int y=e.getY();
       int count=0;
		Location lc=null;
		for(Location l:Location.values()) {
			if(x>l.getColumn()*100-50-40&&x<l.getColumn()*100-50+40&&y>l.getRow()*100-20-40&&y<l.getRow()*100-20+40)
				{lc=l;count++;break;}
		}
		if(count==0) {flag=false;op0=new Operation();repaint();return;}
		op0.lfinish=lc;

		String outcome=game.toDo(op0);
          
		
		if(outcome=="ERROR!") {flag=false;op0=new Operation();repaint();return;};
		System.out.println("Replay Flag:"+replayflag);
		System.out.println("Record Flag:"+recordflag);
		System.out.print("Released"+op0+"\n");
		flag=false;
		game.checkFinished();
		game.checkEatable(op0);

        ComputerComponent.Operate(computerplayer,game);
        Operation lstop=game.csbd.opList.get(game.csbd.opList.size()-1);
       if(lstop.player==computerplayer)if(game.checkEatable(lstop)) {ComputerComponent.Operate(computerplayer,game);}
  	 game.checkFinished();
		repaint();
		
		
	}
	

	
	
	public int getRow(Location l) {
		//to get row
		String ls = l.toString();
		switch(ls) {
		case "a1": case"a4": case "a7" : return 1;
		case "b2": case"b4": case "b6" : return 2;
		case "c3": case"c4": case "c5" : return 3;
		case "d1": case"d2": case "d3" : case "d5": case"d6": case "d7" : return 4;
		case "e3": case"e4": case "e5" : return 5;
		case "f2": case"f4": case "f6" : return 6;
		case "g1": case"g4": case "g7" : return 7;
		default: return 0;
		}
		
	}
	
	
	public int getCol(Location l) {
		//to get column
		String ls = l.toString();
		switch(ls) {
		case "a1": case"d1": case "g1" : return 1;
		case "b2": case"d2": case "f2" : return 2;
		case "c3": case"d3": case "e3" : return 3;
		case "a4": case"b4": case "c4" : case "e4": case"f4": case "g4" : return 4;
		case "c5": case"d5": case "e5" : return 5;
		case "b6": case"d6": case "f6" : return 6;
		case "a7": case"d7": case "g7" : return 7;
		default: return 0;
		}
			
	}
	


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
	lx=e.getX();
	ly=e.getY();
	
	repaint();

	}


	@Override
	public void mouseMoved(MouseEvent e) {
		//lx=e.getX();
		//ly=e.getY();
	
		//repaint();
		
	}
	}

