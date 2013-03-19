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
   public static Button makeButton( String num, Link chain ) {
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
   private Link chain;
   public void setChain (Link _chain) {
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
      System.out.print( "label-" + e.getActionCommand()
         + "  x-" + i/FlyweightDemo.NUM   + "  y-" + i%FlyweightDemo.NUM );  // 1. Identify extrinsic state
      chain.handle( Integer.parseInt( e.getActionCommand() ) );
   }  
}

class FlyweightDemo {
   public static final int NUM = 15;
   public static final int RAN = 224;
   static Link chain = setUpChain();
   public static void main( String[] args ) {
      Frame frame = new Frame( "Flyweight Demo" );
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

   static Link setUpChain() {
      // set up the chain
      ServerFace srvPrime = new ServerPrime();
      ServerFace srvOdd = new ServerOdd();
      ServerFace srvEven = new ServerEven();
      
      Link first = new Link( 1, srvPrime );
      Link second = new Link( 2, srvOdd );
      Link third = new Link( 3, srvEven );
      first.addLast( second );
      first.addLast( third );
   
      return first;
   }
}

// size=25   24 23 22 21 20 19 18 17 16 9 15 8 14 13 7 12 6 5 11 10 4 3 2 1 0
// label-23  x-0  y-0
// label-7  x-0  y-1
// label-21  x-1  y-1
// label-21  x-4  y-6
// label-7  x-9  y-9

