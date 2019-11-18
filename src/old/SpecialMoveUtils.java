package old;
import java.util.List;

public class SpecialMoveUtils {
	// private static int[][] mat = new int[30][30];
	private static int[][] matz = new int[25][25];
	static int checkpointUps[] = new int[2];
	static int checkpointDowns[] = new int[2];

	static int allCheckpoints[][] = new int[4][2];
	static boolean validCheckpoints[][] = new boolean[25][25];
	static boolean isInited = false;
	static boolean isDead = false;
	private static boolean[][] allowZone = new boolean[25][25];
	private static boolean[][] allowZoneUp = new boolean[25][25];
	private static boolean[][] allowZoneDown = new boolean[25][25];
	static List<Node> allowZonePos = new StaticArrayList(100);

	/*
	 * static {
	 * 
	 * for (int i = 0; i < mat.length; i++) { for (int j = 0; j < mat[i].length;
	 * j++) { // mat[i][j] = 0; } }
	 * 
	 * }
	 */

	static void updatemap(int x, int y, int val) {
		matz[x][y] = val;
	}

	static void updateInfo(InfoOld info) {
		if (info.map.roomInfo.map.zone != null) {

			if (!isInited && info.map.roomInfo.map.zone.map != null && info.map.roomInfo.map.zone.map.check_point != null) {
				for (InfoOld.Map.RoomInfo.RoomMap.Zone.ZoneMap.Check_point.MyArrayList checkpoint : info.map.roomInfo.map.zone.map.check_point.myArrayList) {
					if (checkpoint.map.y < 11) {
						checkpointUps[0] = checkpoint.map.x;
						checkpointUps[1] = checkpoint.map.y;
						allCheckpoints[0] = checkpointUps;
						// allCheckpoints[1][0] = checkpoint.map.x - 8;
						// allCheckpoints[1][1] = checkpoint.map.y;

					}
					if (checkpoint.map.y > 11) {
						checkpointDowns[0] = checkpoint.map.x;
						checkpointDowns[1] = checkpoint.map.y;
						allCheckpoints[1] = checkpointDowns;
						// allCheckpoints[3][0] = checkpoint.map.x - 8;
						// allCheckpoints[3][1] = checkpoint.map.y;

					}
					validCheckpoints[checkpoint.map.x][checkpoint.map.y] = true;
				}
				isInited = true;
			}

			if (info.map.roomInfo.map.zone != null && info.map.roomInfo.map.zone.map != null) {
				allowZonePos.clear();
				for (InfoOld.Map.RoomInfo.RoomMap.Zone.ZoneMap.Allowed_place.MyArrayList allpoint : info.map.roomInfo.map.zone.map.allowed_place.myArrayList) {
					allowZone[allpoint.map.x][allpoint.map.y] = true;
					
					if (allpoint.map.y <= 11) {
						allowZoneUp[allpoint.map.x][allpoint.map.y] = true;
					} else {
						allowZoneDown[allpoint.map.x][allpoint.map.y] = true;
					}
					 
					allowZonePos.add(NodeFactory.createNode(allpoint.map.x,
							allpoint.map.y, 0, null, null));
					matz[allpoint.map.y][allpoint.map.x] = 0;
					// mat[allpoint.map.y][allpoint.map.x] = 1;
				}
			}
			/*
			 * if (info.map.roomInfo.map.wall != null &&
			 * info.map.roomInfo.map.wall.myArrayList != null) { for
			 * (Info.Map.RoInfo.RoomMap.Wall.MyArrayList wallMap :
			 * info.map.roomInfo.map.wall.myArrayList) {
			 * matz[wallMap.map.y][wallMap.map.x] = 0; } }
			 */

		}
	}

	private static int specialmove = -1;
	private static boolean trigger = false;
	private static boolean isStartSpecialMove = false;

