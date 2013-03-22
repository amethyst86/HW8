// Flyweight, Chain of Responsibility and Proxy Demo
// this file contains the main entry.

import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.util.Hashtable;
import java.util.Enumeration;


// Flyweight Factory used to generate Buttons
class FlyweightFactory {
	// Button Cache
	private static Hashtable<String, Button> ht = new Hashtable<String, Button>();
	
	// all buttons share the same button listener
	private static ButtonListener bl = new ButtonListener();
	
	public static Button makeButton( String num ) {
		
		// if the requested Button exists
		if (ht.containsValue( num ))
			return (Button) ht.get( num );
		
		// otherwise, generate one and cache it
		Button btn = new Button( num );          
		btn.addActionListener( bl );
		ht.put( num, btn );
		
		return btn;
	}
	
	// report buttons created
	public static void report() {
		System.out.print( "size=" + ht.size() + "   " );
		for (Enumeration<String> e = ht.keys(); e.hasMoreElements(); )
			System.out.print( e.nextElement() + " " );
		System.out.println();
	}  
}

// ButtonListener: actions performed when button is clicked
class ButtonListener implements ActionListener {

	public void actionPerformed( ActionEvent e) {
		Button      btn  = (Button) e.getSource();
		ClosableFrame parent = (ClosableFrame)(btn.getParent());
		
		Component[] btns = btn.getParent().getComponents();
		
		// find the location of clicked button in the layout
		int i = 0;
		for (; i < btns.length; i++)
			if (btn == btns[i]) break;
		
		System.out.println( "label-" + e.getActionCommand()
				+ "  x-" + i/FlyweightDemo.NUM   + "  y-" + i%FlyweightDemo.NUM );  // 1. Identify extrinsic state
		
		// ask chain to handle it
		CompResult res = parent.chain.handle( Integer.parseInt( e.getActionCommand() ) );

		// display the results through messagebox
		JOptionPane.showMessageDialog(btn.getParent(), res.to_string(), "Your Result", JOptionPane.INFORMATION_MESSAGE);
	}  
}


// Closable Framework
class ClosableFrame extends Frame {
	
	static final long serialVersionUID = 723L;
	
	String serv_ip = "127.0.0.1";
	int prime_port = 5561;
	int odd_port = 9876;
	
	ServerFace srvPrime = new PrimeNetServer(serv_ip, prime_port);
	ServerFace srvOdd = new OddNetServerProxy(serv_ip, odd_port, (long)(3e10));
	ServerFace srvEven = new EvenServerProxy();
	
	LinkFace chain = null;
	
	LinkFace setUpChain() {
		
		// setup the chain
		LinkFace first = new PrimeLink( srvPrime );
		LinkFace second = new OddLink( srvOdd );
		LinkFace third = new EvenLink( srvEven );
		first.addLast( second );
		first.addLast( third );

		return first;
	}
	
	public ClosableFrame() {
		
		// setup the event chain
		chain = setUpChain();
		
		// add close event handler
		addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent e ) {
				
				srvPrime.dispose();
				srvOdd.dispose();
				srvEven.dispose();
				
				System.exit(0);
			}
		} );
	}
}

class FlyweightDemo {
	
	// grid width and random number max
	public static final int NUM = 15;
	public static final int RAN = 224;
	
	public static void main( String[] args ) {
		
		ClosableFrame frame = new ClosableFrame();
		frame.setLayout( new GridLayout( NUM, NUM ) );
		for (int i=0; i < NUM; i++)
			for (int j=0; j < NUM; j++)
				// 3. The client must use the Factory to request objects
				frame.add( FlyweightFactory.makeButton( 
						Integer.toString( (int)(Math.random()*RAN) + 1 ) ) );
		frame.pack();
		frame.setVisible( true );
		FlyweightFactory.report();
	} 
}
