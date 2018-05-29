import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class SimpleGUI extends JFrame {

   //private boolean drag = true;
   private  int NoofCards =8;
   private int EnteredCount = 0;
  // private  boolean HaveCards = false;
   private boolean UserNamePut;
	
   private ServerInterface rem;
   private int id;
   private String name;
   private int whosChance;
   private String whatTypetoPut;
   private boolean GameOver = false;
   private boolean exitThread;
	
   private ReadThread t;	
	
   private static final long serialVersionUID = 1L;

   private JPanel jContentPane = null;  
   private JPanel MyPanel = null;
	
	// these are the cards given to user to play
   private CardLabel Card1 = null;
   private CardLabel Card2 = null;
   private CardLabel Card3 = null;
   private CardLabel Card4 = null;
   private CardLabel Card5 = null;
   private CardLabel Card6 = null;
   private CardLabel Card7 = null;
   private CardLabel Card8 = null;
	

	// these are cards put by 4 users
   private CardLabel Other1 = null;
   private CardLabel Other2 = null;
   private CardLabel Other3 = null;
   private CardLabel Other4 = null;

   private JLabel lbl_status = null;

   private JLabel lbl_score = null;

   private JLabel lbl_chance = null;

   private JLabel lbl_LeavedUses = null;

	// these are the labes to put user names
   private JLabel User1 = null;
   private JLabel User2 = null;
   private JLabel User3 = null;
   private JLabel User4 = null;
	
	// hold card label put by users
   private CardLabel UsersPutCards[] = new CardLabel[4];
	
	// hold cards of the user
   private CardLabel CardArray[] = new CardLabel[NoofCards];
	
	// hold user name labels
   private JLabel UsersName[] = new JLabel[5];
	
	// position array to set the user put cards clock wise
   private int[][] OtherUserPutCardPosition = {{0,0},{199,293},{91,190},{199,92},{301,190}};
	
	// to set user name labels clock wise
   private int[][] OtherUserNamePosition = {{0,0},{64,422},{6,94},{350,29},{445,380}};
   private JLabel CardPic1 = null;
   private JLabel CardPic3 = null;
   private JLabel Cardpic2 = null;
   private JLabel CardPrio = null;
   private JLabel Pr1 = null;
   private JLabel pr2 = null;
   private JLabel pr3 = null;
   private JLabel pr4 = null;
   private JPanel BottomPanel = null;
   private JLabel Winner1 = null;
   private JLabel Winner2 = null;
   private JLabel Winner3 = null;
   private JLabel Winner4 = null;
   private JLabel WinnerLabel = null;
   private JLabel jLabel = null;
   private JLabel jLabel1 = null;
	/**
	 * This is the default constructor
	 */
   public SimpleGUI(String p_host, String p_name) {
      super();
      initialize();
      int porti=1099;
      String adresa="192.165.43.195"; 
      try
      {
         this.rem = (ServerInterface)Naming.lookup("rmi://"+adresa+":"+porti+"/RMD");
      	
         this.id = rem.EnterGame(p_name);
         System.out.println("Your id is : "+ id);
      	
         if(id == 0)
         {
            System.out.println("Max Connection reached");
         	
         }
         else
         {
            this.name = rem.getName(id);
            this.setTitle("Welcome " + name);
         	
            GiveCards();
         	
         }
      
      }
      catch (MalformedURLException e)
      {
         System.err.println("MalformedURLException: "+e.getMessage());
      }
      catch (RemoteException e)
      {
         System.err.println("RemoteException: " + e.getMessage());
      }
      catch (NotBoundException e)
      {
         System.err.println("NotBoundException: "+e.getMessage());
      }
   }

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
   private void initialize() {
      this.setSize(529, 654);
      this.setContentPane(getMyPanel());
      this.setResizable(false);
      this.setContentPane(getMyPanel());
      this.setTitle("Card Game");
      this.addWindowListener(
         new java.awt.event.WindowAdapter() {   
            public void windowClosing(java.awt.event.WindowEvent e) {    
            
               try 
               {
                  rem.needToLeave(id);
               } 
               catch (RemoteException e1) 
               {
                  e1.printStackTrace();
               }
               finally
               {
                  exitThread = true;
               }
            }
         });
   	
   	
   	
   }

   public void GiveCards()throws RemoteException
   {
      Card[] arr = null; 
   			
      if(id >0 & id<=4)
      {
         arr= rem.givrMeCards(id);
      		
      		/*
      		 * make cards labels to see 
      		 */
         for(int i=0; i<NoofCards;i++)
         {
            CardArray[i].setIcon(new ImageIcon("pic\\"+arr[i].getType()+" "+arr[i].getValue()+".png"));
            CardArray[i].cardId = i+1;
            CardArray[i].card = arr[i];
            CardArray[i].almostPut = false;
         }
      		
      		
      }
   		
      t = new ReadThread("my"+id,rem,id);
      t.start();
   						
   }
   
    
   private void putCard(CardLabel lbl)
   {
    	
      int putCard = lbl.cardId;
      if(putCard > 0 & putCard <= NoofCards)
      {
         try 
         {
            if(rem.putCard(id,putCard-1))
            {
               lbl.setBounds(lbl.getBounds());
               lbl.setIcon(null);
               lbl.almostPut = true;
            		//lbl.enable(false);
               lbl.card.setCardReset();
               lbl_status.setText("Card put");
            		 
            }
            else
            {
               lbl_status.setText("Almost put or Not Your chance");
            }
         		
         } 
         catch (RemoteException e) 
         {
         		
            e.printStackTrace();
         }
      		
      }
   }
    
   class ReadThread extends Thread 
   {
      ServerInterface rem;
      int u_id;
      private int sleepTime = 0;
      private int chance;
   	
   	
      public ReadThread( String name, ServerInterface p_rem, int p_id )
      {
         super( name );
      
         sleepTime = (int) ( Math.random() * 3000 );
         this.rem = p_rem;
         this.u_id = p_id;
      }
   	
      public void run()
      {
         while(true)
         {
            if(exitThread)
            {
               break;
            }
         	
         	
         	
            try 
            {
               EnteredCount = rem.howManyEntered();
               if(EnteredCount >= 4)
               {
               	// if someone leave the game
                  if(rem.Leave())
                  {
                     lbl_LeavedUses.setText(rem.getName(rem.LeavedPlayerID())+ " Leave the game!");
                     lbl_LeavedUses.setForeground(Color.BLUE);
                     lbl_status.setText("Game Over");
                     lbl_status.setForeground(Color.RED);
                     GameOver = true;
                     break;
                  }
               	
               	
                  if(!UserNamePut)
                  {
                     int theID = id;
                  	
                  	/*
                  	 * set put card positions
                  	 * Application put the current user position to middle lower
                  	 * and others clock wise
                  	 */
                     for(int i=1;i<=4;i++)
                     {
                        UsersName[theID].setLocation(OtherUserNamePosition[i][0], OtherUserNamePosition[i][1]);
                        UsersPutCards[theID-1].setLocation(OtherUserPutCardPosition[i][0], OtherUserPutCardPosition[i][1]);
                     	
                        theID++;
                        if(theID > 4)
                           theID = 1;
                     }
                  	
                  	
                     UsersName[1].setText(rem.getName(1));
                     UsersName[2].setText(rem.getName(2));
                     UsersName[3].setText(rem.getName(3));
                     UsersName[4].setText(rem.getName(4));
                     UsersName[id].setForeground(Color.RED);
                  	
                     lbl_status.setText("");
                     UserNamePut = true;
                     setCardStrength();
                  	
                  	
                  }
               	
               	// if current round over set put card icons to nothing
                  if(rem.roundOver())
                  {
                     for(int i=0; i<4;i++)
                     {
                        UsersPutCards[i].setIcon(new ImageIcon(""));
                     }
                  }
               	
               	// if all cards gone start next round
                  if(rem.canstartNextEvent(u_id))
                  {
                     if(rem.roundOver())
                     {
                     	
                        GiveCards();
                        setResizeCard();
                        break;
                     }
                  }
               	
               	
               		 
               		// return cards put by users
               	
                  Card[] arrUserCards = rem.getLastPut();
                  for(int i=0; i<4;i++)
                  {
                     if(arrUserCards[i].getType() != "N")
                     {
                        UsersPutCards[i].setIcon(new ImageIcon("pic\\"+arrUserCards[i].getType()+" "+ arrUserCards[i].getValue()+".png"));
                     }
                  }
               			
               							 
                  chance = rem.whosChance();
                  whosChance = chance;
                  whatTypetoPut = rem.getPutType();
               		
               		
                  if(u_id == chance)
                  {
                     lbl_chance.setText("You have the chance");
                     lbl_chance.setForeground(Color.ORANGE);
                  		 
                  }
                  else
                  {
                     lbl_chance.setText(rem.getName(chance)+"'s chance");
                     lbl_chance.setForeground(Color.BLACK);
                  		 
                  		 
                  }
                  lbl_score.setText("Score = "+ Integer.toString(rem.giveMyRealPoints(u_id)));
               		
                  if(rem.isGameOver())
                  {
                     GameOver = true;
                     lbl_status.setText("Game Over!");
                     lbl_status.setForeground(Color.RED);
                     lbl_chance.setText("");
                  		
                     String winnerOrder = rem.getWinnerOrder();
                  		
                     String[] winnerArray = winnerOrder.split(",");
                     WinnerLabel.setText("Winners!");
                     Winner4.setText("4 - "+winnerArray[0]);
                     Winner3.setText("3 - "+winnerArray[1]);
                     Winner2.setText("2 - "+winnerArray[2]);
                     Winner1.setText("1 - "+winnerArray[3]);
                  		
                  		//System.out.println("Game Over!");
                     break;
                  }
               }
               else
               {
               	//System.out.println("Not reached requried limit!..");
                  lbl_status.setText("Users = "+EnteredCount);
               	
               }
            		
            	  //put thread to sleep
               Thread.sleep( sleepTime );
            	 
            }
            
            				 
            catch ( InterruptedException interruptedException ) 
            {
               System.err.println("InterruptedException"+interruptedException.getMessage());
            }
            catch (RemoteException e) 
            {
            	
               System.err.println("RemoteException"+ e.getMessage());
            }
            catch (Exception e)
            {
               System.err.println("Exception"+e.getMessage());
            }
         }
      
       
      }
   }
    
   private void setCardStrength()
   {
      String CardPriority;
   	
      try
      {
         CardPriority = rem.getCardPriority();
         String[] splitAraay =  CardPriority.split(",");
      	
         Pr1.setText(splitAraay[3]+"   >");
         pr2.setText(splitAraay[2]+"   >");
         pr3.setText(splitAraay[1]+"   >");
         pr4.setText(splitAraay[0]);
      }
      
      catch (RemoteException e)
      {
         System.err.println("RemoteException: " + e.getMessage());
      }
   	
   	
   }
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
   private JPanel getJContentPane() {
      if (jContentPane == null) {
         jContentPane = new JPanel();
         jContentPane.setLayout(new BorderLayout());
      }
      return jContentPane;
   }

	/**
	 * This method initializes MyPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
   private JPanel getMyPanel() {
      if (MyPanel == null) {
      	
      	
         jLabel1 = new JLabel();
         jLabel1.setBounds(new Rectangle(201, 267, 132, 16));
         jLabel = new JLabel();
         jLabel.setBounds(new Rectangle(207, 247, 116, 17));
      	
         WinnerLabel = new JLabel();
         WinnerLabel.setBounds(new Rectangle(17, 470, 62, 24));
         WinnerLabel.setHorizontalTextPosition(SwingConstants.CENTER);
         WinnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
         WinnerLabel.setText("");
         Winner4 = new JLabel();
         Winner4.setText("");
         Winner4.setSize(new Dimension(90, 16));
         Winner4.setLocation(new Point(8, 560));
         Winner3 = new JLabel();
         Winner3.setText("");
         Winner3.setSize(new Dimension(90, 16));
         Winner3.setLocation(new Point(8, 540));
         Winner2 = new JLabel();
         Winner2.setText("");
         Winner2.setSize(new Dimension(90, 16));
         Winner2.setLocation(new Point(8, 520));
         Winner1 = new JLabel();
         Winner1.setText("");
         Winner1.setSize(new Dimension(90, 16));
         Winner1.setLocation(new Point(8, 500));
         pr4 = new JLabel();
         pr4.setText("");
         pr4.setSize(new Dimension(76, 21));
         pr4.setLocation(new Point(410, 5));
         pr4.setForeground(new Color(37, 38, 215));
         pr3 = new JLabel();
         pr3.setText("");
         pr3.setSize(new Dimension(76, 21));
         pr3.setLocation(new Point(310, 5));
         pr3.setForeground(new Color(37, 38, 215));
         pr2 = new JLabel();
         pr2.setText("");
         pr2.setSize(new Dimension(76, 21));
         pr2.setLocation(new Point(210, 5));
         pr2.setForeground(new Color(37, 38, 215));
         Pr1 = new JLabel();
         Pr1.setText("");
         Pr1.setSize(new Dimension(76, 21));
         Pr1.setLocation(new Point(110, 5));
         Pr1.setForeground(new Color(37, 38, 215));
         CardPrio = new JLabel();
         CardPrio.setForeground(new Color(37, 38, 215));
         CardPrio.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
         CardPrio.setSize(new Dimension(87, 22));
         CardPrio.setLocation(new Point(7, 5));
         CardPrio.setText("Card Strength..");
         Cardpic2 = new JLabel();
         Cardpic2.setBounds(new Rectangle(131, 8, 216, 68));
         Cardpic2.setIcon(new ImageIcon(getClass().getResource("/pic/Cards2.JPG")));
         Cardpic2.setText("");
         CardPic3 = new JLabel();
         CardPic3.setBounds(new Rectangle(449, 93, 64, 269));
         CardPic3.setText("");
         CardPic3.setIcon(new ImageIcon(getClass().getResource("/pic/Cards.JPG")));
         CardPic1 = new JLabel();
         CardPic1.setBounds(new Rectangle(13, 120, 64, 269));
         CardPic1.setIcon(new ImageIcon(getClass().getResource("/pic/Cards.JPG")));
         CardPic1.setText("");
         User4 = new JLabel();
         User4.setBounds(new Rectangle(64, 422, 83, 17));
         User4.setHorizontalTextPosition(SwingConstants.CENTER);
         User4.setText("");
         User4.setHorizontalAlignment(SwingConstants.CENTER);
         User3 = new JLabel();
         User3.setBounds(new Rectangle(445, 380, 70, 16));
         User3.setHorizontalTextPosition(SwingConstants.CENTER);
         User3.setText("");
         User3.setHorizontalAlignment(SwingConstants.CENTER);
         User2 = new JLabel();
         User2.setBounds(new Rectangle(350, 29, 78, 16));
         User2.setHorizontalTextPosition(SwingConstants.CENTER);
         User2.setText("");
         User2.setToolTipText("");
         User2.setHorizontalAlignment(SwingConstants.CENTER);
         User1 = new JLabel();
         User1.setBounds(new Rectangle(6, 94, 81, 16));
         User1.setHorizontalTextPosition(SwingConstants.CENTER);
         User1.setHorizontalAlignment(SwingConstants.CENTER);
         User1.setText("");
         lbl_LeavedUses = new JLabel();
         lbl_LeavedUses.setText("");
         lbl_LeavedUses.setSize(new Dimension(135, 17));
         lbl_LeavedUses.setLocation(new Point(380, 550));
         lbl_chance = new JLabel();
         lbl_chance.setText("");
         lbl_chance.setSize(new Dimension(135, 17));
         lbl_chance.setLocation(new Point(380, 520));
         lbl_score = new JLabel();
         lbl_score.setText("");
         lbl_score.setSize(new Dimension(135, 17));
         lbl_score.setLocation(new Point(380, 490));
         lbl_status = new JLabel();
         lbl_status.setText("");
         lbl_status.setSize(new Dimension(135, 17));
         lbl_status.setLocation(new Point(380, 460));
         Other4 = new CardLabel();
         Other4.setText("");
         Other4.setSize(new Dimension(90, 135));
         Other4.setLocation(new Point(215, 305));
         Other3 = new CardLabel();
         Other3.setText("");
         Other3.setSize(new Dimension(90, 135));
         Other3.setLocation(new Point(340, 195));
         Other2 = new CardLabel();
         Other2.setText("");
         Other1 = new CardLabel();
         Other2.setSize(new Dimension(90, 135));
         Other2.setLocation(new Point(218, 84));
         Other1.setText("");
         Other1.setSize(new Dimension(90, 135));
         Other1.setLocation(new Point(93, 195));
      	
      	
         Card1 = new CardLabel();
         Card1.setText("");
         Card1.setSize(new Dimension(90, 135));
         Card1.setToolTipText("");
         Card1.setLocation(new Point(105, 450));
      	
         Card1.addMouseListener(
            new java.awt.event.MouseAdapter() 
            {   
               public void mouseExited(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card1, 7);
                  Card1.setLocation(new Point(105, 450));
                  MyPanel.updateUI();
               }   
               public void mouseEntered(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card1, 0);
                  Card1.setLocation(new Point(105, 440));
                  MyPanel.updateUI();
               }   
            
               public void mousePressed(java.awt.event.MouseEvent e) 
               {
                  if(GameOver | Card1.almostPut | EnteredCount != 4)
                     return;
                  if(!putTypeOk(Card1.card))
                  {
                     lbl_status.setText(whatTypetoPut + "  !=  "+Card1.card.getType());
                     return;
                  }
               
                  if(whosChance != id)
                  {
                     lbl_status.setText("Not Yours");
                     return;
                  }
                  putCard(Card1);
                  Card1.setSize(new Dimension(2, 135));
               }
            });
      	
      	
         Card2 = new CardLabel();
         Card2.setText("");
         Card2.setSize(new Dimension(90, 135));
         Card2.setToolTipText("");
         Card2.setLocation(new Point(130, 450));
      	
         Card2.addMouseListener(
            new java.awt.event.MouseAdapter() 
            {   
               public void mouseExited(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card2, 6);
                  Card2.setLocation(new Point(130, 450));
                  MyPanel.updateUI();
               }   
               public void mouseEntered(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card2, 0);
                  Card2.setLocation(new Point(130, 440));
                  MyPanel.updateUI();
               }   
            
               public void mousePressed(java.awt.event.MouseEvent e) 
               {
                  if(GameOver | Card2.almostPut | EnteredCount != 4)
                     return;
                  if(!putTypeOk(Card2.card))
                  {
                     lbl_status.setText(whatTypetoPut + "  !=  "+Card2.card.getType());
                     return;
                  }
               
                  if(whosChance != id)
                  {
                     lbl_status.setText("Not Yours");
                     return;
                  }
                  putCard(Card2);
                  Card2.setSize(new Dimension(2, 135));
               }
            });
      	
      	
         Card3 = new CardLabel();
         Card3.setText("");
         Card3.setSize(new Dimension(90, 135));
         Card3.setToolTipText("");
         Card3.setLocation(new Point(155, 450));
      	
         Card3.addMouseListener(
            new java.awt.event.MouseAdapter() 
            {   
               public void mouseExited(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card3, 5);
                  Card3.setLocation(new Point(155, 450));
                  MyPanel.updateUI();
               }   
               public void mouseEntered(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card3, 0);
                  Card3.setLocation(new Point(155, 440));
                  MyPanel.updateUI();
               }   
            
               public void mousePressed(java.awt.event.MouseEvent e) 
               {
                  if(GameOver | Card3.almostPut | EnteredCount != 4)
                     return;
                  if(!putTypeOk(Card3.card))
                  {
                     lbl_status.setText(whatTypetoPut + "  !=  "+Card3.card.getType());
                     return;
                  }
               
                  if(whosChance != id)
                  {
                     lbl_status.setText("Not Yours");
                     return;
                  }
                  putCard(Card3);
                  Card3.setSize(new Dimension(2, 135));
               }
            });
      	
      	
         Card4 = new CardLabel();
         Card4.setText("");
         Card4.setSize(new Dimension(90, 135));
         Card4.setToolTipText("");
         Card4.setLocation(new Point(180, 450));
      	
         Card4.addMouseListener(
            new java.awt.event.MouseAdapter() 
            {   
               public void mouseExited(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card4, 4);
                  Card4.setLocation(new Point(180, 450));
                  MyPanel.updateUI();
               }   
               public void mouseEntered(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card4, 0);
                  Card4.setLocation(new Point(180, 440));
                  MyPanel.updateUI();
               }   
            
               public void mousePressed(java.awt.event.MouseEvent e) 
               {
                  if(GameOver | Card4.almostPut | EnteredCount != 4)
                     return;
                  if(!putTypeOk(Card4.card))
                  {
                     lbl_status.setText(whatTypetoPut + "  !=  "+Card4.card.getType());
                     return;
                  }
               
                  if(whosChance != id)
                  {
                     lbl_status.setText("Not Yours");
                     return;
                  }
                  putCard(Card4);
                  Card4.setSize(new Dimension(2, 135));
               }
            });
      	
         Card5 = new CardLabel();
         Card5.setText("");
         Card5.setSize(new Dimension(90, 135));
         Card5.setToolTipText("");
         Card5.setLocation(new Point(205, 450));
      	
         Card5.addMouseListener(
            new java.awt.event.MouseAdapter() 
            {   
               public void mouseExited(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card5, 3);
                  Card5.setLocation(new Point(205, 450));
                  MyPanel.updateUI();
               }   
               public void mouseEntered(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card5, 0);
                  Card5.setLocation(new Point(205, 440));
                  MyPanel.updateUI();
               }   
            
               public void mousePressed(java.awt.event.MouseEvent e) 
               {
                  if(GameOver | Card5.almostPut | EnteredCount != 4)
                     return;
                  if(!putTypeOk(Card5.card))
                  {
                     lbl_status.setText(whatTypetoPut + "  !=  "+Card5.card.getType());
                     return;
                  }
               
                  if(whosChance != id)
                  {
                     lbl_status.setText("Not Yours");
                     return;
                  }
                  putCard(Card5);
                  Card5.setSize(new Dimension(2, 135));
               }
            });
      	
      	
         Card6 = new CardLabel();
         Card6.setText("");
         Card6.setSize(new Dimension(90, 135));
         Card6.setToolTipText("");
         Card6.setLocation(new Point(230, 450));
      	
         Card6.addMouseListener(
            new java.awt.event.MouseAdapter() 
            {   
               public void mouseExited(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card6, 2);
                  Card6.setLocation(new Point(230, 450));
                  MyPanel.updateUI();
               }   
               public void mouseEntered(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card6, 0);
                  Card6.setLocation(new Point(230, 440));
                  MyPanel.updateUI();
               }   
            
               public void mousePressed(java.awt.event.MouseEvent e) 
               {
                  if(GameOver | Card6.almostPut | EnteredCount != 4)
                     return;
                  if(!putTypeOk(Card6.card))
                  {
                     lbl_status.setText(whatTypetoPut + "  !=  "+Card6.card.getType());
                     return;
                  }
               
                  if(whosChance != id)
                  {
                     lbl_status.setText("Not Yours");
                     return;
                  }
                  putCard(Card6);
                  Card6.setSize(new Dimension(2, 135));
               }
            });
      	
      	
         Card7 = new CardLabel();
         Card7.setText("");
         Card7.setSize(new Dimension(90, 135));
         Card7.setToolTipText("");
         Card7.setLocation(new Point(255, 450));
      	
         Card7.addMouseListener(
            new java.awt.event.MouseAdapter() 
            {   
               public void mouseExited(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card7, 1);
                  Card7.setLocation(new Point(255, 450));
                  MyPanel.updateUI();
               }   
               public void mouseEntered(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card7, 0);
                  Card7.setLocation(new Point(255, 440));
                  MyPanel.updateUI();
               }   
            
               public void mousePressed(java.awt.event.MouseEvent e) 
               {
                  if(GameOver | Card7.almostPut | EnteredCount != 4)
                     return;
                  if(!putTypeOk(Card7.card))
                  {
                     lbl_status.setText(whatTypetoPut + "  !=  "+Card7.card.getType());
                     return;
                  }
               
                  if(whosChance != id)
                  {
                     lbl_status.setText("Not Yours");
                     return;
                  }
                  putCard(Card7);
                  Card7.setSize(new Dimension(2, 135));
               }
            });
      	
      	
      	
      	
         Card8 = new CardLabel();
         Card8.setText("");
         Card8.setSize(new Dimension(90, 135));
         Card8.setToolTipText("");
         Card8.setLocation(new Point(280, 450));
      	
         Card8.addMouseListener(
            new java.awt.event.MouseAdapter() 
            {   
               public void mouseExited(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card8, 0);
                  Card8.setLocation(new Point(280, 450));
                  MyPanel.updateUI();
               }   
               public void mouseEntered(java.awt.event.MouseEvent e) {    
                  MyPanel.setComponentZOrder(Card8, 0);
                  Card8.setLocation(new Point(280, 440));
                  MyPanel.updateUI();
               }   
            
               public void mousePressed(java.awt.event.MouseEvent e) 
               {
                  if(GameOver | Card8.almostPut | EnteredCount != 4)
                     return;
                  if(!putTypeOk(Card8.card))
                  {
                     lbl_status.setText(whatTypetoPut + "  !=  "+Card8.card.getType());
                     return;
                  }
               
                  if(whosChance != id)
                  {
                     lbl_status.setText("Not Yours");
                     return;
                  }
                  putCard(Card8);
                  Card8.setSize(new Dimension(2, 135));
               }
            });
      	
      	
      	
      	
      	/*
      	 *  set arrays
      	 */
      	
         CardArray[0] = Card1;
         CardArray[1] = Card2;
         CardArray[2] = Card3;
         CardArray[3] = Card4;
         CardArray[4] = Card5;
         CardArray[5] = Card6;
         CardArray[6] = Card7;
         CardArray[7] = Card8;
           
           
           
           
           
           
         UsersPutCards[0] = Other1;
         UsersPutCards[1] = Other2;
         UsersPutCards[2] = Other3;
         UsersPutCards[3] = Other4;
           
           
         UsersName[1] = User1;
         UsersName[2] = User2;
         UsersName[3] = User3;
         UsersName[4] = User4;
           
         MyPanel = new JPanel();
         MyPanel.setLayout(null);
         MyPanel.setBackground(new Color(0, 153, 0));
      	
         MyPanel.add(Other1, null);
         MyPanel.add(Other2, null);
         MyPanel.add(Other3, null);
         MyPanel.add(Other4, null);
         MyPanel.add(lbl_status, null);
         MyPanel.add(lbl_score, null);
         MyPanel.add(lbl_chance, null);
         MyPanel.add(lbl_LeavedUses, null);
         MyPanel.add(User1, null);
         MyPanel.add(User2, null);
         MyPanel.add(User3, null);
         MyPanel.add(User4, null);
         MyPanel.add(CardPic1, null);
         MyPanel.add(CardPic3, null);
         MyPanel.add(Cardpic2, null);
      	
      	
         MyPanel.add(Card8, 0);
         MyPanel.add(Card7, 1);
         MyPanel.add(Card6, 2);
         MyPanel.add(Card5, 3);
         MyPanel.add(Card4, 4);
         MyPanel.add(Card3, 5);
         MyPanel.add(Card2, 6);
         MyPanel.add(Card1, 7);
         MyPanel.add(getBottomPanel(), null);
         MyPanel.add(Winner1, null);
         MyPanel.add(Winner2, null);
         MyPanel.add(Winner3, null);
         MyPanel.add(Winner4, null);
         MyPanel.add(WinnerLabel, null);
         MyPanel.add(jLabel, null);
         MyPanel.add(jLabel1, null);
      	
      	
         MyPanel.updateUI();
      	
      }
      return MyPanel;
   }
	
   private void setResizeCard()
   {
      for(int i=0;i<NoofCards;i++)
      {
         CardArray[i].setSize(new Dimension(90, 135));
      }
   	
   }

   private boolean putTypeOk(Card card)
   {
       	
       	
      if((whatTypetoPut.equals(card.getType()) | whatTypetoPut.equals("") | notHaveCard()) & !GameOver)
         return true;
      else
         return false;
   }
	    
   private boolean notHaveCard()
   {
      boolean cardNotHave = true;
      for(int i=0;i < CardArray.length;i++)
      {
         if(whatTypetoPut.equals(CardArray[i].card.getType()))
            cardNotHave = false;
       		
      }
      return cardNotHave;
   }

		/**
		 * This method initializes BottomPanel	
		 * 	
		 * @return javax.swing.JPanel	
		 */
   private JPanel getBottomPanel() {
      if (BottomPanel == null) {
         BottomPanel = new JPanel();
         BottomPanel.setLayout(null);
         BottomPanel.setBounds(new Rectangle(-1, 592, 525, 30));
         BottomPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
         BottomPanel.add(CardPrio, null);
         BottomPanel.add(Pr1, null);
         BottomPanel.add(pr2, null);
         BottomPanel.add(pr3, null);
         BottomPanel.add(pr4, null);
      }
      return BottomPanel;
   }
	    

} 
