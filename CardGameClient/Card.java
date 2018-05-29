import java.io.Serializable;


public class Card implements Serializable
{
   private String Type;
   private String Card_Val;
   private int points;
	//private int U_ID;
	
   public Card(String val,String type,int pnt)
   {
      this.Card_Val = val;
      this.Type = type;   	
      this.points = pnt;
   }
	
   public String getType()
   {
      return Type;
   }
	
   public String getValue()
   {
      return Card_Val;
   }
	
   public void setPoint(int pnt)
   {
      this.points = pnt;
   }
   public int getPoints()
   {
      return points;
   }
	
   public void setCardReset()
   {
      this.Card_Val ="0";
      this.Type="N"; //N means null
      this.points = 0;
   }
}
