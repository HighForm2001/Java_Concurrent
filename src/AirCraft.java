import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
public class AirCraft extends Thread {
	private int id;
	private Runway rw;
	private String destination;
	private double fuel;
	private boolean mechMalf;
	private boolean mediIssue;
	List<Customer> leaving = new LinkedList<Customer>();
	List<Customer> entering = new LinkedList<Customer>();
	private HashSet<Integer> hs = new HashSet<Integer>();
	private volatile int walkingCustomer = 0;
	
	//Constructor
	public AirCraft(int id, Runway rw, String destination) {//Constructor to initialize the passengers etc, and situations
		this.id = id;
		this.rw = rw;
		this.destination = destination;
		fuel = Main.r.nextDouble() * 100;
		if(Main.r.nextDouble()*100 > Main.standardRate)
			mechMalf = true;
		if(Main.r.nextDouble()*100 > Main.standardRate)
			mediIssue = true;
		while(hs.size()<=Main.standardPassenger)
			hs.add(Main.r.nextInt(1000));
		fuel = Main.r.nextDouble() * 100;
		
		char character = (char)(id+64);
		Iterator<Integer> it = hs.iterator();
		Integer element = it.next();
		for(; it.hasNext();) {
			if(hs.size() <= Main.standardPassenger/2+1)
				break;
			leaving.add(new Customer(character + element.toString(), this, true));
			it.remove();
			element = it.next();
		}
		for (;it.hasNext();) {
			entering.add(new Customer(character + element.toString(), this, false));
			it.remove();
			element = it.next();
		}	
		//Add this aircraft to the linkedlist
		Main.aircrafts.add(this);
		if(fuel< 50)
			//goToFirst();
		this.start();
	}
	
	//AirCraft.start() will run this function
	public void run() {
		System.out.println(Main.aircrafts);//show the current list
		try {
				process();
		}catch(InterruptedException ex) {
			System.out.println("Aircraft run error");
		}
	}
	public void process() throws InterruptedException{
		Main.gates.acquire();//Acquire the Gate(Semaphore)
		rw.land(this);//Land the plane
		for(int i = 0; i < leaving.size(); i ++) 
			leaving.get(i).start();
		System.out.println(this + " is letting passengers to embark.");
		while(leaving.size() != 0) {
			Thread.sleep(0);//Modify sleep time,现在测试 所以换取0，原本是3000
			if(leaving.size()==0)
				break;
			//System.out.println(this + " still have " + leaving.size() + " passenger(s) leaving the plane, will wait for another 3 seconds.");

		}
		for(int i = 0; i < entering.size(); i ++)
			entering.get(i).start();
		while(entering.size()!=0) {
			Thread.sleep(0); //Modify sleep time, 现在测试 所以换取0，原本是5000
			if(entering.size()==0)
				break;
			//System.out.println(this + " still has " + entering.size() + " passenger(s) entering the plane, will wait for another 5 seconds.");

		}
		System.out.println(this + " has no more passngers entering the plane. Request to departure.");
		rw.depart(this);
		Main.aircrafts.remove(this);
		System.out.println(Main.aircrafts);
		Main.gates.release();
	}
	
	public String toString() {
		return "Aircraft " + id;
	}
	public String getDestination() {
		return destination;
	}
	
	//Customer Action
	synchronized public void customerAction(Customer c) {
		long useTime = 50;
		try {
			while(walkingCustomer > 0)
				wait();
			walkingCustomer ++;
			if(c.isLeaving()) {//customer leaving
//				System.out.println("Customer "+ c.getID()+ " from " + this + " is leaving.");
//				Thread.sleep(useTime);
//				System.out.println("Customer " + c.getID() + " left " + this + ".");
				leaving.remove(c);
			}
			else {
//				System.out.println("Customer "+ c.getID() + " is entering " + this + ".");
//				Thread.sleep(useTime);
//				System.out.println("Customer " + c.getID() + " entered " + this + ".");
				entering.remove(c);
			}
			walkingCustomer--;
			notifyAll();
			
		}catch(InterruptedException ex) {
			System.out.println("Customer run error.");
		}
	}

}
