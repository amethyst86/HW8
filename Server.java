import java.lang.Math;
import java.util.Map;
import java.util.TreeMap;

// computation result class
enum CompTag {
	REMOTE, LOCAL, CACHE
}

class CompResult {
	
	CompTag tag;
	int value;
	
	CompResult(int arg1, CompTag arg2) 
	{
		value = arg1;
		tag = arg2;
	}
}


// Server face
interface ServerFace {
	CompResult mash_number(int number);
	void dispose();
}



//
// Even Servers
//
class EvenLocalServer implements ServerFace {

	public CompResult mash_number(int number) {
		assert(number % 2 == 0);
		// compute absolute value plus one
		return new CompResult(Math.abs(number)+1, CompTag.LOCAL);
	}
	
	public void dispose() {}
	
}

class EvenServerProxy implements ServerFace {
	
	ServerFace serv = new EvenLocalServer();
	
	public CompResult mash_number(int number) {
		assert(number % 2 == 0);
		if( number % 6 == 0)
			return new CompResult(-1, CompTag.LOCAL);
		else {
			return serv.mash_number(number);
		}
	}
	
	public void dispose() {
		serv.dispose();
	}
	
}


//
// Odd Servers
//


class OddNetServer implements ServerFace {
	
	ClientNetProxy net_proxy;
	
	OddNetServer( String host, int port ) 
	{
		net_proxy = new ClientNetProxy(host, port);
	}
	
	public CompResult mash_number(int number) {
		assert(number % 2 == 1);
		
		net_proxy.sendMessage(Integer.toString(number));
		return new CompResult(Integer.parseInt(net_proxy.getMessage()), CompTag.REMOTE);
	}
	
	public void dispose() {
		net_proxy.dispose();
	}
}


class OddLocalServer implements ServerFace {

	public CompResult mash_number(int number) {
		
		return new CompResult((int)(Math.random() * 1000) + number, CompTag.LOCAL);
		
	}
	
	public void dispose() {}
	
}



class CachedResult {
	
	CachedResult(int arg1, long arg2) {
		value = arg1;
		time = arg2;
	}
	
	public int value;
	public long time;
	
}


class OddNetServerProxy implements ServerFace {
	
	Map<Integer, CachedResult> cache = new TreeMap<Integer, CachedResult>();
	OddNetServer serv;
	long maxInterval;
	
	OddNetServerProxy( String host, int port, long max_interv )
	{
		serv = new OddNetServer(host, port);
		maxInterval = max_interv;
	}

	
	public CompResult mash_number(int number) {
		assert(number % 2 == 1);
		
		long current_time = System.nanoTime();
		
		if( cache.containsKey(number) ) 
		{
			CachedResult res = cache.get(number);
			if( current_time - res.time <= maxInterval )
			{
				return new CompResult(res.value, CompTag.CACHE);
			}
		}
		
		CompResult res = serv.mash_number(number);
		CachedResult obj = new CachedResult(res.value, current_time);
		cache.put(number, obj);
		
		return res;
	}
	
	public void dispose() {
		serv.dispose();
	}
}



//
// Prime Servers
//


class PrimeNetServer implements ServerFace {
	
	ClientNetProxy net_proxy;
	
	PrimeNetServer( String host, int port ) 
	{
		net_proxy = new ClientNetProxy(host, port);
	}
	
	public CompResult mash_number(int number) {
		
		net_proxy.sendMessage(Integer.toString(number));
		return new CompResult(Integer.parseInt(net_proxy.getMessage()), CompTag.REMOTE);
	}
	
	public void dispose() {
		net_proxy.dispose();
	}

}


class PrimeLocalServer implements ServerFace {
	
	public CompResult mash_number(int number) {
		
		return new CompResult(number * 2, CompTag.LOCAL);
		
	}	
	
	public void dispose() {}
}


//
// network listener
//

class NetListener {
	
	ServerNetProxy net;
	ServerFace serv;
	
	public void start( String name, int port, ServerFace arg_serv ) {
		
		System.out.println(" (" + name + " net) listening at port " + Integer.toString(port));
		
		serv = arg_serv;
		
		net = new ServerNetProxy(port);
		net.accept();

		while (true) {
			
		    String str = net.getMessage();
			
			if( str != null && !str.isEmpty() ) {
				
				if( str.equals("close") ) {
					
					System.out.println(" (" + name + " net) client has closed the connection. ");
					System.out.println(" (" + name + " net) listening at port " + Integer.toString(port));
					net.accept();

					continue;
				}
				
				int num = Integer.parseInt(str);
				
				assert(num % 2 == 1);
				
				System.out.println(" (" + name + " net) server asked to handle " + Integer.toString(num));
				CompResult res = serv.mash_number(num);
				System.out.println(" (" + name + " net) server mashed and returned " + Integer.toString(res.value));
				
				net.sendMessage(Integer.toString(res.value));				
			}
		}
		
	}
}

