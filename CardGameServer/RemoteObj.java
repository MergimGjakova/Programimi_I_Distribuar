import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteObj extends UnicastRemoteObject implements ServerInterface
{
	
   private int count = 0;	//-- to count no of enties
   private boolean noOneEntered = true;
   private int cardsHavings = 0;
   private GameUser user0, user1, user2, user3, user4;
   protected GameUser Arr_User[] = { user0, user1 ,user2 ,user3 ,user4};

   private String PriorityString;

   private final static int cardGiveperUser=8;
   private final static int PointLimitToGameOver =1000;
	
   private Card D_card6 = new Card("7","D",1);
   private Card D_card7 = new Card("8","D",2);
   private Card D_card8 = new Card("9","D",3);
   private Card D_card9 = new Card("10","D",4);
   private Card D_card10 = new Card("J","D",5);
   private Card D_card11 = new Card("Q","D",6);
   private Card D_card12 = new Card("K","D",7);
   private Card D_card13 = new Card("A","D",8);
	
   private Card H_card6 = new Card("7","H",1);
   private Card H_card7 = new Card("8","H",2);
   private Card H_card8 = new Card("9","H",3);
   private Card H_card9 = new Card("10","H",4);
   private Card H_card10 = new Card("J","H",5);
   private Card H_card11 = new Card("Q","H",6);
   private Card H_card12 = new Card("K","H",7);
   private Card H_card13 = new Card("A","H",8);
	
   private Card C_card6 = new Card("7","C",1);
   private Card C_card7 = new Card("8","C",2);
   private Card C_card8 = new Card("9","C",3);
   private Card C_card9 = new Card("10","C",4);
   private Card C_card10 = new Card("J","C",5);
   private Card C_card11 = new Card("Q","C",6);
   private Card C_card12 = new Card("K","C",7);
   private Card C_card13 = new Card("A","C",8);
	
   private Card S_card6 = new Card("7","S",1);
   private Card S_card7 = new Card("8","S",2);
   private Card S_card8 = new Card("9","S",3);
   private Card S_card9 = new Card("10","S",4);
   private Card S_card10 = new Card("J","S",5);
   private Card S_card11 = new Card("Q","S",6);
   private Card S_card12 = new Card("K","S",7);
   private Card S_card13 = new Card("A","S",8);
	

   private Card[] cardPack = 
   {
   		D_card6,D_card7,D_card8,D_card9,D_card10,D_card11,D_card12,D_card13,
   		H_card6,H_card7,H_card8,H_card9,H_card10,H_card11,H_card12,H_card13,
   		C_card6,C_card7,C_card8,C_card9,C_card10,C_card11,C_card12,C_card13,
   		S_card6,S_card7,S_card8,S_card9,S_card10,S_card11,S_card12,S_card13
   		};
	
	
   private  int CountPutCards = 0;
   private  int maxXCardVal = 0;
   private  int maxCardOwnerID = 0;
   private  int totlaPoints = 0;
	
   private int currentPayable = 1;
   private String typeToput = "";
	
   private boolean roundisOver;
   private boolean thurumPuGiven = false;
	
   private boolean somOneLeave = false;
   private int leaveID = 0;
	
   protected RemoteObj() throws RemoteException
   {
      super();
   }

   class CardPoint
   {
      int point;
      String type;
      String realName;
   	
      public CardPoint(int p, String t,String rname) 
      {
         this.point = p;
         this.type = t;
         this.realName = rname;
      }
   }
	/*
	 * -------------------------------Allocate random points to cards----------------------
	 * ----------------- then set strength of cards based on points  ------------
	 * eg Diamond > crubs > heart > spades
	 */
   private void setOriginalCardPoints()
   {
      int x;
      while(true)
      {
         x = (int) (Math.random()*100);
         x = (int) (Math.random()*100);
         x = (int) (Math.random()*100);
         if(x <50 & x >0)
            break;
      }
   	
      CardPoint P1,P2,P3,P4;
   	
      int DP=0, HP=0, CP=0, SP = 0;
      for(int a = 0; a < 4; a++)
      {
         DP = x;
         x = x+10;
         if(x>40)
            x=10;
         HP = x;
         x = x+10;
         if(x>40)
            x=10;
         CP = x;
         x = x+10;
         if(x>40)
            x=10;
         SP = x;
         x=x+10;
      }
   	
      P1 = new CardPoint(DP,"D", "Diamond");
      P2 = new CardPoint(HP,"H", "Heart");
      P3 = new CardPoint(CP,"C" , "Crubs");
      P4 = new CardPoint(SP,"S" , "Spades");
   	
      System.out.println(DP + " " + HP + " " + " " + CP + " " + SP);
      for(int b = 0; b < cardGiveperUser*4;b++)
      {
         if(cardPack[b].getType() == "D")
            cardPack[b].setPoint(cardPack[b].getPoints()+DP);
         else if(cardPack[b].getType() == "H")
            cardPack[b].setPoint(cardPack[b].getPoints()+HP);
         else if(cardPack[b].getType() == "C")
            cardPack[b].setPoint(cardPack[b].getPoints()+CP);
         else if(cardPack[b].getType() == "S")
            cardPack[b].setPoint(cardPack[b].getPoints()+SP);
      }
   	 
   	
   	// to get the card priority order 
      CardPoint[] priorityArray = {P1,P2,P3,P4};
      for(int a=0;a<4;a++)
      {
         for(int b=3;b>a;b--)
            if(priorityArray[b].point<priorityArray[b-1].point)
            {
               CardPoint temp = priorityArray[b];
               priorityArray[b] = priorityArray[b-1];
               priorityArray[b-1] = temp;
            }
      }
   	
      PriorityString = priorityArray[0].realName+","+priorityArray[1].realName+","+priorityArray[2].realName+","+priorityArray[3].realName;
      System.out.println("String = "+PriorityString);
   }
	
	
	/*
	 *
	 * return card priority to clients as a string
	 */
	
   public String getCardPriority()
   {
      return PriorityString;
   	
   }
	
	//--------------------------------------------------------- Mix cards-----
   public void mixCards()
   {
      int i1 = (int) (Math.random()*10);
      int i2 = (int) (Math.random()*10);
      int i3 = (int) (Math.random()*10);
      int i4 = (int) (Math.random()*10);
   	
   			
   	
      System.out.println(i1+" "+i2);
   	
      for(; i1<cardGiveperUser*4-1; i1++)
      {
      	
         Card prev = cardPack[i1];
         cardPack[i1]= cardPack[i1+1];
         cardPack[i1+1] = prev;
      }
   	
      for(; i2<cardGiveperUser*4-1; i2++)
      {
         if(i2+3 > cardGiveperUser*4-1)
            break;
         Card prev = cardPack[i2];
      	
         cardPack[i2]= cardPack[i2+3];
         cardPack[i2+3] = prev;
      }
   	
      for(; i3<cardGiveperUser*4-1; i3++)
      {
         if(i3+4 > cardGiveperUser*4-1)
            break;
         Card prev = cardPack[i3];
      	
         cardPack[i3]= cardPack[i3+4];
         cardPack[i3+4] = prev;
      }
   	
      for(; i4<cardGiveperUser*4-1; i4++)
      {
         if(i4+5 > cardGiveperUser*4-1)
            break;
         Card prev = cardPack[i4];
      	
         cardPack[i4]= cardPack[i4+5];
         cardPack[i4+5] = prev;
      }
   }
	
	
	//--------------------------------------------------------- Initialize player enter
	
   public int EnterGame(String name) throws RemoteException 
   {
   	// no one enter to the game - > initial mix cards
      if(noOneEntered)
      {
      	
         setOriginalCardPoints();
         noOneEntered = false;
      	
         mixCards();
         mixCards();
      	
      	
      }
   	
      count++;
      if(count > 4)
      {
         return 0;
      }
      
      
      else if(count == 1)
      {
         Arr_User[count]= new GameUser(count,name);
         printEnterDetl(name,count);
         return Arr_User[count].getID();
      	
      	
      }
      else if(count == 2)
      {
         Arr_User[count]= new GameUser(count,name);
         printEnterDetl(name,count);
         return Arr_User[count].getID();
      }
      else if(count == 3)
      {
         Arr_User[count]= new GameUser(count,name);
         printEnterDetl(name,count);
         return Arr_User[count].getID();
      }
      else if(count == 4)
      {
         Arr_User[count]= new GameUser(count,name);
         printEnterDetl(name,count);
         return Arr_User[count].getID();
      }
      else
         return 0;
   	
   }

	
	//--------------------------------------------------------- Prints who entered in server
	
   public void printEnterDetl(String name,int id)
   {
      System.out.println("Player "+ id + " with name : "+ name + " Entered!");
   }
	
	
	
	//---------------------------------------------------------- Get user name for clients to get each one names
	
   public String getName(int id) throws RemoteException 
   {
      if(id >4 | id < 1)
         return "Illegal user";
      else
         return Arr_User[id].U_NAME;
   }
	
	
	
	//---------------------------------------------------------Distribute card
   public Card[] givrMeCards(int id)
   {
   	
      cardsHavings++;
   	
   	
   	
   	
      Card[] arr = new Card[cardGiveperUser] ;
   	
   	// set return array
      for(int i=0;i<cardGiveperUser;i++)
      {
         arr[i] = new Card(cardPack[(id-1)*cardGiveperUser+i].getValue(),cardPack[(id-1)*cardGiveperUser+i].getType(),cardPack[(id-1)*cardGiveperUser+i].getPoints());
      }
   	
   	
      Arr_User[id].setUserCards(arr);	// set cards and points particular user have
      Arr_User[id].Event++;
   	
   	/*
   	 *  if all  got cards in some round
   	 *  then mix cards for the following round
   	 */
      if(cardsHavings == 4)
      {
         cardsHavings = 0;
         roundisOver = false;
         mixCards();
         mixCards();
         mixCards();
         mixCards();
      	
      }
   	
      return arr;
   	
   }
	
	
	
	
	// ---------------------------------------------------------  Last put cards
   public Card[] getLastPut()
   {
      Card LastPutArray[] =new Card[4];
      LastPutArray[0] = Arr_User[1].getLastPut();//System.out.println(Arr_User[1].getLastPut().getValue());
      LastPutArray[1] = Arr_User[2].getLastPut();//System.out.println(Arr_User[2].getLastPut().getValue());
      LastPutArray[2] = Arr_User[3].getLastPut();//System.out.println(Arr_User[3].getLastPut().getValue());
      LastPutArray[3] = Arr_User[4].getLastPut();//System.out.println(Arr_User[4].getLastPut().getValue());
      return  LastPutArray;
   }
	//--------------------------------------------------------- 
   public int giveMyRealPoints(int id)
   {
   	
      return Arr_User[id].getPointsByOthers();
   }
	
   public String getPutType()
   {
      System.out.println("Put type = >> " + typeToput + " <<");
      return typeToput;
   }
	
	//--------------------------------------------------------- put cards
   public boolean putCard(int id, int crdNo)
   {
   	
   	//System.out.println("Current : =" + currentPayable);
      if(currentPayable != id | Arr_User[id].getCard(crdNo).getPoints() == 0)
         return false;
   	
      CountPutCards++;
   	
   	//---- set put type affect for further puts
      if(CountPutCards == 1)
         typeToput = Arr_User[id].getCard(crdNo).getType();
   	
      int recentGivenVal = Arr_User[id].getCard(crdNo).getPoints();
   			
      totlaPoints =totlaPoints + recentGivenVal;
   	
   	
      Arr_User[id].setPutCard2Zero(crdNo);
      if(maxXCardVal < recentGivenVal)
      {
         maxXCardVal = recentGivenVal;
         maxCardOwnerID = id;
      }
      nextPlayable();
   	
   	
   	
   		
   	
      if(CountPutCards > 3)
      {
         System.out.println("   ----------Before Distribution------------");
         System.out.println("   --Max Owner : "+ maxCardOwnerID);
         System.out.println("   --Total Points : "+ totlaPoints);
         System.out.println("   -----------------------------------------");
         AddCardPoints(totlaPoints,id);
         printSummaray();
      	
      	//if(Arr_User[1].getUserCardPoints() == 0 & Arr_User[2].getUserCardPoints() == 0 & Arr_User[3].getUserCardPoints() == 0 & Arr_User[4].getUserCardPoints() == 0)
         roundisOver = true;
      	// set last put cards to zero
         Arr_User[1].setPutCardtoZero();
         Arr_User[2].setPutCardtoZero();
         Arr_User[3].setPutCardtoZero();
         Arr_User[4].setPutCardtoZero();
      		
      	/*
      	int totPoints = 0;
      	for(int i = 0; i<Arr_User.length;i++)
      	{
      		totPoints += totPoints + Arr_User[i].getUserTotalPoints();
      	}
      	if(totPoints == 90)
      		roundisOver = true;
      	*/
         return true;
      }
      else
      {
         printSummaray();
         return true;
      }
   }
	
	
   private void printSummaray()
   {
      System.out.println("================================================");
      System.out.println("================================================");
      System.out.println("User 1 has points before end distribution:= "+Arr_User[1].getPointsByOthers());
      System.out.println("User 2 has points before end distribution:= "+Arr_User[2].getPointsByOthers());
      System.out.println("User 3 has points before end distribution:= "+Arr_User[3].getPointsByOthers());
      System.out.println("User 4 has points before end distribution:= "+Arr_User[4].getPointsByOthers());
      System.out.println("----Next : =" + currentPayable);
      System.out.println("================================================");
      System.out.println("================================================");
   }
	
	//-------------------------------------------------------------- add gone cards points
   public void AddCardPoints(int totPoints,int id)
   {
      Arr_User[maxCardOwnerID].setUserPoints(totlaPoints);
      currentPayable = maxCardOwnerID;
      CountPutCards =0;
      maxXCardVal=0;
      maxCardOwnerID=0;
      totlaPoints=0;
      typeToput ="";
   }
	
	
	//---------------------------------------------------------- set next playable
   private void nextPlayable()
   {
      currentPayable++;
      if(currentPayable > 4)
         currentPayable = 1;
   }
	////--------------------------------------------------------- get cards
   public Card[] getCards(int id)
   {
      return Arr_User[id].getCards();
   }
	
   public int whosChance()
   {
      return currentPayable;
   }
	
	
	
   public boolean roundOver()
   {
      return roundisOver;
   }
	
   public int howManyEntered()
   {
      return count;
   }
	
	/**
	 * this is to verify whether next event
	 * can start or not 
	 * this will happen when user playing cards are finished
	 */
	
   private boolean noAccess = false;
	
   public synchronized boolean canstartNextEvent(int p_id)
   {
      while ( noAccess ) 
      {
         try {
            wait();
         }
         catch ( InterruptedException exception ) {
         	
         }
      }
   	
      noAccess = true;
      int p = Arr_User[p_id].getUserCardPoints();
   	//System.out.println("Checking...points  = " + p);
      if(p == 0)
      {
         noAccess = false;
         return true;
      }
      	
      else
      {
         noAccess = false;
         return false;
      }
   }
	
   public boolean isGameOver()
   {
      boolean isOver = false;
      for(int i=1;i<Arr_User.length;i++)
         if(Arr_User[i].getPointsByOthers() >= PointLimitToGameOver)
         {
            isOver = true;
         }
      return isOver;
   			
   }
	
   public void needToLeave(int id)
   {
      leaveID = id;
      somOneLeave = true;
   }
	
   public int LeavedPlayerID()
   {
      return leaveID;
   }
   public boolean Leave()
   {
      return somOneLeave;
   }
	
	
   class winner
   {
      String name;
      int points;
      winner(String n,int p)
      {
         this.name = n;
         this.points = p;
      }
   }
	
   public String getWinnerOrder()
   {
      winner win1 = new winner(Arr_User[1].U_NAME, Arr_User[1].getPointsByOthers());
      winner win2 = new winner(Arr_User[2].U_NAME, Arr_User[2].getPointsByOthers());
      winner win3 = new winner(Arr_User[3].U_NAME, Arr_User[3].getPointsByOthers());
      winner win4 = new winner(Arr_User[4].U_NAME, Arr_User[4].getPointsByOthers());
   	
   	
      winner[] winnerArray = {win1,win2,win3,win4};
      for(int a=0;a<4;a++)
      {
         for(int b=3;b>a;b--)
            if(winnerArray[b].points < winnerArray[b-1].points)
            {
               winner temp = winnerArray[b];
               winnerArray[b] = winnerArray[b-1];
               winnerArray[b-1] = temp;
            }
      }
   	
      String pointString = winnerArray[0].name+","+
         					winnerArray[1].name + ","+
         						winnerArray[2].name + ","+
         							winnerArray[3].name;
   	
      return pointString;
   	
   }
}
