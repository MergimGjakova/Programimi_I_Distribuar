import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ServerInterface extends Remote
{
	
	
	public int EnterGame(String name) throws RemoteException;
	
	public String getName(int id) throws RemoteException;
	
	public String getCardPriority()throws RemoteException;
	
	public Card[] givrMeCards(int id)throws RemoteException;
	
	public int giveMyRealPoints(int id)throws RemoteException;
	
	public boolean putCard(int id, int crdNo)throws RemoteException;
	
	public Card[] getCards(int id)throws RemoteException;
	
	public int whosChance()throws RemoteException;
	
	public  boolean roundOver()throws RemoteException;
	
	public Card[] getLastPut() throws RemoteException;
	
	public int howManyEntered() throws RemoteException;
	
	public boolean canstartNextEvent(int u_id)throws RemoteException;
	
	public String getPutType() throws RemoteException;
	
	public boolean isGameOver()throws RemoteException;
	
	public void needToLeave(int id)throws RemoteException;
	
	public int LeavedPlayerID()throws RemoteException;
	
	public boolean Leave() throws RemoteException;
	
	public String getWinnerOrder() throws RemoteException;
	
	
}
