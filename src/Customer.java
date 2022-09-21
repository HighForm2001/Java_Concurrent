
public class Customer extends Thread{
	private String ID;
	private AirCraft ac;
	private boolean leaving;
	
	public Customer(String ID, AirCraft ac, boolean leaving) {
		this.ID = ID;
		this.ac = ac;
		this.leaving = leaving;
	}
	public String getID() {
		return ID;
	}
	public String getAirCraft() {
		return ac.toString();
	}
	public void run() {
		ac.customerAction(this);
		try {
			this.join();
		}catch(InterruptedException ex) {
			System.out.println("Error in Customer run");
		}
	}
	public boolean isLeaving() {
		return leaving;
	}
}
