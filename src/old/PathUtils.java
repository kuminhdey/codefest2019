package old;
import java.util.EnumSet;
import java.util.List;
import java.util.Queue;

enum Direction {
	UP(0, -1, "1"), LEFT(-1, 0, "2"), DOWN(0, 1, "3"), RIGHT(1, 0, "4");

	int x;
	int y;
	String dir;

	Direction(int x, int y, String dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public static Direction fromString(String dir) {
		Direction ret = null;
		switch (dir) {
		case "1":
			ret = UP;
			break;
		case "2":
			ret = LEFT;
			break;
		case "3":
			ret = DOWN;
			break;
		case "4":
			ret = RIGHT;
			break;
		}
		return ret;
	}
	
	public static Direction invert(Direction dir) {
		Direction ret = null;
		switch (dir) {
		case UP:
			ret = DOWN;
			break;
		case LEFT:
			ret = RIGHT;
			break;
		case DOWN:
			ret = UP;
			break;
		case RIGHT:
			ret = LEFT;
			break;
		}
		return ret;
	}

}

public class PathUtils {

	

	public static EnumSet<Direction> DIRECTIONS = EnumSet
			.allOf(Direction.class);

	static Queue<Node> queue = new StaticQueue(1000000);

	static boolean[][] visited = new boolean[30][30];

	public static void init() {
		NodeFactory.init();
	}

	static boolean isWall(int x, int y, int[][] mat, Direction precom) {
		if (precom == null)
			return false;

		switch (precom) {

		case DOWN:
			if (!isValidCell(mat, x, y + 1)) {
				return true;
			}

			if (!isValidCell(mat, x + 1, y + 1)
					&& !isValidCell(mat, x - 1, y + 1))
				return true;

			if (!isValidCell(mat, x + 1, y + 1) && isValidCell(mat, x + 1, y)) {
				return true;
			}
			if (!isValidCell(mat, x - 1, y + 1) && isValidCell(mat, x - 1, y)) {
				return true;
			}

			break;

		case UP:
			if (!isValidCell(mat, x, y - 1)) {
				return true;
			}
			if (!isValidCell(mat, x + 1, y - 1)
					&& !isValidCell(mat, x - 1, y - 1))
				return true;

			if (!isValidCell(mat, x + 1, y - 1) && isValidCell(mat, x + 1, y)) {
				return true;
			}
			if (!isValidCell(mat, x - 1, y - 1) && isValidCell(mat, x - 1, y)) {
				return true;
			}
			break;
		case RIGHT:
			if (!isValidCell(mat, x + 1, y)) {
				return true;
			}
			if (!isValidCell(mat, x + 1, y + 1)
					&& !isValidCell(mat, x + 1, y - 1))
				return true;

			if (!isValidCell(mat, x + 1, y + 1) && isValidCell(mat, x, y + 1)) {
				return true;
			}
			if (!isValidCell(mat, x + 1, y - 1) && isValidCell(mat, x, y - 1)) {
				return true;
			}
			break;
		case LEFT:
			if (!isValidCell(mat, x - 1, y)) {
				return true;
			}
			if (!isValidCell(mat, x - 1, y + 1)
					&& !isValidCell(mat, x - 1, y - 1))
				return true;

			if (!isValidCell(mat, x - 1, y + 1) && isValidCell(mat, x, y + 1)) {
				return true;
			}
			if (!isValidCell(mat, x - 1, y - 1) && isValidCell(mat, x, y - 1)) {
				return true;
			}
			break;
		}
		return false;
	}

	public static boolean isValidCell(int mat[][], int row, int col) {
		return (row >= 0) && (row < mat.length) && (col >= 0)
				&& (col < mat[0].length) && mat[col][row] == 1;
	}
	
	private static int [] row  = {1,-1,1, -1, 1, 0,0 , -1 };
	private static int [] col  = {1,-1,-1, 1 , 0, 1,-1 , 0 };
	public static void drawInValidSquare(int mat[][], int x, int y, int size) {
	
		for (int i = 1; i <= size; i++) {
			for (int j = 0; j < row.length; j++) {
				int px = x + (row[j] * i);
				int py =  y + (col[j] * i);
				if(isValidCell(mat, px, py)){
					mat[py][px] = 0;
				}
			}
		}

	}

