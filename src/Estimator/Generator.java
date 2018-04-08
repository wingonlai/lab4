package Estimator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Generator implements Runnable {
	private int nBlackBoxPort;
	private int nEstimatorPort;
	private int nPacketsPerTrain;
	private int nPacketSize;
	private int nGap;
	private long[] timeStamp;
	public Generator(int BlackBox, int Estimator, int PacketsInTrain, int PacketSize, int Gap)
	{
		nBlackBoxPort = BlackBox;
		nEstimatorPort = Estimator;
		nPacketsPerTrain = PacketsInTrain;
		nPacketSize = PacketSize;
		nGap = Gap;
		timeStamp = new long[PacketsInTrain];
	}
	
	private void Wait(long nLastSentTime, boolean bWait)
	{
		if(!bWait)
			return;
		else
		{
			while(System.nanoTime() - nLastSentTime < nGap);
		}
	}
	
	public void run() {
		try
		{
			FileOutputStream fout =  new FileOutputStream("/homes/l/laiyong/workspace/lab4/generator.data");
			PrintStream pout = new PrintStream (fout);
			int nSent = 0;
			byte[] buf = new byte[nPacketSize];
			System.arraycopy(Utilities.toByteArray(nEstimatorPort),2,buf,0,2);
			InetAddress addr = InetAddress.getByName("127.0.0.1");
			DatagramSocket socket = new DatagramSocket();
			System.out.println("start sending!");
			long nLastSentTime = -1;
			while(nSent < nPacketsPerTrain)
			{
				System.arraycopy(Utilities.toByteArray(nSent),2,buf,2,2);
				DatagramPacket packet =
		                 new DatagramPacket(buf, buf.length, addr, nBlackBoxPort);
				Wait(nLastSentTime, nSent != 0);
				socket.send(packet);
				long nOldLastSentTime = nLastSentTime;
				nLastSentTime = System.nanoTime();
				timeStamp[nSent] = nLastSentTime;
				nSent++;
				if(nOldLastSentTime == -1)
					pout.println(nSent + "\t" + "0");
				else
					pout.println(nSent + "\t" + (nLastSentTime - nOldLastSentTime)); 
				System.out.println("the number of packets sent: " + nSent);
			}
			System.out.println("finished sending!");
			socket.close();
			pout.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
