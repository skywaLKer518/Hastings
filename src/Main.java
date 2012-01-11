import java.io.IOException;

public class Main{
	public static void main(String args[]) throws IOException{
//		Log log = new Log("log.txt");
		
		HastingsMC HMC = new HastingsMC(20);
//		HMC.qConstruction3();
		
//		HMC.initial2();
		HMC.initial();
		
		HMC.Sample1(0, 200000);
//		HMC.Sample0(200000);
//		HMC.Sample2(300000);
		
		
		
		
		
		
		
		
		
		
		
		
		
//		log.close();
		return;
	}
}