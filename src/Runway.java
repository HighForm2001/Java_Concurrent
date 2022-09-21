import java.util.Random;

public class Runway {
	private Random r = new Random();
	private volatile int plane = 0;
	public Runway() {
		System.out.println("The runway is opened to be used.");
	}
	synchronized public void depart(AirCraft ac) {
		try {
			while(plane > 0) {
				wait();
			}
			plane ++;
			System.out.print("ATC: " + ac + " is leaving.......");
			int randomTime = r.nextInt(5)+1;
			Main.addLeavingTime(randomTime);//Add the total leaving time
			for(; randomTime>= 0; randomTime --) { 
				System.out.print(randomTime + "...");
				Thread.sleep(1000);
			}
			System.out.println(ac+ " departure Successful. Destination: " + ac.getDestination());	
			plane --;
			notifyAll();
			
		}catch(InterruptedException ex) {
			System.out.println("Errors in Runway");
		}
	}
	synchronized public void land(AirCraft ac) {
		try {
			while(plane >0) {
				wait();
			}
			plane++;
			System.out.print("ATC: " + ac + " is landing......");
			int randomTime = r.nextInt(1)+1;
			Main.addLandingTime(randomTime);//Add the total landing time
			for(; randomTime >= 0; randomTime --) {
				System.out.print(randomTime+ "...");
				Thread.sleep(1000);
			}
			System.out.println(ac + " landing Successful.");
			Main.serveAPlane();//When a plane landed, it means a plane is served.
			plane--;
			
			notifyAll();
		}catch(InterruptedException ex) {
			System.out.println("Errors in Runway");
		}
	}
}
