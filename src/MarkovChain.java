public class MarkovChain{
	static int maxStateNumber = Constant.MaxStateNumber;
	
	protected boolean timeIndependent = true;
	protected int stateNumber;
	protected double[][] p;//transitionMatrix
	protected double[] pie; // probability distribution of each state
	
	
	
	MarkovChain(int state){
		setStateNumber(state);
		p = init1(state);
		initMatrix(p,state);
		pie = init2(state);
		initVector(pie,state);
	}

	public int go(int current){
		int a = sampleFromVector(p[current],stateNumber);
		return a;
	}
	public int getStateNumber(){
		return stateNumber;
	}
	public void setStateNumber(int target){
		stateNumber = target;
		return;
	}
	public void pieConstruction(){
		return;
	}
	public void pConstruction(){
		return;
	}	
	
	
	/*
	 * Sample from vector distribution
	 * Given p[] probability measure
	 * return sample result
	 */
	protected int sampleFromVector(double p[],int size){		
		double r = Math.random();
//		System.out.println("r = " +r);
		double F = 0;
		for (int i = 0; i < size; i ++){
			F += p[i];
			if (r <= F){
//				System.out.println("return i= " +i);
				return i;
			}
		}
		return -1; // error
	}
	
	
	
	
	
	
	// init transition matrix
	protected double[][] init1(int n) {
		double [][]d = new double[n][];
		for (int i = 0; i < n; i++){
			d[i] = new double[n];
		}
		return d;
	}
	// init transition matrix :assign
	protected void initMatrix(double[][] m,int n){
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				m[i][j] = 0;
			}
		}
	}
	// init vector pie
	protected double[] init2(int n) {
		double []d = new double[n];
		return d;
	}
	// init vector pie : assign uniformly
	protected void initVector(double[] v,int n){
		for (int i = 0; i < n ; i++){
			v[i] = 1 * 1.0 / n;
		}
	}
}