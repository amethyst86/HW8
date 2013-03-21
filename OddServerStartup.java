
public class OddServerStartup {

	public static void main(String[] args) {
		
		ServerFace serv = new OddLocalServer();
		NetListener listener = new NetListener();
		
		int port = 9876;
		listener.start("Odd", port, serv);

	}

}
