package old;
import java.util.List;

public class FinalSpecialMoveUtils {
	// private static int[][] mat = new int[30][30];
	private static int[][] matz = new int[25][25];
	//static int checkpointUps[] = new int[] { 10, 9 };
	//static int checkpointDowns[] = new int[] { 14, 14 };

	static int allCheckpoints[][] = new int[4][2];

	static boolean isInited = false;
	static boolean isDead = false;
	static boolean isDoneSpecial = false;
	//private static boolean[][] allowZone = new boolean[25][25];
	// private static boolean[][] allowZoneUp = new boolean[25][25];
	// private static boolean[][] allowZoneDown = new boolean[25][25];
	//static List<Node> allowZonePos = new StaticArrayList(25);
	static List<SpecialMove> moves = new StaticArrayMoveList(10);
	static SpecialMove zMove = null; 
	static List<SpecialMove> protectMove = new StaticArrayMoveList(5);

	static {
		// checkpointUps
		
	}
	
	static void reset(){
		isDoneSpecial = false;
		countZ79 = 0;
		countLove = 0;
		countWe = 0;
		countZ79_done = 0;
		countLove_done = 0;
		countWe_done = 0;
		countend =0;
		for (SpecialMove move : moves) {
			move.isDone = false;
			   move.isValid = true;
		}
		protectMove.clear();
		protectMove.add(zMove);
	}

	static void updatemap(int x, int y, int val) {
		matz[x][y] = val;
	}

	static void updatemapWall(int x, int y, int val) {
		matz[x][y] = val;
		for (SpecialMove move : moves) {
			move.updateState(y, x);
		}
	}
	
	static int countZ79 = 0;
	static int countLove = 0;
	static int countWe = 0;
	static int countZ79_done = 0;
	static int countLove_done = 0;
	static int countWe_done = 0;
	static int countend = 0;
	
	static void updateProtectZone(SpecialMove move) {
		//matz[x][y] = val;
		//for (SpecialMove move : moves) {
		//	move.updateState(y, x);
		//}
		/*if(move.type.equals("Z")){
	
			protectMove.clear();
			isZDone = !move.isTaken();
		}*/
		countend++;
		
		if(countend>=5){
			isDoneSpecial = true;
		}
		System.out.println("move is taken "+ move.type + "  " + move.isTaken());
		if (move.isTaken()) {
			switch (move.type) {
			case "Z":
				countZ79++;
				break;
			case "W":
				countWe++;
				break;
			case "L":
				countLove++;
				break;
			}
		}
		else{
			switch (move.type) {
			case "Z":
				countZ79_done++;
				break;
			case "W":
				countWe_done++;
				break;
			case "L":
				countLove_done++;
				break;
			}
		}
		if(countZ79_done >=1 || countWe_done>=2 ||countLove_done>=2){
			//protectMove.clear();
			//isDoneSpecial = true;
		}
		
		int sum = countZ79+ countLove + countWe_done;
		System.out.println("sum taken"+ sum);
		if(sum>=2){
		

			if(countZ79>0 && countLove>0){
				protectMove.clear();
				for (SpecialMove smove : moves) {
					if (smove.isValid()) {
						protectMove.add(smove);
					}
				}
			}
			else if(countWe>0 && countLove>0){
				protectMove.clear();
				for (SpecialMove smove : moves) {
					if (smove.isValid()) {
						protectMove.add(smove);
					}
				}
			}
			if(countZ79>0 && countWe>0){
				protectMove.clear();
				for (SpecialMove smove : moves) {
					if (smove.isValid()) {
						protectMove.add(smove);
					}
				}
			}
		}
		System.out.println(" protectMove length" + protectMove.size());
		for (SpecialMove smove : protectMove) {
			System.out.println(" protectMove " + smove.type);
		}
	}
	