	public static Node searchBFS(int mat[][], int i, int j, int x, int y) {
		// construct a matrix to keep track of visited cells
		for (int n = 1; n < 30; n++) {
			for (int p = 1; p < 30; p++) {
				visited[n][p] = false;
			}
		}

		// create an empty queue
		Queue<Node> q = queue;
		q.clear();
		// mark source cell as visited and enqueue the source node
		visited[j][i] = true;
		Node root = NodeFactory.createNode(i, j, 0, null, null);
		q.add(root);

		// stores length of longest path from source to destination
		int min_dist = Integer.MAX_VALUE;
		Node node = null;

		// run till queue is not empty
		while (!q.isEmpty()) {
			// pop front node from queue and process it

			node = q.poll();
			i = node.x;
			j = node.y;
			int dist = node.dist;

			if (i == x && j == y) {
				min_dist = dist;
				break;
			}

			for (Direction dir : DIRECTIONS) {
				int nx = i + dir.x;
				int ny = j + dir.y;

				if (isValid(mat, visited, nx, ny)) {
					// mark next cell as visited and enqueue it

					visited[ny][nx] = true;
					int ndist = dist + 1;

					Node newNode = NodeFactory.createNode(nx, ny, ndist, dir,
							node);

					// Node newNode =new Node(node, nx, ny, ndist, dir);

					q.add(newNode);

				}
			}

		}

		if (min_dist == Integer.MAX_VALUE){
			return null;
		}
		return node;
	}
	
	public static Node buildPathNode(Node node){
		Node ret = null;
		Node preRet = null;
		Node cnode = node;
		Node res = null;
		boolean isfist = true;
		while (cnode!=null && cnode.par!=null){
			ret = NodeFactory.createNode(cnode.par.x, cnode.par.y, cnode.par.dist, cnode.dir, null);
			if(isfist){
				res = ret;
				isfist = false;
			}
			//ret.child = cnode;
			if(preRet != null){
				ret.child = preRet;
				preRet.par = ret;
			}
			preRet = ret;
			cnode = cnode.par;
		}
		ret.root = ret;
		ret.par = null;
		Node rootRet = ret;
		Node nnode = ret;
		while (nnode!=null){
			nnode.root = rootRet;
			nnode = nnode.child;
		}
		return res;
	}

	public static Node searchBFSInvalidDes(int mat[][], int i, int j, int x,
			int y) {
		// construct a matrix to keep track of visited cells
		for (int n = 1; n < 30; n++) {
			for (int p = 1; p < 30; p++) {
				visited[n][p] = false;
			}
		}

		// create an empty queue
		Queue<Node> q = queue;
		q.clear();
		// mark source cell as visited and enqueue the source node
		visited[j][i] = true;
		Node root = NodeFactory.createNode(i, j, 0, null, null);
		q.add(root);

		// stores length of longest path from source to destination
		int min_dist = Integer.MAX_VALUE;
		Node node = null;
		// run till queue is not empty
		while (!q.isEmpty()) {
			// pop front node from queue and process it
			node = q.poll();

			// (i, j) represents current cell and dist stores its
			// minimum distance from the source
			i = node.x;
			j = node.y;
			int dist = node.dist;

			// if destination is found, update min_dist and stop
			if (i == x && j == y) {
				min_dist = dist;
				break;
			}

			// check for all 4 possible movements from current cell
			// and enqueue each valid movement
			for (Direction dir : DIRECTIONS) {
				// check if it is possible to go to position
				// (i + row[k], j + col[k]) from current position
				int nx = i + dir.x;
				int ny = j + dir.y;
				if (isValid(mat, visited, nx, ny) || ((nx == x) && ny == y)) {
					// mark next cell as visited and enqueue it
					visited[ny][nx] = true;
					int ndist = dist + 1;
					Node newNode = NodeFactory.createNode(nx, ny, ndist, dir,
							node);
					// Node newNode =new Node(node, nx, ny, ndist, dir);
					q.add(newNode);
				}
			}
		}
		if (min_dist == Integer.MAX_VALUE)
			return null;
		return node;
	}
	
