import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;
public class Main {
	public static LinkedList<AirCraft> aircrafts = new LinkedList<AirCraft>() {
		@Override
		public boolean add(AirCraft a) {//rewrite the method to have a better visualization
			addLast(a);
			System.out.println("ATC: " + a + " is ready to land!");
			return true;
		}
		@Override
		public String toString() {
			if(size() == 0)
				return "No Airplane left.";
			
			System.out.print("\nATC: Current Queue: {");
			for(int i = 0; i < size(); i ++) {
				if(i==size()) {
					System.out.print(get(i) + "}");
					break;
				}
				System.out.print(get(i)+ ", ");
				
			}
			return "";
		}
		
	};
	public static String[] countries = {"Argentina", "Australia", "Bangladesh", "China","Denmark", "Egypt",
			 "France","Germany", "Hong Kong","Iceland","India","Japan","Kenya","Macau",
			 "Malaysia", "Mexico","New Zealand","Norway","Pakistan","Singapore","Switzerland",
			 "Taiwan","Thailand","United States","United Kingdom", "Vietnam"};
	private static HashSet<Integer> hs = new HashSet<Integer>();
	public static Random r = new Random();
	public static Semaphore gates = new Semaphore(4);
	public static int standardPassenger = 4;
	public static int standardAircraftRange = 26;
	public static int standardRate = 95;
	public static Runway rw = new Runway();
	private static int landingTime = 0;
	private static int leavingTime = 0;
	private static int servedPlane = 0;

	public static void main(String[] args) {
		System.out.println(countries.length);
		while(hs.size() != 2)
			hs.add(r.nextInt(standardAircraftRange)+1);
		for(Integer j:hs) {
			new AirCraft(j,rw,countries[j-1]);
		}
		
		
	}
	
	public static void addLandingTime(int i ) {
		landingTime+= i;
	}
	public static void addLeavingTime(int i ) {
		leavingTime +=i;
	}
	public static void serveAPlane() {
		servedPlane++;
	}

}