	private static void buildZMatrix(Info info, int [][]mat, SpecialMove move) {

		int col = info.map.roomInfo.map.map.map.horizontal;
		int row = info.map.roomInfo.map.map.map.vertical;
		for (int i = 1; i < row; i++) {
			for (int j = 1; j < col; j++) {
				mat[i][j] =( (move.allowZone[j][i]) || (move.blockZone[j][i]))? 0 : 1; 
			}
		}
		for (Info.Map.RoomInfo.RoomMap.Players.MyArrayList player : info.map.roomInfo.map.players.myArrayList) {
			for (Info.Map.RoomInfo.RoomMap.Players.MyArrayList.MyArrayListMap.Segments.SegmentsMyArrayList seg : player.map.segments.myArrayList) {
				mat[seg.map.y][seg.map.x] = 0;
			}
		}
		if(info.map.roomInfo.map.wall !=null && info.map.roomInfo.map.wall.myArrayList != null){
			for (Info.Map.RoomInfo.RoomMap.Wall.MyArrayList wallMap : info.map.roomInfo.map.wall.myArrayList) {
				mat[wallMap.map.y][wallMap.map.x] = 0;
			}
		}
	}

	static void updateInfo(Info info) {
		if (info.map.roomInfo.map.zone != null) {
			if (!isInited && info.map.roomInfo.map.zone.myArrayList != null) {
				//allowZonePos.clear();
				SpecialMoveFactory.reset();
				for (Info.Map.RoomInfo.RoomMap.Zone.MyArrayList list : info.map.roomInfo.map.zone.myArrayList) {

					SpecialMove move = SpecialMoveFactory
							.createMove(list.map.TYPE);
					for (Info.Map.RoomInfo.RoomMap.Zone.MyArrayList.MyArrayListMap.WIN_ZONE.WIN_ZONE_MyArrayList winList : list.map.WIN_ZONE.myArrayList) {

						switch (list.map.TYPE) {
						case "Z":

						
							move.allowZone[winList.map.x][winList.map.y] = true;
							//allowZone[winList.map.x][winList.map.y] = true;
							Node node = new Node(null,winList.map.x, winList.map.y,
									0, null);
							//allowZonePos.add(node);
							move.allowPoints.add(node);
							zMove = move;
							break;
						case "W":
							move.allowZone[winList.map.x][winList.map.y] = true;
							move.allowPoints.add(new Node(null,winList.map.x, winList.map.y,
									0, null));
							break;
						case "L":
							move.allowZone[winList.map.x][winList.map.y] = true;
							move.allowPoints.add(new Node(null,winList.map.x, winList.map.y,
									0, null));
							break;
						default:
							break;
						}
					}

					for (Info.Map.RoomInfo.RoomMap.Zone.MyArrayList.MyArrayListMap.BLOCK_ZONE.BLOCK_ZONE_MyArrayList blockList : list.map.BLOCK_ZONE.myArrayList) {
						// matz[blockList.map.y][blockList.map.x] = 0;
						move.blockZone[blockList.map.x][blockList.map.y] = true;
					}

					moves.add(move);

				}
				protectMove.add(zMove);
				isInited = true;
			}

		}
	}

	static boolean isThereZone(int px, int py) {
		for (SpecialMove move : moves) {
			//System.out.println("move valid " + move.isValid());
			if (move.isValid() && !move.allowZone[px][py]) {
				return true;
			}
		}
		return false;
	}
	
	static boolean isInProtectZone(int px, int py, SpecialMove pt ){
		//boolean check = true;
		if(pt!=null){
			return pt.allowZone[px][py];
		}
		return false;
	}
	
	static boolean isInProtectZoneCp(int x, int y){
		for (SpecialMove move : protectMove) {
			if(move.isInCheckpoint(x, y))
				return true;
		}
		return false;
	}
	
	
	static SpecialMove getNearProtectZoneMove(int ex, int ey, int[][] mat){
		SpecialMove ret = null;
		Node path =null;
		if(protectMove.size()==1){
			return protectMove.get(0);
		}
		for (SpecialMove move : protectMove) {
			for (int[] pos : move.getCheckpoint()) {
					Node p = PathUtils.searchBFS(mat, ex, ey, pos[0], pos[1]);
					if (p != null) {
						if(path == null){
							path=p;
							ret = move;
						}
						else if(p.dist<path.dist){
							path = p;
							ret = move;
						}
					}
			}
		}
		return ret;
	}
	
	
	static SpecialMove getNearProtectZone(int ex, int ey, int[][] mat){
		
		for (SpecialMove move : protectMove) {
			for (int[] pos : move.getCheckpoint()) {
					Node p = PathUtils.searchBFS(mat, ex, ey, pos[0], pos[1]);
					if (p != null && p.dist <=3) {
						return move;
					}
			}
		}
		return null;
	}
	
