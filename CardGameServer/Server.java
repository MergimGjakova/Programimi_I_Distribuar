import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;


public class Server
{

   public static void main(String[] args)
   {
      
      int porti =1099;
      try
      {
         RemoteObj obj = new RemoteObj();
         
      	//Naming.rebind("obj", obj);
         LocateRegistry.createRegistry(porti); //Aloko regjistrin
         Naming.rebind("rmi://192.168.1.4:"+porti+"/RMD", obj);// lidh implementimin me adresen
      	
      
      	/*(NDR3.) E kemi kriju nje instance te re te vequar nga loja*/
         int convertPort = new Integer(1996).intValue();//
         LocateRegistry.createRegistry(convertPort);//
         ChatImpl impl= new ChatImpl();//
         Naming.rebind("rmi://192.168.1.4:1099/chat", impl);//
      
      
         System.out.println("Server running.");
      	
      }
      catch (MalformedURLException e)
      {
         System.err.println(e.getMessage());
      }
      catch (RemoteException e)
      {
         System.err.println(e.getMessage());
      }
   }

}
