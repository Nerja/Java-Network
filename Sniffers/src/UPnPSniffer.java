import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class UPnPSniffer {
	public static void main(String[] args) throws IOException {
		System.out.println("Starting to sniff enter # to exit");
		Sniffer sniff = new Sniffer("239.255.255.250", 1900, "UTF-8", new PrintWriter(System.out));
		sniff.start();
		Scanner scan = new Scanner(System.in);
		String line;
		while((line = scan.nextLine()) != null && !line.startsWith("#"));
		System.out.println("Got exit interrupt thread");
		sniff.interrupt();
		scan.close();
	}

}