	private static EnumSet<Direction> HOZ_DIRECTIONS = EnumSet
			.of(Direction.LEFT, Direction.RIGHT);
	private static EnumSet<Direction> VET_DIRECTIONS = EnumSet
			.of(Direction.UP, Direction.DOWN);
	public static Node searchBNFLongestPath(Node shortPath, int mat[][]) {
		if(shortPath != null && shortPath.dist <1)
			return shortPath;
		
		
		for (int n = 1; n < 30; n++) {
			for (int p = 1; p < 30; p++) {
				visited[n][p] = false;
			}
		}
		Node node = shortPath;
		while (node!=null) {
			visited[node.y][node.x] = true;
			node = node.par;
		}
		Node cur = shortPath.root;
	
		while(true){

			Node next = cur.child;
			if(next == null)
				break;
			
			EnumSet<Direction> extDir = null;
			//System.out.println("DIRECTIONS " + cur.dir);
			if(HOZ_DIRECTIONS.contains(cur.dir)){
				//System.out.println("HOZ_DIRECTIONS");
				extDir = VET_DIRECTIONS;
			}
			else if(VET_DIRECTIONS.contains(cur.dir)){
				//System.out.println("VET_DIRECTIONS");
				extDir = HOZ_DIRECTIONS;
			}		
			//System.out.println("DIRECTIONS " + extDir);
			boolean ext = false;
			for (Direction dir : extDir) {
				int cx=  cur.x + dir.x;
				int cy=  cur.y + dir.y;
				int nx=  next.x + dir.x;
				int ny=  next.y + dir.y;
			
				if (isValid(mat,visited, cx, cy) && isValid(mat,visited, nx, ny)){
					//System.out.println("cur " + cur.x + " " + cur.y);
					//System.out.println("next " + next.x + " " + next.y);
					
					Node ccNode = NodeFactory.createNode(cx, cy, cur.dist+1, cur.dir, cur);
					Node ncNode = NodeFactory.createNode(nx, ny, cur.dist+2, Direction.invert(dir), ccNode);
					cur.dir = dir;
					ncNode.child = next;
					next.par = ncNode;
					next.dist = cur.dist+3;
					visited[cy][cx] = true;
					visited[ny][nx] = true;
					ext = true;
				}				
			}			
			if(!ext){
				cur = cur.child;
				//System.out.println("cur " + cur.x + " " + cur.y);
			}
		}
		
		return cur;
	}
	

	public static int countActiveSpace(int mat[][], int i, int j) {
		// construct a matrix to keep track of visited cells
		for (int n = 1; n < 30; n++) {
			for (int p = 1; p < 30; p++) {
				visited[n][p] = false;
			}
		}
		int count = 0;
		// create an empty queue
		Queue<Node> q = queue;
		q.clear();
		// mark source cell as visited and enqueue the source node
		visited[j][i] = true;
		Node root = NodeFactory.createNode(i, j, 0, null, null);
		q.add(root);
		Node node = null;
		while (!q.isEmpty()) {
			node = q.poll();
			i = node.x;
			j = node.y;
			int dist = node.dist;
			for (Direction dir : DIRECTIONS) {
				int nx = i + dir.x;
				int ny = j + dir.y;
				if (isValid(mat, visited, nx, ny)) {
					// mark next cell as visited and enqueue it
					visited[ny][nx] = true;
					int ndist = dist + 1;
					Node newNode = NodeFactory.createNode(nx, ny, ndist, dir,
							node);
					count++;
					q.add(newNode);
				}
			}
		}
		
		return count;
	}

	/*
	 * static StringBuilder BUIDER = new StringBuilder();
	 * 
	 * public static String formatStep(Node node) { BUIDER.setLength(0); Node
	 * cNode = node; while (cNode.dist >= 1) { BUIDER.insert(0, node.dir.dir);
	 * cNode = cNode.par; } return BUIDER.toString(); }
	 */

	public static Direction getNodeDir(Node node) {
		//return node.dist == 1 ? node.dir : getNodeDir(node.par);
		return node.root.dir;
	}
	
	public static String getNodeDirStr(Node node) {
		//return node.dist == 1 ? node.dir.dir : getNodeDirStr(node.par);
		return node.root.dir.dir;
	}

	public static boolean isValid(int mat[][], boolean visited[][], int row,
			int col) {
		return (row >= 0) && (row < mat.length) && (col >= 0)
				&& (col < mat[0].length) && mat[col][row] == 1
				&& !visited[col][row];
	}

