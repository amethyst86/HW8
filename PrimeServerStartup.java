
public class PrimeServerStartup {

	public static void main(String[] args) {
		
		ServerFace serv = new PrimeLocalServer();
		NetListener listener = new NetListener();
		
		int port = 5561;
		listener.start("Prime", port, serv);

	}

}
