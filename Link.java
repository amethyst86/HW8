
// Link: class represents node in the chain
// LinkFace: interface of different Links

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

	public CompResult handle( int number ) {
		return new CompResult(-1, CompTag.UNHANDLED);
	}
}


//Prime Link

class PrimeLink extends LinkFace {

	public PrimeLink( ServerFace server ) { 
		super( server );
	}

	//checks whether an integer is prime or not.
	private boolean isPrime(int n) {
		
		// deal with special cases
		if(n == 1)
			return false;
		if(n == 2)
			return true;
		
	    //check if n is a multiple of 2
	    if (n%2==0) return false;
	    //if not, then just check the odds
	    for(int i=3;i*i<=n;i+=2) {
	        if(n%i==0)
	            return false;
	    }
	    
	    return true;
	}

	public CompResult handle(int number) {
		if( isPrime(number) ) {
			return srv.mash_number(number);
		} else {
			if( next == null ) return new CompResult(-1,CompTag.UNHANDLED);
			else return next.handle(number);  
		}   
	}
}  


//Odd Link

class OddLink extends LinkFace {
	
	public OddLink( ServerFace server ) { 
		super( server );
	}
	
	private boolean isOdd(int n) 
	{
		if( n % 2 == 1 )
			return true;
		else
			return false;
	}

	public CompResult handle(int number) {
		if( isOdd( number ) ) {
			return srv.mash_number( number );
		}
		else {
			if( next == null ) return new CompResult(-1,CompTag.UNHANDLED);
			else return next.handle(number);
		}    
	}
}  



//Even Link
class EvenLink extends LinkFace {
	
	public EvenLink( ServerFace server ) { 
		super( server );
	}
	
	private boolean isEven(int n) 
	{
		if( n % 2 == 0 )
			return true;
		else
			return false;
	}

	public CompResult handle(int number) {
		if( isEven(number) ) {
			return srv.mash_number(number);
		}
		else {
			if( next == null ) return new CompResult(-1,CompTag.UNHANDLED);
			else return next.handle(number);
		}     
	}
}  





