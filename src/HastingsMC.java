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
	}
	
	/*
	 * in Hastings' method, p only relies on the ratio of pie_i / pie_j
	 * so, we construct s and alpha together without particular pie
	 */
	public void initial(){
		qConstruction2();
		sAlphaConstruct();
		pConstruction();
	}
	/*
	 *  construc transition matrix P for the Hastings Markov Chain
	 *  but actually pie should not be constructed
	 */
	public void initial2(){
		pieConstruction();
		qConstruction2();
		sConstructionM();
		alphaConstruction();
		pConstruction();
		// print the p matrix
//		for (int i = 0; i < stateNumber; i ++){
//			System.out.println(p[19][i]);
//		}
	}
	
	/*
	 * Example 1 : sample from poisson distribution
	 * a discrete sample
	 * fix number of states
	 * the key is to construct the transition matrix
	 */
	public void Sample1(int start, int times){
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
	
	/*
	 *  directly sample from pie
	 */
	public void Sample0(int times){
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
	 *  Example 2 : normal distribution
	 *  first x
	 *  then get [ex - delta, ex + delta]
	 *  sample y from it
	 *  x - > y 
	 *  if ( x^2 - y^2 ) > 0 x -> y
	 *  else if beta  x -> y
	 *  else x -> x
	 */
	
	public void Sample2(int times){
		Log log = new Log("log.txt");
		int record[] = new int[100];
		double x = 0;
		double y = 0;
		double e1 = 1;
		double e2 = -1;
		double delta = 1;
		double r1 = 0,r2 = 0;
		double m;
		double l;
		int zhengshu;
		for (int i = 0; i < times; i ++){
			r1 = Math.random();
			r2 = Math.random();
			y = 2 * delta * r1 + e1 * x - delta;
			m = x * x - y * y;
			if (m >= 0){
				x = y;
			}
			else {
				l = Math.pow(Math.E, 0.5 * m);
				if (r2 < l){
					x = y;
				}
				else;
			}
			zhengshu = (int)Math.floor(x * 10) + 50;
			record[zhengshu]++;
//			System.out.println(x);
			log.outln(x);
		}
		for (int i = 0; i < 100; i++){
			System.out.println(record[i]);
		}
		log.close();
	}
	
	/*
	 * Example 3 : multidimensional 
	 * 
	 */
	public void Sampe3(){
		// TODO
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
		 * Example 1 P101
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
//			System.out.println(pie[i]);
		}
		return;
	}
	
	/*
	 * construct s and alpha
	 * example 1 : Poisson Distribution
	 * pie_i+1 / pie_i = lamda / (i + 1), thus (q_ij the same)alpha_ij = 1 (if A<1) or 1/A (else), where A = pie_i / pie_j
	 */
	public void sAlphaConstruct(){
		int n = stateNumber;
		double lamda = 4;
		double ratio = 1;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j ++){
				if (i == j) ratio = 1;
				else {
					ratio = 1;
					int a = Math.max(i, j);
					int b = Math.min(i, j); 
					for (int k = 0; k < a - b; k++){
						ratio = ratio * lamda; 
					}
					for (int k = a; k >= b+1; k--){
						ratio = ratio / (k * 1.0);
					}
					if (i > j)	;
					else	ratio = 1 / ratio;
				}
				if (ratio<1)
					alpha[i][j] = 1;
				else
					alpha[i][j] = 1 / ratio;
			}
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