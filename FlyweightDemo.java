// Purpose.  Flyweight design pattern
//
// 1. Identify shareable state (intrinsic) and non-shareable state (extrinsic)
// 2. Create a Factory that can return an existing object or a new object
// 3. The client must use the Factory instead of "new" to request objects
// 4. The client (or a third party) must compute the extrinsic state

import java.awt.*;
import java.awt.event.*;

class FlyweightFactory {
   private static java.util.Hashtable ht = new java.util.Hashtable();
   private static ButtonListener bl = new ButtonListener();
   public static Button makeButton( String num, LinkFace chain ) {
      if (ht.containsValue( num ))
         return (Button) ht.get( num );        // 2. Return an existing object
      Button btn = new Button( num );          // 1. Identify intrinsic state
      bl.setChain( chain );
      btn.addActionListener( bl );
      ht.put( num, btn );
      return btn;                              // 2. Return a new object
   }
   public static void report() {
      System.out.print( "size=" + ht.size() + "   " );
      for (java.util.Enumeration e = ht.keys(); e.hasMoreElements(); )
         System.out.print( e.nextElement() + " " );
      System.out.println();
   }  
}

class ButtonListener implements ActionListener {
   private LinkFace chain;
   public void setChain (LinkFace _chain) {
	chain = _chain;
   }	
   public void actionPerformed( ActionEvent e) {
      Button      btn  = (Button) e.getSource();
      Component[] btns = btn.getParent().getComponents();
      float stuff[] = new float[500];
      int i = 0;
      for ( ; i < btns.length; i++)
         if (btn == btns[i]) break;
      // 4. A third party must compute the extrinsic state (x and y)
      //    (the Button label is intrinsic state)
      System.out.println( "label-" + e.getActionCommand()
         + "  x-" + i/FlyweightDemo.NUM   + "  y-" + i%FlyweightDemo.NUM );  // 1. Identify extrinsic state
      chain.handle( Integer.parseInt( e.getActionCommand() ) );
   }  
}

class ClosableFrame extends Frame {
	public ClosableFrame() {
		addWindowListener( new WindowAdapter() {
	 	public void windowClosing( WindowEvent e ) {
			FlyweightDemo.chain.dispose();	
	    		System.exit(0);
		}
      		} );
	}
}

class FlyweightDemo {
   public static final int NUM = 15;
   public static final int RAN = 224;
   static LinkFace chain = setUpChain();
   public static void main( String[] args ) {
      ClosableFrame frame = new ClosableFrame();
      frame.setLayout( new GridLayout( NUM, NUM ) );
      for (int i=0; i < NUM; i++)
         for (int j=0; j < NUM; j++)
            // 3. The client must use the Factory to request objects
            frame.add( FlyweightFactory.makeButton( 
               Integer.toString( (int)(Math.random()*RAN) ), chain ) );
      frame.pack();
      frame.setVisible( true );
      FlyweightFactory.report();
   }  

   static LinkFace setUpChain() {
      // set up the chain
      ServerFace srvPrime = new PrimeNetServer("127.0.0.1", 5561);
      ServerFace srvOdd = new OddNetServerProxy("127.0.0.1", 9876, (long)(3e10));
      ServerFace srvEven = new EvenServerProxy();
      
      LinkFace first = new PrimeLink( srvPrime );
      LinkFace second = new OddLink( srvOdd );
      LinkFace third = new EvenLink( srvEven );
      first.addLast( second );
      first.addLast( third );
   
      return first;
   }
}
