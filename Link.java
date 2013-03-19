

// Purpose.  Chain of Responsibility - links bid on job, chain assigns job
//
// 1. Base class maintains a "next" pointer
// 2. Each "link" does its part to handle (and/or pass on) the request
// 3. Client "launches and leaves" each request with the chain
// 4. The current bid and bidder are maintained in chain static members
// 5. The last link in the chain assigns the job to the low bidder

import java.util.Arrays;

public class Link {
      private static boolean[] primes = new boolean[255];

      private int id;
      private ServerFace  srv;
      private Link next;                       // 1. "next" pointer
      
      private static Link handler;

      public Link( int num, ServerFace server ) {
	 id = num;
         srv = server;
	 fillPrime();
      }

      private static void fillPrime() {
         Arrays.fill( primes, true);
         primes[0] = primes[1] = false;
         for (int i=2; i<primes.length; i++) {
	     if( primes[i] ){
	         for( int j=2; i*j<primes.length; j++){
		     primes[i*j] = false;
	         }
	     }
         } 
      }

      public void addLast( Link l ) {
         if (next != null) next.addLast( l );  // 2. Handle and/or pass on
         else              next = l; 
      }

      public void handle(int number) {
         //System.out.print( id + ":bid " + num + "("+theBid+" low),  " );
	 if( primes[number] )
	 {
		if( id == 1 ) handler = this;
	 }
	 else if( number%2 == 1 )
	 {
		if( id == 2 ) handler = this;
	 }
         else
	 {
		if( id == 3 ) handler = this;
	 }
         if (next != null) next.handle(number);         // 2. Handle and/or pass on
         else              handler.execute(number);   // 5. The last 1 assigns the job
      }

      public void execute(int number) {
         Client clt = new Client();
	 clt.start( srv, number );
	 System.out.println( "done. >>> Processor " + id + " handles " + number + "\n" );
   }

}  




