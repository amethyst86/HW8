// Purpose.  Proxy design pattern
// 1. Create a "wrapper" for a remote, or expensive, or sensitive target
// 2. Encapsulate the complexity/overhead of the target in the wrapper
// 3. The client deals with the wrapper
// 4. The wrapper delegates to the target
// 5. To support plug-compatibility of wrapper and target, create an interface

import java.io.*;   
//import java.net.*;

public class Client {

   public void start (ServerFace server, int number) { // pass in the server
	                                        // base class, so it can be a proxy or
														 // the real thing
	   System.out.println("\nclient starting...");
  	   System.out.println(" result: " + server.mashNum( number ) );
      	   System.out.println("ok, client is done");

	}  
}


