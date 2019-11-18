package old;
import java.util.Comparator;
import java.util.List;

public class PredictUtils {
	private static int[][] mat = new int[25][25];
	private static int[][] pre_mat = new int[25][25];
	static List<Node> attpos = new StaticArrayList(10000);

	// static List<Node> attpos = new ArrayList<Node>();
	static void updatemap(int x, int y, int val) {
		mat[x][y] = val;
	}

	static void updatemap(int x, int y, int val, boolean isEnemy) {
		mat[x][y] = isEnemy ? 4 : 2;
	}

	private static void rebuildMatrixbyDist(Info info, int [][]mat,
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList player,
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList enemy, int dist) {

		for (int i = 1; i < 25; i++) {
			for (int j = 1; j < 25; j++) {
				mat[i][j] = 1;
				// PredictUtils.updatemap(i, j, 1);
			}
		}
		int plength = player.map.segments.myArrayList.length > dist ? player.map.segments.myArrayList.length
				- dist
				: 1;

		for (int i = 0; i < plength; i++) {
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList.MyArrayListMap.Segments.SegmentsMyArrayList seg = player.map.segments.myArrayList[i];
			// mat[seg.map.y][seg.map.x] = 0;
			mat[seg.map.y][seg.map.x] = 2;
		}

		int elength = enemy.map.segments.myArrayList.length > dist ? enemy.map.segments.myArrayList.length
				- dist
				: 1;
		for (int i = 0; i < elength; i++) {
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList.MyArrayListMap.Segments.SegmentsMyArrayList seg = enemy.map.segments.myArrayList[i];
			// mat[seg.map.y][seg.map.x] = 0;
			mat[seg.map.y][seg.map.x] = 4;
		}
		if(info.map.roomInfo.map.wall !=null && info.map.roomInfo.map.wall.myArrayList != null){
			for (Info.Map.RoomInfo.RoomMap.Wall.MyArrayList wallMap : info.map.roomInfo.map.wall.myArrayList) {
				mat[wallMap.map.y][wallMap.map.x] = 0;
			}
		}

	}

	static Node getAttPoint(Node pe, Info info,
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList player,
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList enemy, int px,
			int py, int ex, int ey, int plength, int elength, int scores, int escores) {
		if (pe != null) {
			// ep = PathUtils.buildPathNode(ep);
			List<Node> attnodes = getAttNodeList(pe, mat);
			// System.out.println("getAttNodeList "+ attnodes.size());
			// System.out.println("attnodes " + attnodes.size());
			for (Node node : attnodes) {
				// int dist = Math.abs(px - node.y) + Math.abs(py - node.x);
				// int edist = Math.abs(ex - node.y) + Math.abs(ey - node.x);

				Node pnode = PathUtils.searchBFS(mat, px, py, node.x, node.y);
				int dist = pnode != null ? pnode.dist : 0;
				Node enode = PathUtils.searchBFS(mat, ex, ey, node.x, node.y);
				int edist = enode != null ? enode.dist : 0;
				/*
				 * System.out.println("attnode" + node.x + "  attnode " + node.y
				 * +" dist" + dist +" edist" + edist);
				 */

				if (edist > dist && dist < elength / 2) {
					rebuildMatrixbyDist(info, pre_mat,player, enemy, dist + 1);
					Node att = pnode;

					if (att != null) {
						Node check = att;
						while (check != null && check.dist > 0) {
							pre_mat[check.y][check.x] = 7;
							check = check.par;
						}

						/* int maxwallaf = getMaxWall(ex, ey); */
						int emaxwall = PathUtils.getMaxValidLand(ex, ey, pre_mat);
						int maxwall = PathUtils.getMaxValidLand(px, py, pre_mat);

						// System.out.println("maxwall " + maxwall);
						// System.out.println("maxwallaf " + maxwallaf);
						// System.out.println("att.dist " + att.dist);

						boolean isKill = (plength > (att.dist + 1))
								&& maxwall > emaxwall && emaxwall < escores;
						// System.out.println("isKill " + isKill);

						/*for (int i = 0; i < mat.length; i++) {
							for (int j = 0; j < mat[i].length; j++) {
								System.out.print(mat[i][j] + " ");
							}
							System.out.println();
						}*/

						/*if (isKill) {
							System.out.println();
							pre_mat[node.y][node.x] = 8;
							pre_mat[py][px] = 6;
							pre_mat[ey][ex] = 5;
							for (int i = 0; i < pre_mat.length; i++) {
								for (int j = 0; j < pre_mat[i].length; j++) {
									System.out.print(pre_mat[i][j] + " ");
								}
								System.out.println();
							}

						}*/

						check = att;
						while (check != null) {
							mat[check.y][check.x] = 1;
							check = check.par;
						}
						if (isKill) {
							return att;
						}
					}
				}
			}
		}
		return null;
	}

	static List<Node> getAttNodeList(Node pe, int[][] mat) {
		attpos.clear();
		int dist = pe.dist;
		Node node = pe;
		while (node != null) {
			if (node.dist > dist / 2) {
				node = node.par;
				continue;
			}
			int x = node.x;
			int y = node.y;
			int yy = y - 1;
			boolean check = false;
			Node newNode = null;
			while (yy > 0 && mat[x][yy] == 1) {

				check = true;
				newNode = NodeFactory.createNode(x, yy,
						node.dist + Math.abs(y - yy), null, null);
				yy = yy - 1;
			}
			if (check) {
				attpos.add(newNode);
			}
			int yyd = y + 1;
			check = false;
			while (yyd < 25 && mat[x][yyd] == 1) {
				check = true;
				newNode = NodeFactory.createNode(x, yyd,
						node.dist + Math.abs(yyd - y), null, null);
				yyd = yyd + 1;
			}
			if (check) {
				attpos.add(newNode);
			}

			int xx = x - 1;
			check = false;
			while (xx > 0 && mat[xx][y] == 1) {
				check = true;
				newNode = NodeFactory.createNode(xx, y,
						node.dist + Math.abs(x - xx), null, null);
				xx = xx - 1;
			}
			if (check) {
				attpos.add(newNode);
			}
			int xxd = x + 1;
			check = false;
			while (xxd < 25 && mat[xxd][y] == 1) {
				check = true;
				newNode = NodeFactory.createNode(xxd, y,
						node.dist + Math.abs(x - xxd), null, null);
				xxd = xxd + 1;
			}
			if (check) {
				attpos.add(newNode);
			}
			node = node.par;
		}
		// Collections.sort(attpos,compareNode);
		return attpos;
	}

	static final Comparator<Node> compareNode = new NodeDistComparer();

	public static class NodeDistComparer implements Comparator<Node> {
		@Override
		public int compare(Node x, Node y) {
			return x.dist - y.dist;
		}
	}

	public static void init() {
	};
}
