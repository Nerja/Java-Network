import java.io.IOException;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class Sniffer extends Thread {
	
	private String encoding;
	private Writer output;
	private MulticastSocket socket;
	private InetAddress multicastGroup;
	
	public Sniffer(String address, int port, String encoding, Writer output) throws IOException {
		this.encoding = encoding;
		this.output = output;
		multicastGroup = InetAddress.getByName(address);
		socket = new MulticastSocket(port);
		socket.joinGroup(multicastGroup);
		socket.setSoTimeout(1000); //Jump out and check if iterrupted
	}
	
	@Override
	public void run() {
		byte[] content = new byte[8192]; //Common
		DatagramPacket packet = new DatagramPacket(content, 0, content.length);
		while(!isInterrupted()) {
			try{
				socket.receive(packet);
				String contentString = new String(content, 0, packet.getLength(), encoding);
				output.write(contentString+"\n******************************************\n");
				output.flush();
			} catch (SocketTimeoutException e) {
				//Ignore
			} catch (IOException e) {
				e.printStackTrace();
				interrupt();
			}
		}
		try {
			socket.leaveGroup(multicastGroup);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
