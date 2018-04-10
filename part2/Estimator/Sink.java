package Estimator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Sink implements Runnable {
	private int nEstimatorPort;
	private int nToReceive;
	private int nPacketSize;
	private long[] timeStamp;
	
	public Sink(int Estimator, int nPackets, int PacketSize)
	{
		nEstimatorPort = Estimator;
		nToReceive = nPackets;
		nPacketSize = PacketSize;
		timeStamp = new long[nToReceive];
	}
	
	public void run() {
		try
		{
			FileOutputStream fout =  new FileOutputStream("./sink.data");
			PrintStream pout = new PrintStream (fout);
			int nReceived = 0;
			DatagramSocket socket = new DatagramSocket(nEstimatorPort);
			byte[] buf = new byte[nPacketSize];
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			long nLastReceiveTime = 0;
			while(nReceived < nToReceive)
			{
				System.out.println("Waiting for packet #" + (nReceived + 1) + " on port " + nEstimatorPort);
				socket.receive(p);
				long nOldLastReceiveTime = nLastReceiveTime;
				nLastReceiveTime = System.nanoTime();
				timeStamp[nReceived] = nLastReceiveTime;
				nReceived++;
				pout.println(nReceived + "\t" + (nLastReceiveTime - nOldLastReceiveTime)/1000);
				System.out.println("the number of packets received: " + nReceived);
			}
			pout.close();
			socket.close();
			System.out.println("finished receiving!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
