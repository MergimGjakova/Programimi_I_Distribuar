public class GameUser 
{

   private int U_ID=0;
   public  String U_NAME=null;
   private Card[] U_Have = null;
   private int UserCardsPoints = 0;
   private int pointsGivenByothers=0;
   private Card lastPut;
   public int Event=0;
	
   public GameUser(int id, String name)
   {
      this.U_ID = id;
      this.U_NAME = name;
      this.lastPut = new Card("0", "N",0);
   }
	
   public int getID()
   {
      return U_ID;
   }
	
   public void setUserCards(Card[] card_arr)
   {
      this.U_Have = card_arr;
      setPlayingCardPoints(card_arr);
   }
	
	
   private void setPlayingCardPoints(Card[] card_arr)
   {
      int points = 0;
      for (int i=0;i<card_arr.length ;i++)
         points += card_arr[i].getPoints();
      this.UserCardsPoints = points;
   }
	
	
	
   public int getPointsByOthers()
   {
      return  pointsGivenByothers;
   }
	
	
	
   public void setPutCard2Zero(int val)
   {
      this.lastPut = new Card(U_Have[val].getValue(),U_Have[val].getType(),U_Have[val].getPoints());
      this.UserCardsPoints = this.UserCardsPoints - U_Have[val].getPoints();
      this.U_Have[val].setCardReset();
   			
   }
	
	
   public Card[] getCards()
   {
      return U_Have;
   }
	
   public Card getCard(int i)
   {
      return U_Have[i];
   }
	
   public void setUserPoints(int points)
   {
      this.pointsGivenByothers =this.pointsGivenByothers+ points;
   }
	
   public int getUserCardPoints()
   {
      return UserCardsPoints;
   }
	
   public Card getLastPut()
   {
      return lastPut;
   }
	
   public void setPutCardtoZero()
   {
      this.lastPut = new Card("0", "N",0);
   }
}
