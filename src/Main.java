import java.io.IOException;

public class Main{
	public static void main(String args[]) throws IOException{
//		Log log = new Log("log.txt");
		
		HastingsMC HMC = new HastingsMC(20);
//		HMC.qConstruction3();
//		System.out.println(HMC.step1(5));
//		System.out.println(HMC.step1(2));
//		System.out.println(HMC.step1(1));
		
		HMC.initial();
		
		HMC.Sample(0, 2000);
//		HMC.Sample2(200000);
		
		
		
		
		
		
		
		
		
		
		
		
		
//		log.close();
		return;
	}
}