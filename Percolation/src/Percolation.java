import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.QuickFindUF;

/**
 * The Percolation data structure represents an N-N grid within which each grid
 * location can be connected to its neighboring location (left, right, up,
 * down). Each site can be either closed, open, or full, if its full that means
 * there's a path of open sites that connects it to the upper row, we say that
 * the system "percolates" if at least one site in the bottom row is full @
 * @author Saed Mami
 */

public class Percolation {
	private WeightedQuickUnionUF _grid; // N-N grid that represent the
										// percolation system, represented by a
										// quickWightedUF object
	private WeightedQuickUnionUF _secondaryGrid;
	private int _n; // the grid's dimension
	private final int _hiddenNodesCount = 2; // we need to extra hidden nodes at
												// the top and at the bottom of
												// the
												// grid to check for percolation
												// efficiently
	private final int _upperHiddenNodeIndex = 0;
	private int _lowerHiddenNodeIndex;
	private boolean[] _isOpen; // tracking of open sites in the 1D array ,
								// initially all false

	/**
	 * Initializes an empty percolation data structure with <tt>N</tt> sites
	 * Each site is initially closed.
	 *
	 * @param N
	 *            the dimension of the percolation grid
	 * @throws IllegalArgumentException
	 *             if <tt>N &lt; 0</tt>
	 */
	public Percolation(int N) // create N-by-N grid, with all sites blocked
	{
		if (N <= 0) {
			throw new IllegalArgumentException("invalid grid size");
		}

		_n = N;
		// create the grid including the upper and lower hidden nodes
		_grid = new WeightedQuickUnionUF(N * N + _hiddenNodesCount);
		_secondaryGrid = new WeightedQuickUnionUF(N * N + 1);
		_lowerHiddenNodeIndex = N * N + 1;
		_isOpen = new boolean[N * N + _hiddenNodesCount];

		// open the upper and lower nodes
		_isOpen[_upperHiddenNodeIndex] = true;
		_isOpen[_lowerHiddenNodeIndex] = true;
	}

	/**
	 * Opens a site in the grid designated by the indices(i,j), if any of the
	 * adjacent sides are open, then we will perform a union operation between
	 * these sites
	 *
	 * @param i
	 *            the row index of the designated site to open
	 * @param j
	 *            the column index of the designated site to open
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if either i or j are invalid to access the N-N grid
	 */
	
	public void open(int i, int j) {
		if (!(isValidIndex(i) && isValidIndex(j))) {
			throw new java.lang.IndexOutOfBoundsException("invalid indexing into the grid");
		}

		if (isOpen(i, j))
			return;

		int oneDIndex = from2DTo1D(i, j);

		// if the site is in the first row then we have to connect it with the
		// virtual upper node
		if (i == 1) {
			_grid.union(_upperHiddenNodeIndex, oneDIndex);
			_secondaryGrid.union(_upperHiddenNodeIndex, oneDIndex);
		}

		// if the site is in the last row then we have to connect it with the
		// virtual lower node
		if (i == _n) {
			_grid.union(_lowerHiddenNodeIndex, oneDIndex);
		}

		// union with open adjacent sites
		unionWithOpenNeighbours(i, j);
		
		// finally mark the site as open
		_isOpen[oneDIndex] = true;
	}

	/**
	 * Checks whether a grid site designated by index(i,j) is open or not
	 *
	 * @param i
	 *            the row index of the designated site
	 * @param j
	 *            the column index of the designated site
	 *            
	 * @return  if grid site at grid[i,j] open or not
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if either i or j are invalid to access the N-N grid
	 */
	public boolean isOpen(int i, int j) {
		if (!(isValidIndex(i) && isValidIndex(j))) {
			throw new java.lang.IndexOutOfBoundsException("invalid indexing into the grid");
		}
		int oneDIndex = from2DTo1D(i, j);
		return _isOpen[oneDIndex];
	}

	/**
	 * Checks whether a grid site designated by index(i,j) is full, that mean that it is 
	 * connected with the upper hidden node
	 *
	 * @param i
	 *            the row index of the designated site
	 * @param j
	 *            the column index of the designated site
	 *            
	 * @return whether the grid site at grid[i,j] is full or not           
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if either i or j are invalid to access the N-N grid
	 */
	
	public boolean isFull(int i, int j) {
		if (!(isValidIndex(i) && isValidIndex(j))) {
			throw new java.lang.IndexOutOfBoundsException("invalid indexing into the grid");
		}
		// we simply check if the designated site is connected to the upper hidden node
		int oneDIndex = from2DTo1D(i, j);
		if (_secondaryGrid.connected(oneDIndex, _upperHiddenNodeIndex)) {
			return true;
		}
		else {
			return false;
		}
	} 

	/**
	 * Checks whether or not the whole grid system percolates, that mean that there is a path
	 * of full grid sites going from the top of the grid to the bottom, we simply do a check whether 
	 * or not the upper hidden node is connected to the lower hidden node
	 *
	 *            
	 * @return whether or not the grid system percolates           
	 * 
	 */
	public boolean percolates() {
		if (_grid.connected(_upperHiddenNodeIndex, _lowerHiddenNodeIndex)) {
			return true;
		}
		
		return false;
	} 

	public static void main(String[] args) {
	} // test client (optional)

	private int from2DTo1D(int i, int j) { // convert 2d indexing to 1D indexing
											// the underlying array
		return (i - 1) * _n + j;
	}

	// see whether the indexing parameter is valid to index our underlying UF structure
	private boolean isValidIndex(int i) {
		if (i > 0 && i <= _n) {
			return true;
		}
		return false;
	}

	private void unionWithOpenNeighbours(int i, int j) {
		int oneDIndex = from2DTo1D(i, j);
		
		// left
		int leftj = j - 1;
		if (leftj > 0) {
			if (isOpen(i, leftj)) {
				int leftAdjacent1DIndex = from2DTo1D(i, leftj);
				_grid.union(oneDIndex, leftAdjacent1DIndex);
				_secondaryGrid.union(oneDIndex, leftAdjacent1DIndex);
			}
		}

		// right
		int rightj = j + 1;
		if (rightj <= _n) {
			if (isOpen(i, rightj)) {
				int rightAdjacent1DIndex = from2DTo1D(i, rightj);
				_grid.union(oneDIndex, rightAdjacent1DIndex);
				_secondaryGrid.union(oneDIndex, rightAdjacent1DIndex);
			}
		}

		// up
		int upperi = i - 1;
		if (upperi > 0) {
			if (isOpen(upperi, j)) {
				int upperAdjacent1DIndex = from2DTo1D(upperi, j);
				_grid.union(oneDIndex, upperAdjacent1DIndex);
				_secondaryGrid.union(oneDIndex, upperAdjacent1DIndex);
			}
		}

		// down
		int loweri = i + 1;
		if (loweri <= _n) {
			if (isOpen(loweri, j)) {
				int lowerAdjacent1DIndex = from2DTo1D(loweri, j);
				_grid.union(oneDIndex, lowerAdjacent1DIndex);
				_secondaryGrid.union(oneDIndex, lowerAdjacent1DIndex);
				
			}
		}
	}
}