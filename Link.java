import java.util.Arrays;

// Chain's Link face
abstract class LinkFace {
	ServerFace srv;
      	LinkFace next;

      	public LinkFace( ServerFace server ) {
         	srv = server;
      	}

      	public void addLast( LinkFace l ) {
         	if (next != null) next.addLast( l );  
         	else              next = l; 
      	}

      	public void handle( int number ) {};

	void execute(int number) {
		CompResult res = srv.mash_number( number );
		print_result( res );
   	}

	public void dispose() {
		srv.dispose();
		if( next != null ) {
			next.dispose();
		}
	}

	static void print_result(CompResult res) {
		if( res.tag == CompTag.LOCAL ) {
			
			System.out.println("[Local] " + Integer.toString(res.value));
			
		}else if( res.tag == CompTag.REMOTE ) {
			
			System.out.println("[Remote] " + Integer.toString(res.value));
			
		}else if( res.tag == CompTag.CACHE ) {
			
			System.out.println("[Cache] " + Integer.toString(res.value));
		}
	}
}

//
//  Prime Link
//
class PrimeLink extends LinkFace {

      	private static boolean[] primes = new boolean[255];

	public PrimeLink( ServerFace server ) { 
		super( server );
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

      	public void handle(int number) {
         	fillPrime();
	 	if( primes[number] ) {
			System.out.println( "Running prime handler PH…" );
			execute(number);
			System.out.println( "Prime handler finished.\n" );
	 	}
	 	else {
			if( next == null ) System.out.println( "No handler after the prime one." );
			else next.handle(number);  
		}   
      	}
}  

//
//  Odd Link
//
class OddLink extends LinkFace {
	public OddLink( ServerFace server ) { 
		super( server );
	}

      	public void handle(int number) {
	 	if( number%2 == 1 ) {
			System.out.println( "Running odd handler OH …" );
			execute(number);
			System.out.println( "Odd handler finished.\n" );
	 	}
	 	else {
			if( next == null ) System.out.println( "No handler after the odd one." );
			else next.handle(number); 
		}    
      	}
}  

//
//  Even Link
//
class EvenLink extends LinkFace {
	public EvenLink( ServerFace server ) { 
		super( server );
	}

      	public void handle(int number) {
	 	if( number%2 == 0 ) {
			System.out.println( "Running even handler EH …" );
			execute(number);
			System.out.println( "Even handler finished.\n" );
	 	}
	 	else {
			if( next == null ) System.out.println( "No handler after the even one." );
			else next.handle(number);
		}     
      	}
}  





