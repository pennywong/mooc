public class PercolationStats {
	private double[] x;
	private int T;

	// perform T independent computational experiments on an N-by-N grid
	public PercolationStats(int N, int T) {
		if (N <= 0)
			throw new IllegalArgumentException();

		if (T <= 0)
			throw new IllegalArgumentException();

		x = new double[T];
		this.T = T;

		for (int t = 0; t < T; t++) {
			Percolation percolation = new Percolation(N);
			int count = 0;
			while (percolation.percolates() == false) {
				int i = StdRandom.uniform(N) + 1;
				int j = StdRandom.uniform(N) + 1;

				if (percolation.isOpen(i, j) == false) {
					percolation.open(i, j);
					count++;
				}
			}

			x[t] = count * 1.0 / (N * N);
		}
	}

	// sample mean of percolation threshold
	public double mean() {
		return StdStats.mean(x);
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return StdStats.stddev(x);
	}

	// returns lower bound of the 95% confidence interval
	public double confidenceLo() {
		return mean() - 1.96 * stddev() / Math.sqrt(T);
	}

	// returns upper bound of the 95% confidence interval
	public double confidenceHi() {
		return mean() + 1.96 * stddev() / Math.sqrt(T);
	}

	// test client, described below
	public static void main(String[] args) {
		int N = StdIn.readInt();
		int T = StdIn.readInt();
		PercolationStats percolationStats = new PercolationStats(N, T);
		StdOut.println("mean                    = " + percolationStats.mean());
		StdOut.println("stddev                  = " + percolationStats.stddev());
		StdOut.println("95% confidence interval = "
				+ percolationStats.confidenceLo() + ", "
				+ percolationStats.confidenceHi());
	}
}