	static Node  getPlayerNearProtectZone(int px, int py, int[][] mat, SpecialMove move){
		for (int[] pos : move.getCheckpoint()) {
					Node p = PathUtils.searchBFS(mat, px, py, pos[0], pos[1]);
					if (p != null && p.dist <8) {
						return p;
					}
		}

		return null;
	}

	// private static int specialmove = -1;
	private static boolean trigger = false;
	private static boolean isStartSpecialMove = false;
	private static SpecialMove curMove = null;

	static String processSpecialMove(int[][] mat, Info info, 
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList player,
			Info.Map.RoomInfo.RoomMap.Players.MyArrayList enemy, int px,
			int py, int ex, int ey, int score, int length, int elength) {
		//System.out.println(" isZDone " + isZDone);
		//System.out.println(" isWeDone " + isWeDone);
		//System.out.println(" isLoveDone " + isLoveDone);
	
		
		if(!isDoneSpecial){
			if (!isDead && length == 1 && score > 1 && enemy != null
					&& enemy.map.score >= SpecialMoveZ79.LENGTH && elength != 1) {
				isDead = true;
				trigger = false;
				isStartSpecialMove = false;
				curMove = null;
			}
			SpecialMove ptmove = getNearProtectZoneMove(ex,ey,mat);
			if (!isDead &&  !isInProtectZone(px,py,ptmove)
					&& enemy != null
					&& enemy.map.score >= 18
					&& isInProtectZoneCp(ex,ey)) {
				isDead = true;
				trigger = false;
				isStartSpecialMove = false;
				curMove = null;
			}
	
			System.out.println(" protectMove length" + protectMove.size());
			System.out.println(" protectMove isInProtectZone" + isInProtectZone(px,py,ptmove));
			for (SpecialMove smove : protectMove) {
				System.out.println(" protectMove " + smove.type);
				System.out.println(" protectMove " + smove.allowZone[px][py]);
			}
			if (isDead
					&& ((enemy != null && enemy.map.score < SpecialMoveZ79.LENGTH) || isInProtectZone(px,py,ptmove) || !zMove.isValidMove(player))) {
				isDead = false;
			}
		
			

			if(!isDead && !isInProtectZone(px,py,ptmove) && enemy != null
					&& enemy.map.score >= 18){
				if(ptmove != null && ptmove.isAvailable(mat)){
					isDead =  true;
				}
			}
			
			if (isDead) {
				Node path = null;
				if(ptmove!= null){
					for (Node pos : ptmove.allowPoints) {
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
				}
			
				if (path != null) {
					path = PathUtils.buildPathNode(path);
					return path.root.dir.dir;
				}
				else{
					//isDead = false;
				}
			}
			
			
			/*if(!isDead){
				SpecialMove protectMoveZone = getEnemyNearProtectZone(ex, ey, mat);
				Node pnNode =  getPlayerNearProtectZone(px, py, mat, protectMoveZone);
				
				if(protectMoveZone!=null){
					Node path = null;
					for (Node pos : protectMoveZone.allowPoints) {
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
			}*/
			
		
		}

		if (!trigger && score >= 15 && elength > 1 && enemy != null
				&& isThereZone(ex, ey)
				&& (ex <= 3 || ex >= 20 || ey <= 3 || ey >= 20)) {
			/*
			 * for (int i = 1; i < enemy.map.segments.myArrayList.length; i++) {
			 * boolean[][] check = allowZone; if
			 * (check[enemy.map.segments.myArrayList
			 * [i].map.x][enemy.map.segments.myArrayList[i].map.y]) { return
			 * null; } }
			 */
			trigger = true;
		}
		
		if(!isDoneSpecial && score >= 18 && zMove.isAvailable(mat)){
			trigger = true;
		}

		if (elength <= 1 && score >= 15) {
			trigger = true;
		}

		if (score < 15 || (curMove != null && score < curMove.getLength()) ||  (curMove != null && !curMove.canMove(mat)  && curMove.isEnemyThere(enemy))) {
			trigger = false;
			isStartSpecialMove = false;
			curMove = null;
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
		//System.out.println("pathZone moves length  " + moves.size());
		//System.out.println("pathZone isThereZone  " + isThereZone(px, py));
		//System.out.println(" px py   " + px + " " +py);
		//System.out.println(" curMove   " + curMove);
		//System.out.println(" curMove   " + (curMove!=null?curMove.allowZone[px][py] :""));
		if (curMove != null && !curMove.allowZone[px][py] || curMove == null
				&& isThereZone(px, py)) {
			if (isStartSpecialMove && curMove != null
					&& !curMove.allowZone[px][py]) {
				boolean check  = curMove.isReallyDone(mat);
				curMove.isDone = check;
				//if(check){
					//isZDone = isZDone ? isZDone : curMove.type.equals("Z");
					//isWeDone = isWeDone ? isWeDone : curMove.type.equals("W");
					//isLoveDone = isLoveDone ? isLoveDone : curMove.type.equals("L");
					/*for (SpecialMove tmove : moves) {
						if (tmove.type.equals(curMove.type)) {
							tmove.isDone = true;
						}
					}*/
				//}
				/*
				 * for (SpecialMove tmove : moves) {
				 * //if(tmove.type.equals(curMove.type)){ //}
				 * System.out.println(" move type" + tmove.type );
				 * System.out.println(" move type" + tmove.isDone ); }
				 */
				curMove = null;
			}
			isStartSpecialMove = false;
			// Node p = PathUtils.searchBFSInvalidDes(matz, px, py,
			// allCheckpoints[3][0], allCheckpoints[3][1]);
			if(zMove.isValid() && score >= zMove.getLength() && zMove.isAvailable(mat)){
				
				int[][] checkpoints = zMove.getCheckpoint();
				for (int[] checkpoint : checkpoints) {
					if (zMove.canMove(checkpoint[0], checkpoint[1], mat)) {
						Node p = PathUtils.searchBFSInvalidDes(matz, px,
								py, checkpoint[0], checkpoint[1]);
						if (p != null) {
							if (pathZone == null) {
								pathZone = p;
							}
							if (pathZone != null
									&& (pathZone.dist > p.dist)) {
								pathZone = p;
							}
							curMove = zMove;
						}
						
					}
					
				}
			}
			else{
			
				for (SpecialMove move : moves) {
					if (move.isValid() && length >= move.getLength()
							&& move.isValidMove(player, score, 0)) {
						buildZMatrix(info, matz, move);
	
						int[][] checkpoints = move.getCheckpoint();
						for (int[] checkpoint : checkpoints) {
							if (move.canMove(checkpoint[0], checkpoint[1], mat)) {
								Node p = PathUtils.searchBFSInvalidDes(matz, px,
										py, checkpoint[0], checkpoint[1]);
								// System.out.println("p "+ p);
								/*
								 * for (int i = 0; i < mat.length; i++) { for (int j
								 * = 0; j < mat[i].length; j++) {
								 * System.out.print(mat[i][j] + " "); }
								 * System.out.println(); }
								 */
	
								if (p != null) {
									if (pathZone == null) {
										pathZone = p;
										//System.out.println("cp first x"
										//		+ checkpoint[0]);
										//System.out.println("cp first y"
										//		+ checkpoint[1]);
										// specialmove = move.getSpecialMove(px,
										// py);
										curMove = move;
									}
									if (pathZone != null
											&& (pathZone.dist > p.dist)) {
										//System.out.println("cp x" + checkpoint[0]);
										//System.out.println("cp y" + checkpoint[1]);
										pathZone = p;
										// specialmove = move.getSpecialMove(px,
										// py);
										curMove = move;
									}
								}
							}
						}
					}
				}
			}
			// System.out.println("pathZone player  " + pathZone);
			/*
			 * if(p != null){ if(pathZone == null){ pathZone = p; specialmove =
			 * count; } if(pathZone != null && pathZone.dist > p.dist){ pathZone
			 * = p; specialmove = count; } }
			 */
			if (pathZone != null) {
				//System.out.println("pathZone "+ pathZone.dump());
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
			 System.out.println("start move ");
			 
			 
			int move = curMove != null ? curMove.getSpecialMove(px, py) : -1;
			 System.out.println(" move type" + move);
			 System.out.println(" move isStartSpecialMove" + isStartSpecialMove);
			if (move != -1) {
				if (!isStartSpecialMove) {
					isStartSpecialMove = true;
					String movestr = curMove.move(move);
					return movestr;
				}
			}
	
			if (isStartSpecialMove) {
				return "";
			}

		}
		return null;
	}

	static final class SpecialMoveFactory {
		static int mz = 0;
		static int ml = 0;
		static int mw = 0;
		static SpecialMove[] cz = new SpecialMove[3];
		static SpecialMove[] cl = new SpecialMove[3];
		static SpecialMove[] cw = new SpecialMove[3];

		static {
			for (int i = 0; i < cz.length; i++) {
				cz[i] = new SpecialMoveZ79();
				cl[i] = new SpecialMoveLove();
				cw[i] = new SpecialMoveWe();
			}
		}

		static void reset() {
			mz = ml = mw = 0;
		}

		static SpecialMove createMove(String type) {
			SpecialMove move = null;
			switch (type) {
			case "Z":
				move = cz[mz++];
				move.type = type;
				break;
			case "W":
				move = cw[mw++];
				move.type = type;
				break;
			case "L":
				move = cl[ml++];
				move.type = type;
				break;
			default:
				throw new IllegalArgumentException(
						"Wrong move or move to implemented");
			}
			return move;
		}

	}

	static class SpecialMoveZ79 extends SpecialMove {
		static int[][] checkpoint = new int[][] { { 10, 9 },{ 10, 9 }, { 14, 14 } ,{ 14, 14 }};
		static int LENGTH = 18;

		@Override
		String move(int type) {
			String movestr = null;
			// UP(0, -1, "1"), LEFT(-1, 0, "2"), DOWN(0, 1, "3"), RIGHT(1, 0,
			// "4");
			switch (type) {

			case 0:
				//
				movestr = "444432323232344444";
				break;
			case 1:
				//
				movestr = "444432323232344443";
				break;
			case 2:
				movestr = "222214141414122222";
				// move =
				// "3414341434143414332323232323232323341434143414341433";
				break;
			case 3:
				movestr = "222214141414122221";
				// move =
				// "3414341434143414332323232323232323341434143414341433";
				break;
			}
			return movestr;
		}

		@Override
		int[][] getCheckpoint() {
			// TODO Auto-generated method stub
			return checkpoint;
		}

		@Override
		int getLength() {
			// TODO Auto-generated method stub
			return LENGTH;
		}

		@Override
		int onGetSpecialMove(int px, int py) {
			if (checkpoint[0][0] == px && checkpoint[0][1] == py)
				return 0;
			if (checkpoint[1][0] == px && checkpoint[1][1] == py)
				return 1;
			if (checkpoint[2][0] == px && checkpoint[2][1] == py)
				return 2;
			if (checkpoint[2][0] == px && checkpoint[3][1] == py)
				return 13;
			return -1;
		}

		@Override
		protected boolean canMove(int move, int[][] mat) {
			boolean canMove = false;
			switch (move) {

			case 0://{ 10, 9 }
				//
				canMove = PathUtils.isValidCell(mat, 15, 14);
				break;
			case 1://{ 14, 14 }
				canMove = PathUtils.isValidCell(mat, 14, 15);
				// move =
				// "3414341434143414332323232323232323341434143414341433";
				break;
			case 2://{ 14, 14 }
				canMove = PathUtils.isValidCell(mat, 9, 9);
				// move =
				// "3414341434143414332323232323232323341434143414341433";
				break;
			case 3://{ 14, 14 }
				canMove = PathUtils.isValidCell(mat, 10, 8);
				// move =
				// "3414341434143414332323232323232323341434143414341433";
				break;
			}
			return canMove;
		}
	}

	static class SpecialMoveLove extends SpecialMove {
		static int LENGTH = 22;
		static int[][] checkpointUp = new int[][] { { 15, 4 }, { 15, 5 }, {18, 7} };
		static int[][] checkpointDown = new int[][] { { 9, 18 }, { 9, 19 }, { 5, 17 } };
		//static int[][] testpoints = new int[][] { {18, 7 }};

		@Override
		String move(int type) {
			String movestr = null;
			switch (type) {
			// UP(0, -1, "1"), LEFT(-1, 0, "2"), DOWN(0, 1, "3"), RIGHT(1, 0,
			// "4");
			case 0:
				// { 15, 4 },
				movestr = "4143441434323232212122";
				break;
			case 1:
				// { 15, 5 }
				movestr = "4343441414121232212322";
				break;
			case 2:
				// { 9, 18 }
				movestr = "2123221232343434414144";

				break;
			case 3:
				// { 9, 19 }
				movestr = "2323221212141434414344";
				break;
			case 4:
				// { 18, 7 }
				movestr = "2121214143441434323233";
				break;
			case 5:
				// { 5, 17 }
				movestr = "3441434323232212121411";
				break;

			}
			
			return movestr;
		}

		@Override
		int onGetSpecialMove(int px, int py) {
			if (checkpointUp[0][0] == px && checkpointUp[0][1] == py) {
				return 0;
			} else if (checkpointUp[1][0] == px && checkpointUp[1][1] == py) {
				return 1;
			} else if (checkpointDown[0][0] == px && checkpointDown[0][1] == py) {
				return 2;
			}

			else if (checkpointDown[1][0] == px && checkpointDown[1][1] == py) {
				return 3;
			}
			else if (checkpointUp[2][0] == px && checkpointUp[2][1] == py) {
				return 4;
			}
			else if (checkpointDown[2][0] == px && checkpointDown[2][1] == py) {
				return 5;
			}

			return -1;
		}

		@Override
		int[][] getCheckpoint() {
			return allowZone[15][4] ? checkpointUp : checkpointDown;
			//return allowZone[15][4] ?testpoints:new int[][]{};
		}
		
		

		@Override
		int getLength() {
			// TODO Auto-generated method stub
			return LENGTH;
		}

		@Override
		protected boolean canMove(int move, int[][] mat) {
			boolean canMove = false;
			switch (move) {
			// UP(0, -1, "1"), LEFT(-1, 0, "2"), DOWN(0, 1, "3"), RIGHT(1, 0,
			// "4");
			case 0:
				// { 15, 4 },
				//movestr = "4143441434323232212122";
				canMove = PathUtils.isValidCell(mat, 14, 5);
				break;
			case 1:
				// { 15, 5 }
				//movestr = "4343441414121232212322";
				canMove = PathUtils.isValidCell(mat, 14, 4);
				break;
			case 2:
				// { 9, 18 }
				//movestr = "2123221232343434414144";
				canMove = PathUtils.isValidCell(mat, 10, 19);
				break;
			case 3:
				// { 9, 19 }
				//movestr = "2323221212141434414322";
				canMove = PathUtils.isValidCell(mat, 10, 18);
				break;
			case 4:
				// { 18, 7 }
				//movestr = "2121214143441434323233";
				canMove = PathUtils.isValidCell(mat, 19, 8);
				break;
			case 5:
				// { 5, 17 }
				//movestr = "3441434323232212121411";
				canMove = PathUtils.isValidCell(mat, 4, 16);
				break;

			}
			
			return canMove;
		}

	}

	static class SpecialMoveWe extends SpecialMove {
		static int[][] checkpointUp = new int[][] { { 3, 3 }, {3,3}, { 9, 3 }, {9,3} };
		static int[][] checkpointDown = new int[][] { { 15, 18 },{ 15, 18 }, { 21, 18 } , { 21, 18 }};
		static int LENGTH = 15;

		@Override
		String move(int type) {
			String movestr = null;
			switch (type) {
			// UP(0, -1, "1"), LEFT(-1, 0, "2"), DOWN(0, 1, "3"), RIGHT(1, 0,
			// "4");
			//{ 3, 3 }  { 15, 18 }
			case 0: 
				movestr = "333441443441111";	
				break;
			case 1:
				movestr = "333441443441114";	
				break;
				//down
			case 2: 
				movestr = "333441443441111";	
				break;
			case 3:
				//
				movestr = "333441443441114";	
				break;
				//{ 9, 3 } 
			case 4:
				movestr = "333221223221111";
				break;
			case 5:
				movestr = "333221223221112";
				break;
				//down
				//  { 21, 18 }
			case 6:
				movestr = "333221223221111";
				break;
			case 7:
				movestr = "333221223221112";
				// move =
				// "3414341434143414332323232323232323341434143414341433";
				break;
			}
			return movestr;
		}

		@Override
		int[][] getCheckpoint() {
			// TODO Auto-generated method stub
			return allowZone[3][3] ? checkpointUp : checkpointDown;
		}

		@Override
		int getLength() {
			// TODO Auto-generated method stub
			return LENGTH;
		}

		@Override
		int onGetSpecialMove(int px, int py) {
			if (checkpointUp[0][0] == px && checkpointUp[0][1] == py)
				return 0;
			if (checkpointUp[1][0] == px && checkpointUp[1][1] == py)
				return 1;
			if (checkpointUp[2][0] == px && checkpointUp[2][1] == py)
				return 4;
			if (checkpointUp[3][0] == px && checkpointUp[3][1] == py)
				return 5;
			
			
			if (checkpointDown[0][0] == px && checkpointDown[0][1] == py)
				return 2;
			if (checkpointDown[1][0] == px && checkpointDown[1][1] == py)
				return 3;
			if (checkpointDown[2][0] == px && checkpointDown[2][1] == py)
				return 6;
		
			if (checkpointDown[3][0] == px && checkpointDown[3][1] == py)
				return 7;
			return -1;
		}
		
		@Override
		protected boolean canMove(int move, int[][] mat) {
			boolean canMove = false;

			switch (move) {
			// UP(0, -1, "1"), LEFT(-1, 0, "2"), DOWN(0, 1, "3"), RIGHT(1, 0,
			// "4");
			//{ 3, 3 }  
			case 0: 
				canMove = PathUtils.isValidCell(mat, 9, 2);
				break;
			case 1: 
				canMove = PathUtils.isValidCell(mat, 10, 3);
				break;	
			case 2: 
				//{ 15, 18 }
				canMove = PathUtils.isValidCell(mat, 21, 17);
				//canMove = PathUtils.isValidCell(mat, 9, 2);
				break;
				
				
			case 3:
				//{ 15, 18 }
				canMove = PathUtils.isValidCell(mat, 22, 18);
				break;
				//{ 9, 3 }  
			case 4:
				canMove = PathUtils.isValidCell(mat, 3, 2);
				break;
			case 5:
				canMove = PathUtils.isValidCell(mat, 2, 3);
				break;
			case 6://{ 21, 18 }
				canMove = PathUtils.isValidCell(mat, 15, 17);
				break;
			case 7://{ 21, 18 }
				canMove = PathUtils.isValidCell(mat, 14, 18);
				break;
			}
			return canMove;
		}
	}

}

abstract class SpecialMove {
	int curSMove = -1;
	//boolean validCheckpoints[][] = new boolean[25][25];
	boolean[][] allowZone = new boolean[25][25];
	boolean[][] blockZone = new boolean[25][25];
	List<Node> allowPoints = new StaticArrayList(25);
	boolean isValid = true;
	boolean isDone= false;
	String type;
	abstract String move(int type);

	abstract int[][] getCheckpoint();

	abstract int getLength();

	abstract int onGetSpecialMove(int px, int py);
	
	protected abstract boolean canMove(int move, int[][] mat);
	
	int getSpecialMove(int px, int py){
		curSMove = onGetSpecialMove(px, py);
		return curSMove;
	}

	boolean isEnemyThere(Info.Map.RoomInfo.RoomMap.Players.MyArrayList enemy) {
		if (enemy != null) {
			for (int i = 0; i < enemy.map.segments.myArrayList.length; i++) {
				if (allowZone[enemy.map.segments.myArrayList[i].map.x][enemy.map.segments.myArrayList[i].map.y]) {
					return true;
				}
			}
		}
		return false;
	}
	
	boolean isReallyDone(int[][] mat){
		for (Node node : allowPoints) {
			if (PathUtils.isValidCell(mat, node.x, node.y))
				return false;
		}
		return true;
	}
	
	boolean isAvailable(int[][] mat){
		for (Node node : allowPoints) {
			if (!PathUtils.isValidCell(mat, node.x, node.y))
				return false;
		}
		return true;
	}
	
	boolean isInCheckpoint(int x, int y){
		int[][] cp = getCheckpoint();
		for (int[] is : cp) {
			if(is[0]==x && is[1] == y)
				return true;
		}
		return false;
	}

	void updateState(int x, int y) {
		if (isValid && allowZone[x][y]) {
			isValid = false;
			FinalSpecialMoveUtils.updateProtectZone(this);
		}
	}
	
	boolean canMove(int cx, int cy, int [][]mat) {
		int move = onGetSpecialMove(cx, cy);
		return canMove(move, mat);
	}
	
	boolean canMove(int[][] mat){
		return canMove(curSMove, mat);
	}

	boolean isValid() {
		return isValid && !isDone;
	}
	
	boolean isTaken() {
		return !isValid && !isDone;
	}
	

	boolean isValidMove(Info.Map.RoomInfo.RoomMap.Players.MyArrayList player) {


		for (int i = 0; i < player.map.segments.myArrayList.length; i++) {
			boolean[][] check = allowZone;
			if (check[player.map.segments.myArrayList[i].map.x][player.map.segments.myArrayList[i].map.y]) {
				return false;
			}
		}

		return true;
	}
	
	boolean isValidPlayerMove(Info.Map.RoomInfo.RoomMap.Players.MyArrayList player) {


		for (int i = 1; i < player.map.segments.myArrayList.length; i++) {
			boolean[][] check = allowZone;
			if (check[player.map.segments.myArrayList[i].map.x][player.map.segments.myArrayList[i].map.y]) {
				return false;
			}
		}

		return true;
	}


	boolean isValidMove(Info.Map.RoomInfo.RoomMap.Players.MyArrayList player,
			int score, int move) {
		/*
		 * if (score >= 52) { for (int i = 1; i < score && i <
		 * player.map.segments.myArrayList.length; i++) { boolean[][] check =
		 * allowZone; if
		 * (check[player.map.segments.myArrayList[i].map.x][player.
		 * map.segments.myArrayList[i].map.y]) { return false; } } } else { for
		 * (int i = 0; i <= player.map.segments.myArrayList.length / 2; i++) {
		 * boolean[][] check = allowZone; if
		 * (check[player.map.segments.myArrayList
		 * [i].map.x][player.map.segments.myArrayList[i].map.y]) { return false;
		 * } } for (int i = player.map.segments.myArrayList.length - 1; i >
		 * player.map.segments.myArrayList.length / 2; i--) { boolean[][] check
		 * = move == 0 ? allowZoneUp : allowZoneDown; if
		 * (check[player.map.segments
		 * .myArrayList[i].map.x][player.map.segments.myArrayList[i].map.y]) {
		 * return false; } } }
		 */
		if (score < getLength()) {
			return false;
		}

		for (int i = 0; i < score && i < player.map.segments.myArrayList.length; i++) {
			boolean[][] check = allowZone;
			if (check[player.map.segments.myArrayList[i].map.x][player.map.segments.myArrayList[i].map.y]) {
				return false;
			}
			check = blockZone;
			if (check[player.map.segments.myArrayList[i].map.x][player.map.segments.myArrayList[i].map.y]) {
				return false;
			}
		}

		return true;
	}
}
