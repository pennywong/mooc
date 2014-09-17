public class Percolation {
	private int N;
	private boolean[] array;

	private int virtualTop = 0;
	private int virtualBottom;

	private WeightedQuickUnionUF weightedQuickUnionUF;
	private WeightedQuickUnionUF fullWeightedQuickUnionUF;

	// create N-by-N grid, with all sites blocked
	public Percolation(int N) {
		if (N <= 0)
			throw new IllegalArgumentException();
	
		this.N = N;

		array = new boolean[N * N + 2];
		virtualBottom = N * N + 1;

		array[virtualTop] = true;
		array[virtualBottom] = true;

		weightedQuickUnionUF = new WeightedQuickUnionUF(N * N + 2);
		fullWeightedQuickUnionUF = new WeightedQuickUnionUF(N * N + 1);
	}

	// open site (row i, column j) if it is not already
	public void open(int i, int j) {
		int index = getIndex(i, j);
		if (!array[index]) {
			array[index] = true;

			union(index, i - 1, j);
			union(index, i + 1, j);
			union(index, i, j - 1);
			union(index, i, j + 1);
		}
	}

	private void union(int index, int i, int j) {
		if (j < 1 || j > N)
			return;

		// top
		if (i == 0) {
			weightedQuickUnionUF.union(virtualTop, index);
			fullWeightedQuickUnionUF.union(virtualTop, index);
		}
		// bottom
		else if (i == N + 1) {
			weightedQuickUnionUF.union(virtualBottom, index);
		} else {
			int p = getIndex(i, j);
			if (array[p]) {
				weightedQuickUnionUF.union(p, index);
				fullWeightedQuickUnionUF.union(p, index);
			}
		}
	}

	// is site (row i, column j) open?
	public boolean isOpen(int i, int j) {
		int index = getIndex(i, j);

		return array[index];
	}

	// is site (row i, column j) full?
	public boolean isFull(int i, int j) {
		int index = getIndex(i, j);

		return fullWeightedQuickUnionUF.connected(virtualTop, index);
	}

	public boolean percolates() {
		return weightedQuickUnionUF.connected(virtualTop, virtualBottom);
	}

	private int getIndex(int i, int j) {
		check(i);
		check(j);

		return (i - 1) * N + j;
	}

	private void check(int i) {
		if (i < 1 || i > N)
			throw new IndexOutOfBoundsException();
	}
}
