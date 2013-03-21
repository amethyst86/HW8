// Purpose.  Proxy design pattern
// 1. Create a "wrapper" for a remote, or expensive, or sensitive target
// 2. Encapsulate the complexity/overhead of the target in the wrapper
// 3. The client deals with the wrapper
// 4. The wrapper delegates to the target
// 5. To support plug-compatibility of wrapper and target, create an interface

import java.io.*;   
import java.net.*;

interface SocketInterface {                // 5. To support plug-compatibility 
   String getMessage();                      //    between the wrapper and the
   void   sendMessage( String str );         //    target, create an interface
   void   dispose(); 
}

// net proxy for client side
class ClientNetProxy implements SocketInterface {  
   private Socket         socket;            
   private BufferedReader in;                
   private PrintWriter    out;

   public ClientNetProxy( String host, int port ) {
      try {
         socket = new Socket( host, port );  
         in  = new BufferedReader( new InputStreamReader(socket.getInputStream()));
         out = new PrintWriter( socket.getOutputStream(), true );
      } catch( IOException e ) { e.printStackTrace(); }
   }
   
   public String getMessage() {
      String str = null;
      try { str = in.readLine();
      } catch( IOException e ) { e.printStackTrace(); }
      return str;
   }
   
   public void sendMessage( String str ) {
      out.println( str );             
   }
   public void dispose() {
      try { 
    	  sendMessage("close");
    	  socket.close();
      } catch( IOException e ) { e.printStackTrace(); }
   }  
}

// net proxy for server side
class ServerNetProxy implements SocketInterface {
	private ServerSocket servSocket;
	private Socket socket; 
	private BufferedReader in; 
	private PrintWriter out;

	public ServerNetProxy(int port) {
		try {
			servSocket = new ServerSocket( port );
		} catch ( IOException e ) { e.printStackTrace(); }
	}
	
	public void accept() {
		
		try {
			socket = servSocket.accept();
			in  = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter( socket.getOutputStream(), true );	
		} catch ( IOException e ) { e.printStackTrace(); }
		
	}
	

	public String getMessage() {
		String str = null;
		try {
			str = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	public void sendMessage(String str) {
		out.println(str); 
	}

	public void dispose() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