	public static int getMaxValidLand(int x, int y, int[][] mat) {
		int maxwall = -1;
		for (Direction dir : DIRECTIONS) {
			// check if it is possible to go to position
			// (i + row[k], j + col[k]) from current position
			if (PathUtils.isValidCell(mat, x + dir.x, y + dir.y)) {
				int count = PathUtils.countActiveSpace(mat, x + dir.x, y
						+ dir.y);
				if (maxwall < count) {
					maxwall = count;
				}
			}
		}
		return maxwall;
	}

	static final List<Node> posAdj = new StaticArrayList(4);

	public static List<Node> getValidAdj(int px, int py, int[][] mat) {
		posAdj.clear();
		for (Direction dir : DIRECTIONS) {
			// check if it is possible to go to position
			// (i + row[k], j + col[k]) from current position
			if (PathUtils.isValidCell(mat, px + dir.x, py + dir.y)) {
				Node node = NodeFactory.createNode(px + dir.x, py + dir.y, 0,
						dir, null);
				posAdj.add(node);
			}
		}
		return posAdj;
	}

	public static List<Node> getValidAdj(int px, int py, int[][] mat, int deep) {
		posAdj.clear();

		for (Direction dir : DIRECTIONS) {
			// check if it is possible to go to position
			// (i + row[k], j + col[k]) from current position
			int rowd = dir.x * deep;
			int cold = dir.y * deep;
			if (PathUtils.isValidCell(mat, px + rowd, py + cold)) {
				Node node = NodeFactory.createNode(px + rowd, py + cold, 0,
						dir, null);
				posAdj.add(node);
			}
		}

		return posAdj;
	}

	static boolean checkfood(int x, int y, int[][] mat, int px, int py) {
		int count = 0;
		for (Direction dir : DIRECTIONS) {
			// check if it is possible to go to position
			// (i + row[k], j + col[k]) from current position
			if (PathUtils.isValidCell(mat, x + dir.x, y + dir.y)) {
				count++;
			}
			if (((x + dir.x) == px && (y + dir.y) == py)) {
				count++;
			}
		}
		return count > 1;
	}

	public static Direction findAny(int px, int py, int[][] mat) {
		Direction dcom = null;
		for (Direction dir : DIRECTIONS) {
			// check if it is possible to go to position
			// (i + row[k], j + col[k]) from current position
			if (PathUtils.isValidCell(mat, px + dir.x, py + dir.y)) {
				dcom = dir;
				if (checkfood(px + dir.x, py + dir.y, mat, px, py)) {
					break;
				}
				// return getStrCommand(dcom);
			}
		}

		return dcom;
	}
}

class NodeFactory {
	static Node[] cacheMap = new Node[1000000];
	static private int nodeindex;
	static {
		for (int i = 0; i < cacheMap.length; i++) {
			cacheMap[i] = new Node(null, 0, 0, 0, null);
		}
	}

	static Node createNode(int x, int y, int dist, Direction dir,
			Node par) {
		Node node = cacheMap[nodeindex++];
		node.x = x;
		node.y = y;
		node.dist = dist;
		node.par = par;
		node.dir = dir;
		node.child = null;
		if(par == null){
			node.root = node;
		}
		else{
			node.root = par.root;
			par.child = node;
		}
		return node;
	}

	static void reset() {
		nodeindex = 0;
	}

	static void init() {

	}
}

// queue node used in BFS
class Node {
	// (x, y) represents matrix cell coordinates
	// dist represent its minimum distance from the source
	int x, y, dist;
	Direction dir;
	Node par;
	Node root;
	Node child;
	int food;

	Node(Node par, int x, int y, int dist, Direction dir) {
		this.x = x;
		this.y = y;
		this.dist = dist;
		this.par = par;
		this.dir = dir;
	}
	
	int getRealLength(){
		int len =-1;
		Node node = this;
		while(node != null){
			len++;
			node = node.par;
		}
		return len;
	}
	
	String dump(){
		return " x " + x + 
				" y " + y + 
				" dist " + dist + 
				" dir " + dir + 
				" par " + par + 
				" root " + root + 
				" child " + child + 
				" food " + food ;
	}
};