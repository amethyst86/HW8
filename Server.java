// Purpose.  Proxy design pattern
// 1. Create a "wrapper" for a remote, or expensive, or sensitive target
// 2. Encapsulate the complexity/overhead of the target in the wrapper
// 3. The client deals with the wrapper
// 4. The wrapper delegates to the target
// 5. To support plug-compatibility of wrapper and target, create an interface

//import java.io.*;   
//import java.net.*;

// the server will have a proxy to talk to sockets
// it also implements the same interface as the proxy the client talks to

interface ServerFace {                     // 5. To support plug-compatibility 
   String handle(String message);         //
	boolean getStatus();                  //    between the wrapper and the
   double mashNum(double param);        //    target, create an interface 
}

class ServerProxy implements ServerFace {
  Server srv = new Server();   // the target
  
  public String handle (String msg) {
      if (msg.equals("bot attack")){
		  return "(proxy repels bot attack, not sent to server)"; }
	   return srv.handle(msg);    // wrapper delegates to the target
	
  }	
  
  public double mashNum (double num) {
    if (num < 0.0) return 0.0;
	 return srv.mashNum(num);     // wrapper delegates to the target
  }
  
  public boolean getStatus () {
    return srv.getStatus();
  }

}

class ServerPrime implements ServerFace {
	public String handle (String msg) {
	   return "server got: "+msg;
	}	
	public double mashNum (double num) {
	   return num;
	}
	public boolean getStatus() {
	   return true;
	}
}

class ServerOdd implements ServerFace {
	public String handle (String msg) {
	   return "server got: "+msg;
	}	
	public double mashNum (double num) {
	   return 2*num;
	}
	public boolean getStatus() {
	   return true;
	}
}

class ServerEven implements ServerFace {
	public String handle (String msg) {
	   return "server got: "+msg;
	}	
	public double mashNum (double num) {
	   return 3*num;
	}
	public boolean getStatus() {
	   return true;
	}
}



public class Server implements ServerFace {  // it also implements the same interface
                                             // as the proxy the client talks to																
   public String handle (String msg) {
	   return "server got: "+msg;
	}	
	public double mashNum (double num) {
	   return 2.0*num;
	}
	public boolean getStatus() {
	   return true;
	}
	  
}