	static String processZ(int[][] mat,
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList player,
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList enemy, int px,
			int py, int ex, int ey, int score, int length, int elength) {
		if (length == 1 && score > 1 && enemy != null && enemy.map.score >= 49 && elength != 1) {
			isDead = true;
		}

		if (!allowZone[px][py]
				&& enemy != null
				&& enemy.map.score >= 49
				&& ((ex == checkpointUps[0] && ey == checkpointUps[1]) || (ex == checkpointDowns[0])
						&& ey == checkpointDowns[1])) {
			isDead = true;
		}

		if (isDead && enemy != null && enemy.map.score >= 50 && allowZone[px][py]) {
			isDead = false;
		}

		if (isDead) {
			Node path = null;
			for (Node pos : allowZonePos) {
				if (PathUtils.isValidCell(mat, pos.x, pos.y)) {
					Node p = PathUtils.searchBFS(mat, px, py, pos.x, pos.y);
					if (p != null) {
						if (path == null) {
							path = p;
						} else if (path.dist > p.dist) {
							path = p;
						}
					}

				}
			}
			if (path != null) {
				path = PathUtils.buildPathNode(path);
				return path.root.dir.dir;
			}
		}
		if (!trigger && score >= 52 && elength > 1
				&& enemy != null && !allowZone[ex][ey]
				&& (ex <= 3 || ex >= 20 || ey <= 3 || ey >= 20)) {
			for (int i = 0; i < enemy.map.segments.myArrayList.length; i++) {
				boolean[][] check = allowZone;
				if (check[enemy.map.segments.myArrayList[i].map.x][enemy.map.segments.myArrayList[i].map.y]) {
					return null;
				}
			}
			trigger = true;
		}

		if (elength <= 1 && score >= 52) {
			trigger = true;
		}

		if ((score < 52) || allowZone[ex][ey]
				|| isEnemyThere(enemy)) {
			trigger = false;
			isStartSpecialMove = false;
			return null;
		}
		/*
		 * if(allowZone[px][py] && !isenter){ isenter = false; return null; }
		 */

		if (!trigger) {
			return null;
		}
		Node pathZone = null;
		// System.out.println("allowZone player  " + allowZone[px][py]);
		if (!allowZone[px][py]) {
			isStartSpecialMove = false;
			int count = 0;
			// Node p = PathUtils.searchBFSInvalidDes(matz, px, py,
			// allCheckpoints[3][0], allCheckpoints[3][1]);
			for (int[] checkpoint : allCheckpoints) {
				Node p = PathUtils.searchBFSInvalidDes(matz, px, py,
						checkpoint[0], checkpoint[1]);
				// System.out.println("p "+ p);
				/*
				 * for (int i = 0; i < mat.length; i++) { for (int j = 0; j <
				 * mat[i].length; j++) { System.out.print(mat[i][j] + " "); }
				 * System.out.println(); }
				 */

				if (p != null && isValidMove(player, score, count)) {
					if (pathZone == null) {
						pathZone = p;
						specialmove = count;
					}
					if (pathZone != null && pathZone.dist > p.dist) {
						pathZone = p;
						specialmove = count;
					}
				}
				count++;
			}
			/*
			 * if(p != null){ if(pathZone == null){ pathZone = p; specialmove =
			 * count; } if(pathZone != null && pathZone.dist > p.dist){ pathZone
			 * = p; specialmove = count; } }
			 */
			if (pathZone != null) {

				pathZone = PathUtils.buildPathNode(pathZone);

				/*
				 * System.out.println(); for (int i = 0; i < matz.length; i++) {
				 * for (int j = 0; j < matz[i].length; j++) {
				 * System.out.print(matz[i][j] + " "); } System.out.println(); }
				 */
				return pathZone.root.dir.dir;
			}
		} else {

			// UP(0, -1, "1"), LEFT(-1, 0, "2"), DOWN(0, 1, "3"), RIGHT(1, 0,
			// "4");
			// System.out.println("start move ");
			int move = getSpecialMove(px,py);
			if (move!=-1 && isValidMove(player, score, move)) {
				if (!isStartSpecialMove) {
					isStartSpecialMove = true;
					String movestr = "";
					switch (specialmove) {

					case 0:
						//
						movestr = "2222222234444444432323232323232323341434143414341433";
						break;
					case 1:
						movestr = "1232123212321232114141414141414141222222221444444441";
						// move =
						// "3414341434143414332323232323232323341434143414341433";
						break;
					/*
					 * case 2: move =
					 * "1232123212321232114141414141414141222222221444444441";
					 * break; case 3: move =
					 * "4444444412222222214141414141414141222222221444444441";
					 * break;
					 */
					default:
						break;
					}
					// System.out.println("move " + move);

					return movestr;
				}
			}
			if (isStartSpecialMove) {
				return "";
			}

		}
		return null;
	}
	
	static int getSpecialMove(int px, int py){
		if(checkpointUps[0] == px && checkpointUps[1]  == py)
			return 0;
		if(checkpointDowns[0] == px && checkpointDowns[1]  == py)
			return 1;
		return -1;
	}

	static boolean isEnemyThere(
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList enemy) {
		if (enemy != null) {
			for (int i = 0; i < enemy.map.segments.myArrayList.length; i++) {
				if (allowZone[enemy.map.segments.myArrayList[i].map.x][enemy.map.segments.myArrayList[i].map.y]) {
					return true;
				}
			}
		}
		return false;
	}

	static boolean isValidMove(
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList player, int score,
			int move) {
		if (score >= 52) {
			for (int i = 1; i < score
					&& i < player.map.segments.myArrayList.length; i++) {
				boolean[][] check = allowZone;
				if (check[player.map.segments.myArrayList[i].map.x][player.map.segments.myArrayList[i].map.y]) {
					return false;
				}
			}
		} else {
			for (int i = 0; i <= player.map.segments.myArrayList.length / 2; i++) {
				boolean[][] check = allowZone;
				if (check[player.map.segments.myArrayList[i].map.x][player.map.segments.myArrayList[i].map.y]) {
					return false;
				}
			}
			for (int i = player.map.segments.myArrayList.length - 1; i > player.map.segments.myArrayList.length / 2; i--) {
				boolean[][] check = move == 0 ? allowZoneUp : allowZoneDown;
				if (check[player.map.segments.myArrayList[i].map.x][player.map.segments.myArrayList[i].map.y]) {
					return false;
				}
			}
		}
		return true;
	}

}
