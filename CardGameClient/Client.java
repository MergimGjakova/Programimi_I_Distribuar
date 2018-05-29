import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.Container;
import java.awt.event.ActionListener;


public class Client extends JFrame{

   JFrame frame=new JFrame();
   JFrame frame2=new JFrame();
   JButton b1=new JButton("OK");
   JTextField feild=new JTextField("");
   JTextField feild2=new JTextField("");
   JLabel lab=new JLabel("IP :");

   JLabel lab2=new JLabel("Name :");
   protected int i=0;

   private Client(){
      frame.setLayout(null);
   
      frame.add(b1);
      frame.add(feild);
      frame.add(feild2);
      frame.add(lab);
   
      frame.add(lab2);
   
      b1.setBounds(110,80,80,20);
      feild.setBounds(110,25,100,20);
      feild2.setBounds(110,45,100,20);
      lab.setBounds(60,25,80,15);
   
      lab2.setBounds(60,45,80,15);
   
   
   
      b1.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent e){
               String host=feild.getText();
               String name=feild2.getText();
               if(name.equals("")| host.equals(""))
               {
                  frame.setTitle("No Blank allowed!");
                  return;
               
               }
            
            
               new SimpleGUI(host,name).setVisible(true);

                /*(NDR2.)E kemi thirr metoden Main per lancim te chatit me parametrat host dhe name*/

               Main main = new Main(host, name);

               frame.setVisible(false);	//password frame disappeared
            
            
            }
         });
      feild.addMouseListener(
         new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
            //System.out.println("mouseClicked()"); // TODO Auto-generated Event stub mouseClicked()
               feild.selectAll();
            }
         });
   
      feild2.addMouseListener(
         new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
            //System.out.println("mouseClicked()"); // TODO Auto-generated Event stub mouseClicked()
               feild2.selectAll();
            }
         });
      frame.setSize(300,150);
   
      frame.setDefaultCloseOperation(1);
   
      frame.setLocation(400,300);
      frame.setAlwaysOnTop(true);
      frame.setResizable(false);
      frame.setVisible(true);
   
   
   }

   public static void main(String args[]){
   
   
      new Client();
   
   }

}