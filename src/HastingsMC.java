/*
 * 2012.01.10
 * Kuan Liu
 * according to W.K.HASTINGS "Monte Carlo Sampling Methods using Markov chains and their applications"
 * 
 * 
 * given PIE,
 * 1, construct q
 * 2, construct s
 * 3, construct alpha
 * that is, we have already get p_ij in the form q_ij * alpha_ij (i != j) and p_ii = 1 - sigma p_ij
 * 4, so, construct p, that is we have get this markov chain~ right?
 */

public class HastingsMC extends MarkovChain{
	private double[][] q; //transition Matrix of an arbitrary Markov Chain
	private double[][] alpha; //transition Matrix of an arbitrary Markov Chain	
	private double[][] s; //transition Matrix of an arbitrary Markov Chain
	private double rejRate;
	
	HastingsMC(int stateN) {
		super(stateN);
		q = init1(stateN);
		initMatrix(q,stateN);
		s = init1(stateN);
		initMatrix(s,stateN);
		alpha = init1(stateN);
		initMatrix(alpha,stateN);
		
		// TODO Auto-generated constructor stub
	}
	
	// construc transition matrix P for the Hastings Markov Chain
	public void initial(){
		pieConstruction();
		qConstruction2();
		sConstructionM();
		alphaConstruction();
		pConstruction();
	}
	
	public void Sample(int start, int times){
		Log log = new Log("log.txt");
		int r[] = new int[stateNumber];
		int s = start;
		int d = -1; 
		for (int i = 0; i < times; i ++){
			d = go(s);
//			System.out.println("The "+i+"th result = " +d);
			r[d] ++;
//			log.outln(d);
			s = d;
		}
		for (int i = 0; i < stateNumber; i++){
			log.outln("r["+i+"] = "+r[i]);
		}
		log.newline();log.newline();
		for (int i = 0; i < stateNumber; i++){
			log.outln(r[i]);
		}
		for (int i = 0; i < stateNumber; i++){
			System.out.println(r[i]);
		}
		log.close();
	}
	
	public void Sample2(int times){
		int a = 0;
		int r[] = new int[stateNumber];
		for (int i = 0; i < times; i++){
			a = sampleFromVector(pie,stateNumber);
			if (a<0) continue;
			r[a]++;
		}
		for (int i = 0; i < stateNumber; i++){
			System.out.println(r[i]);
		}
	}
	/*
	 * step 1
	 * assume that X(t) = i and select j using the distribution given by the ith row of Q
	 * sample according to Q (ith row of Q)
	 */
	public int step1(int startingI){
		
		int j = sampleFromVector(q[startingI],stateNumber);
		if (j<0 || j>= stateNumber) System.out.println("error ddd");
		return j;
	}
	/*
	 * step 2
	 * take X(t+1) = j with probability alpha_ij and X(t+1) with probability 1 - alpha_ij
	 */
	public int step2(int startingI, int targetJ){
		double r = Math.random();
		if (r < alpha[startingI][targetJ]) return targetJ;
		else return startingI;
	}

	/*
	 * (non-Javadoc)
	 * @see MarkovChain#pieConstruction()
	 * 
	 */
	public void pieConstruction(){
		int n = stateNumber;
		/*
		 *  setting 1 uniform distribution
		 */
//		for (int i = 0; i < n; i ++){
//			pie[i] = 1.0 / n;
//		}
		/*
		 * setting 2 Poisson distribution Pie_i = lamda^i * e^-lamda / i!
		 */
//		double lamda = 10.0;
		double lamda = 4.0;
		double fac = 1;
		for (int i = 0; i < n; i ++){
			fac = 1;
			for (int j = 1; j < i; j++){
				fac = fac * (j+1);                // pay attention, here overflows
			}
//			System.out.println(i+"th fac =  "+ fac);
			pie[i] = Math.pow(lamda, i) * Math.pow(Math.E, -lamda) / fac;
			System.out.println(pie[i]);
		}
		
		return;
	}
	/*
	 * (non-Javadoc)
	 * @see MarkovChain#pConstruction()
	 * p_ij = q_ij * alpha_ij (i != j)
	 * p_ii = 1 - sigma p_ji  
	 */
	public void pConstruction(){
		int n = stateNumber;
		double sum = 0;
		for (int i = 0; i < n; i++){
			sum = 0;
			for (int j = 0; j < n; j++){
				if ( i == j)
					continue;
				else{
					p[i][j] = q[i][j] * alpha[i][j];
					sum += p[i][j];
				}
			}
			p[i][i] = 1 - sum;
		}
		return;
	}	
	public void qConstruction(){
		return;
	}
	// uniformly
	public void qConstruction2(){
		for (int i = 0; i < stateNumber; i++){
			for (int j = 0; j < stateNumber; j++){
				q[i][j] = 1.0 / stateNumber; 
			}
		}
		return;
	}
	public void qConstruction3(){
		for (int i = 0; i < stateNumber; i++){
			for (int j = 0; j < stateNumber; j++){
				q[i][j] = 0.5 / stateNumber; 
			}
			q[i][5] = 0.5 + 0.5 / stateNumber;
		}
		return;
	}
	public void alphaConstruction(){
		int n = stateNumber;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				alpha[i][j] = s[i][j] / (1 + pie[i] * q[i][j] / pie[j] / q[j][i]);
			}
		}
		return;
	}	
	/*
	 * s_ij construction
	 * using Metropolis Hastings way
	 */
	public void sConstructionM(){
		int n = stateNumber;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				if (pie[j] * q[i][j] - pie[i] * q[i][j] >= 0)
					s[i][j] = 1 + pie[i] * q[i][j] / pie[j] / q[j][i];
				else s[i][j] = 1 + pie[j] * q[j][i] / pie[i] / q[i][j];
			}
		}
		return;
	}
	/*
	 * s_ij construction
	 * using Barker's (1965) way
	 */
	public void sConstructionB(){
		int n = stateNumber;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				s[i][j] = 1;
			}
		}
		return;
	}
	public double getRejRate(){
		return rejRate;
	}
	public void setRejRate(double a){
		rejRate = a;
		return;
	}
}