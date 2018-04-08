package Estimator;

import java.io.IOException;

public class Estimator implements Runnable{
	private Generator generator;
	private Sink sink;
	
	public Estimator(int nBlackBoxPort, int nEstimatorPort, int PacketsInTrain, int PacketSize, int Rate)
	{
		int nRateInBytePerSecond = Rate * 1000 / 8;
		int Gap = (int) (PacketSize * java.lang.Math.pow(10,9) / nRateInBytePerSecond);
		System.out.println("The generator will send " + PacketsInTrain + " packets with size " + PacketSize + " Bytes. The gap between two packets is " + Gap + " nanoseconds.");
		generator = new Generator(nBlackBoxPort, nEstimatorPort, PacketsInTrain, PacketSize, Gap);
		sink = new Sink(nEstimatorPort, PacketsInTrain, PacketSize);
	}
	
	public void run() {
		new Thread(sink).start();
		new Thread(generator).start();
	}
	
	public static void main(String[] args) throws IOException {
		Estimator estimator = new Estimator(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
		new Thread(estimator).start();
	}
}
