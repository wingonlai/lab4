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
	
	public Sink(int Estimator, int nPackets, int PacketSize)
	{
		nEstimatorPort = Estimator;
		nToReceive = nPackets;
		nPacketSize = PacketSize;
	}
	
	public void run() {
		try
		{
			FileOutputStream fout =  new FileOutputStream("/homes/l/laiyong/workspace/lab4/sink.data");
			PrintStream pout = new PrintStream (fout);
			int nReceived = 0;
			DatagramSocket socket = new DatagramSocket(nEstimatorPort);
			byte[] buf = new byte[nPacketSize];
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			System.out.println("Waiting ...");
			long nLastReceiveTime = -1;
			while(nReceived < nToReceive)
			{
				socket.receive(p);
				long nOldLastReceiveTime = nLastReceiveTime;
				nLastReceiveTime = System.nanoTime();
				nReceived++;
				if(nOldLastReceiveTime == -1)
					pout.println(nReceived + "\t" + "0");
				else
					pout.println(nReceived + "\t" + (nLastReceiveTime - nOldLastReceiveTime));
				System.out.println("the number of packets received: " + nReceived);
			}
			pout.close();
			socket.close();
			System.out.println("finished sending!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
