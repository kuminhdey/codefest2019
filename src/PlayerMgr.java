import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import model.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PlayerMgr {

    public static final int SPACE_STONE = 3;
    public static final int MIND_STONE = 4;
    public static final int REALITY_STONE = 5;
    public static final int POWER_STONE = 6;
    public static final int TIME_STONE = 7;
    public static final int SOUL_STONE = 8;

    public static final String JOIN_GAME = "join game";
    public static final String DRIVE_PLAYER = "drive player";

    public static final String TICKTACK = "ticktack player";

    public static final String DRIVE_ELEPHANT_STATE = "player drive elephant state";

    public static final String HOST = "https://codefest.techover.io";

    public static final String GAME_ID = "72e65a1c-d930-4e17-bb18-656e97391d78";
    public static final String PLAYER_ID = "player1-xxx-xxx-xxx";
    public static String PLAYER_1 = "";
    public static String PLAYER_2 = "";

    public static final String PLAYER_NAME = "SSS";

    public static final boolean ENABLE_PROXY = false;
    public static final int ROUND_Dist = 0;
    public static final int ROUND_LENGTH = 2; // min = 1;
    public static final boolean PREDICT = true;
    public static final int PREDICT_Dist = 100;

    static final String FOOD_NORMAL = "NORMAL";
    static final String FOOD_GOLDEN = "GOLDEN ";
    static final String FOOD_SUPER = "SUPER";
    static Socket socket = null;
    static boolean attmode = false;
    static final String PROXY_HOST = "10.133.93.63";
    static final String PROXY_USR = "tuanna35";
    static final String PROXY_PASS = "";

    static final int GAME_TYPE_1A = 0;
    static final int GAME_TYPE_1B = 1;

    static final int JOIN_GAME_TYPE = GAME_TYPE_1A;

    static ExecutorService executorService = Executors.newFixedThreadPool(1);
    static Gson gson = new Gson();
    static ProcessMapRunnable preProcess = null;
    private static String turnLeft = "1";
    private static String turnRight = "2";
    private static String turnUp = "3";
    private static String turnDown = "4";
    private static Integer box = 2;
    private static String setupBom = "b";
    private static String stepBack = "";
    private static boolean isStartFromTop = false;

    static class ProcessMapRunnable implements Runnable {
        Object param;
        boolean isCancel;

        ProcessMapRunnable(Object param) {
            this.param = param;
        }

        @Override
        public void run() {
            if (!isCancel) {
                Info info = gson.fromJson(gson.toJson(param), Info.class);
            }
        }
    }

    private static boolean isBombSetup = false;
    private static boolean isWaitBomb = false;
    private static int spoilsSize = 0;
    private static Map_____ oldPostion;
    static boolean isWaitEnemyBomb = false;


    public static void main(String[] args) {
        try {

            if (ENABLE_PROXY) {
                OkHttpClient okHttpClient = getHttpClientBuilder().build();
                IO.setDefaultOkHttpCallFactory(okHttpClient);
                IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            }
            socket = IO.socket(HOST);

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("EVENT_CONNECT" + Arrays.toString(args));
                    if (JOIN_GAME_TYPE == GAME_TYPE_1A) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject().put("game_id", GAME_ID).put("player_id", PLAYER_ID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        socket.emit(JOIN_GAME, obj);
                    } else if (JOIN_GAME_TYPE == GAME_TYPE_1B) {
                        socket.emit(JOIN_GAME);
                    }
                }
            }).on(JOIN_GAME, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("JOIN_GAME " + gson.toJson(args[0]));
//                    randomDirection();

                }
            }).on(TICKTACK, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("TICKTACK" + gson.toJson(args[0]));
//					Map map = (Map) args[0];
                    if (preProcess != null)
                        preProcess.isCancel = true;
                    preProcess = new ProcessMapRunnable(args[0]);
                    // long start = System.currentTimeMillis();
                    executorService.execute(preProcess);
                    Info info = gson.fromJson(gson.toJson(args[0]), Info.class);
                    setPlayer(info);
                    info = setEnemyPostion(info);
                    info = setBombPosition(info);
//                    try {
//                        Map_____ currentPostion = getCurrentPostion(info);
//                        Integer typeLeft = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row).myArrayList.get(currentPostion.col - 1);
//                        Integer typeRight = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row).myArrayList.get(currentPostion.col + 1);
//                        Integer typeTop = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row - 1).myArrayList.get(currentPostion.col);
//                        Integer typeBottom = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row + 1).myArrayList.get(currentPostion.col);
//                        for (BoomsMap booms : info.map.getMapInfo().map.bombs.myArrayList) {
//                            if (booms.map.getRow() == currentPostion.row - 1 && !booms.map.getPlayerId().equals(PLAYER_ID)
//                                    || booms.map.getRow() == currentPostion.row + 1 && !booms.map.getPlayerId().equals(PLAYER_ID)
//                                    || booms.map.getCol() == currentPostion.col - 1 && !booms.map.getPlayerId().equals(PLAYER_ID)
//                                    || booms.map.getCol() == currentPostion.col + 1 && !booms.map.getPlayerId().equals(PLAYER_ID)) {
//                                isWaitEnemyBomb = true;
//                                break;
//                            }
//                            if (booms.map.getCol() == currentPostion.col && !booms.map.getPlayerId().equals(PLAYER_ID)) {
//                                if (typeLeft == 0) {
//                                    socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnLeft));
//                                    isWaitEnemyBomb = true;
//                                    break;
//                                } else if (typeRight == 0) {
//                                    socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnRight));
//                                    isWaitEnemyBomb = true;
//                                    break;
//                                }
//                                if (booms.map.getRow() == currentPostion.row && !booms.map.getPlayerId().equals(PLAYER_ID)) {
//                                    if (typeTop == 0) {
//                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnUp));
//                                        isWaitEnemyBomb = true;
//                                        break;
//                                    } else if (typeBottom == 0) {
//                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnDown));
//                                        isWaitEnemyBomb = true;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                    }
//                    if (!PLAYER_ID.equals(info.map.getPlayerId()) && info.map.getTag().equals("bomb:explosed") && isWaitEnemyBomb) {
//                        info.map.setTag("start-game");
//                        isWaitEnemyBomb = false;
//                    }

                    if (PLAYER_ID.equals(info.map.getPlayerId()) && info.map.getTag().equals("bomb:setup")) {
                        isBombSetup = true;
                    } else if (PLAYER_ID.equals(info.map.getPlayerId()) && info.map.getTag().equals("bomb:explosed")) {
                        isBombSetup = false;
                        isWaitBomb = true;
//                        isMoveBack = test(oldPostion, info);
                    }
                    if (PLAYER_ID.equals(info.map.getPlayerId()) && "player:stop-moving".equals(info.map.getTag()) && isWaitEnemyBomb == false || PLAYER_ID.equals(info.map.getPlayerId()) && info.map.getTag().equals("bomb:explosed") && !isWaitEnemyBomb || info.map.getTag().equals("start-game") && !isWaitEnemyBomb) {
                        try {
                            if (info.map.getTag().equals("start-game")) {
                                if (info.map.getMapInfo().map.players.myArrayList.get(1).map.spawnBegin.map.col <= 14) {
                                    isStartFromLeft = true;
                                }
                                if (info.map.getMapInfo().map.players.myArrayList.get(1).map.spawnBegin.map.row <= 9) {
                                    isStartFromTop = true;
                                }
                            }

                            if (info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.col == 1 && info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.row == 1 || info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.col == 1 && info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.row == 16) {
                                isStartFromLeft = true;
                                if (info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.row == 1) {
                                    isStartFromTop = true;
                                }
                                if (info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.row == 16) {
                                    isStartFromTop = false;
                                }
                            } else if (info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.col == 26 && info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.row == 1 || info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.col == 26 && info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.row == 16) {
                                isStartFromLeft = false;
                                if (info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.row == 1) {
                                    isStartFromTop = true;
                                }
                                if (info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map.row == 16) {
                                    isStartFromTop = false;
                                }
                            }
                        } catch (Exception e) {

                        }
                        getEnviroment(getCurrentPostion(info), info);
                    }
                }
//				randomDirection();

            }).

                    on(DRIVE_PLAYER, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            System.out.println("DRIVE" + gson.toJson(args[0]));
                            Info info = gson.fromJson(gson.toJson(args[0]), Info.class);
                            if (PLAYER_ID.equals(info.map.getPlayerId())) {
                                if (String.valueOf(info.map.getDirection().charAt(info.map.getDirection().length() - 1)).equals(turnLeft) || String.valueOf(info.map.getDirection().charAt(info.map.getDirection().length() - 1)).equals(turnRight)) {
                                    moving = movingX;
                                } else if (String.valueOf(info.map.getDirection().charAt(info.map.getDirection().length() - 1)).equals(turnUp) || String.valueOf(info.map.getDirection().charAt(info.map.getDirection().length() - 1)).equals(turnDown)) {
                                    moving = movingY;
                                }
                                if (loop == 4) {
                                    isStartFromTop = !isStartFromTop;
                                    isStartFromLeft = !isStartFromLeft;
                                }
                            }
                        }
                    })/*
             * .on(DRIVE_ELEPHANT_STATE, new Emitter.Listener() {
             *
             * @Override public void call(Object... args) {
             * //System.out.println("DRIVE_ELEPHANT_STATE" + gson.toJson(args)); } })
             */.

                    on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            System.out.println("EVENT_DISCONNECT" + gson.toJson(args));
                            socket.connect();
                            System.out.println("reconnected");

                            socket.emit(JOIN_GAME, GAME_ID, PLAYER_ID);
                            System.out.println("emited JOIN_ROOM");

                        }
                    });

            socket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void moveBack() {
        try {
            socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", stepBack));
            stepBack = "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void randomDirection() {
        // random moving player

//		int i1 = new Random().nextInt(5);
//		int i2 = new Random().nextInt(5);
//		int i3 = new Random().nextInt(5);
//		int i4 = new Random().nextInt(5);
//		int i5 = new Random().nextInt(5);
//		int i6 = new Random().nextInt(5);
//		int i7 = new Random().nextInt(5);
//		int i8 = new Random().nextInt(5);

        try {
            //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
            socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", "b13"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate client builder with proxy settings applied
     *
     * @return
     */
    private static OkHttpClient.Builder getHttpClientBuilder() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().pingInterval(10000, TimeUnit.MILLISECONDS);

        int proxyPort = Integer.parseInt("8080");
        Proxy networkProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, proxyPort));
        // set proxy
        clientBuilder.proxy(networkProxy);

        String proxyUser = PROXY_USR;
        String proxyPassword = PROXY_PASS;

        Authenticator proxyAuthenticator = new Authenticator() {

            @Override
            public Request authenticate(Route arg0, Response response) throws IOException {
                String credential = Credentials.basic(proxyUser, proxyPassword);
                Request authRequest = response.request().newBuilder().header("Proxy-Authorization", credential).build();

                ResponseBody responseBody = response.body();
                if (responseBody != null)
                    responseBody.close();
                return authRequest;
            }
        };
        clientBuilder.proxyAuthenticator(proxyAuthenticator);

        return clientBuilder;
    }


    private static Map_____ getCurrentPostion(Info info) {
        try {
            return info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.currentPosition.map;
        } catch (Exception e) {
            return null;
        }

    }

    static String move = "";

    private static Integer movingY = 1;
    private static Integer movingX = 0;
    static Integer moving = movingX;
    static Integer loopX = 0;
    static Integer loopY = 0;
    static Integer loop = 0;
    static boolean isStartFromLeft = true;

    private static void getEnviroment(Map_____ currentPostion, Info info) {
        try {
            Integer typeLeft = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row).myArrayList.get(currentPostion.col - 1);
            Integer typeRight = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row).myArrayList.get(currentPostion.col + 1);
            Integer typeTop = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row - 1).myArrayList.get(currentPostion.col);
            Integer typeTopLeft = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row - 1).myArrayList.get(currentPostion.col - 1);
            Integer typeBottomLeft = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row + 1).myArrayList.get(currentPostion.col - 1);
            Integer typeTopRight = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row - 1).myArrayList.get(currentPostion.col + 1);
            Integer typeBottomRight = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row + 1).myArrayList.get(currentPostion.col + 1);
            Integer typeBottom = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row + 1).myArrayList.get(currentPostion.col);
            if (!typeRight.equals(box) && !typeTop.equals(box) && !typeBottom.equals(box) && !typeLeft.equals(box) && !isBombSetup) {
                isWaitBomb = true;
                loop++;
            }
            boolean isEatStones = false;
            for (SpoilsList spoilsList : info.map.getMapInfo().map.spoils.myArrayList) {
                if (info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 0 : 1).map.speed >= 100) {
                    if (spoilsList.map.row == currentPostion.row && spoilsList.map.col == currentPostion.col + 1 && !isBombSetup) {
                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnRight));
                        isEatStones = true;
                        break;
                    }
                    if (spoilsList.map.row == currentPostion.row && spoilsList.map.col == currentPostion.col - 1 && !isBombSetup) {
                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnLeft));
                        isEatStones = true;
                        break;
                    }
                    if (spoilsList.map.row == currentPostion.row - 1 && spoilsList.map.col == currentPostion.col && !isBombSetup) {
                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnUp));
                        isEatStones = true;
                        break;
                    }
                    if (spoilsList.map.row == currentPostion.row + 1 && spoilsList.map.col == currentPostion.col && !isBombSetup) {
                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnDown));
                        isEatStones = true;
                        break;
                    }
                } else {
                    if (spoilsList.map.row == currentPostion.row && spoilsList.map.col == currentPostion.col + 1 && !isBombSetup && spoilsList.map.spoilType != MIND_STONE) {
                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnRight));
                        isEatStones = true;
                        break;
                    }
                    if (spoilsList.map.row == currentPostion.row && spoilsList.map.col == currentPostion.col - 1 && !isBombSetup && spoilsList.map.spoilType != MIND_STONE) {
                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnLeft));
                        isEatStones = true;
                        break;
                    }
                    if (spoilsList.map.row == currentPostion.row - 1 && spoilsList.map.col == currentPostion.col && !isBombSetup && spoilsList.map.spoilType != MIND_STONE) {
                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnUp));
                        isEatStones = true;
                        break;
                    }
                    if (spoilsList.map.row == currentPostion.row + 1 && spoilsList.map.col == currentPostion.col && !isBombSetup && spoilsList.map.spoilType != MIND_STONE) {
                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnDown));
                        isEatStones = true;
                        break;
                    }
                }

            }
            if (!isEatStones) {
                Random random = new Random();
                int n = random.nextInt(3);
//            if (isBombSetup) {
//                if (moving.equals(movingX)) {
//                    if (typeTopLeft != 0 && typeBottomLeft != 0 || typeTopRight != 0 && typeBottomRight != 0) {
//                        moveBack();
//                    }
//                }
//                if (moving.equals(movingX)) {
//                    if (typeTopLeft != 0 && typeTopRight != 0 || typeBottomLeft != 0 && typeBottomRight != 0) {
//                        moveBack();
//                    }
//                }
//            }
                if (moving.equals(movingY)) {
//                if(loopX >= 3){
//                    isStartFromLeft = !isStartFromLeft;
//                    loopX = 0;
//                }
                    if (isStartFromLeft ? n != 0 : n == 0) {
                        if (typeRight == 0) {
                            try {
                                //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                if (typeLeft.equals(box) && !isBombSetup || typeTop.equals(box) && !isBombSetup || typeBottom.equals(box) && !isBombSetup) {
                                    if (typeBottomRight == 0 || typeTopRight == 0) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", setupBom + turnRight));
                                        oldPostion = currentPostion;
                                        stepBack = turnLeft;
                                        isBombSetup = true;
                                        isWaitBomb = true;
                                    } else {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnRight));
                                        loopX++;
                                    }
                                } else {
                                    if (isWaitBomb) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnRight));
                                        loopX++;
                                        if (isBombSetup) {
                                            stepBack = stepBack + turnLeft;
                                        }
                                        isWaitBomb = false;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if (typeLeft == 0) {
                            try {
                                //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                if (typeRight.equals(box) && !isBombSetup || typeTop.equals(box) && !isBombSetup || typeBottom.equals(box) && !isBombSetup) {
                                    if (typeTopLeft == 0 || typeBottomLeft == 0) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", setupBom + turnLeft));
                                        oldPostion = currentPostion;
                                        stepBack = turnLeft;
                                        isBombSetup = true;
                                        isWaitBomb = true;
                                    } else {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnLeft));
                                        loopX++;
                                    }
                                } else {
                                    if (isWaitBomb) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnLeft));
                                        loopX++;
                                        if (isBombSetup) {
                                            stepBack = stepBack + turnRight;
                                        }
                                        isWaitBomb = false;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (!isBombSetup) {
                                moving = movingX;
                                getEnviroment(currentPostion, info);
                            }
                        }
                    }
                    else {
                        if (typeLeft == 0) {
                            try {
                                //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                if (typeRight.equals(box) && !isBombSetup || typeTop.equals(box) && !isBombSetup || typeBottom.equals(box) && !isBombSetup) {
                                    if (typeTopLeft == 0 || typeBottomLeft == 0) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", setupBom + turnLeft));
                                        oldPostion = currentPostion;
                                        stepBack = turnLeft;
                                        isBombSetup = true;
                                        isWaitBomb = true;
                                    } else {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnLeft));
                                        loopX++;
                                    }
                                } else {
                                    if (isWaitBomb) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnLeft));
                                        loopX++;
                                        if (isBombSetup) {
                                            stepBack = stepBack + turnRight;
                                        }
                                        isWaitBomb = false;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (typeRight == 0) {
                            try {
                                //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                if (typeLeft.equals(box) && !isBombSetup || typeTop.equals(box) && !isBombSetup || typeBottom.equals(box) && !isBombSetup) {
                                    if (typeBottomRight == 0 || typeTopRight == 0) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", setupBom + turnRight));
                                        oldPostion = currentPostion;
                                        stepBack = turnLeft;
                                        isBombSetup = true;
                                        isWaitBomb = true;
                                    } else {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnRight));
                                        loopX++;
                                    }
                                } else {
                                    if (isWaitBomb) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnRight));
                                        loopX++;
                                        if (isBombSetup) {
                                            stepBack = stepBack + turnLeft;
                                        }
                                        isWaitBomb = false;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            if (!isBombSetup) {
                                moving = movingX;
                                getEnviroment(currentPostion, info);
                            }
                        }
                    }

                } else if (moving.equals(movingX)) {
                    if (isStartFromTop ? n != 0 : n == 0) {
                        if (typeBottom == 0) {
                            try {
                                //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                if (typeRight.equals(box) && !isBombSetup || typeTop.equals(box) && !isBombSetup || typeLeft.equals(box) && !isBombSetup) {
                                    if (typeBottomRight == 0 || typeBottomLeft == 0) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", setupBom + turnDown));
                                        oldPostion = currentPostion;
                                        stepBack = turnLeft;
                                        isBombSetup = true;
                                        isWaitBomb = true;
                                    } else {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnDown));
                                    }
                                } else {
                                    if (isWaitBomb) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnDown));
                                        if (isBombSetup) {
                                            stepBack = stepBack + turnUp;
                                        }
                                        isWaitBomb = false;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (typeTop == 0) {
                            try {
                                //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                if (typeRight.equals(box) && !isBombSetup || typeLeft.equals(box) && !isBombSetup || typeBottom.equals(box) && !isBombSetup) {
                                    if (typeTopRight == 0 || typeTopLeft == 0) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", setupBom + turnUp));
                                        oldPostion = currentPostion;
                                        stepBack = turnLeft;
                                        isBombSetup = true;
                                        isWaitBomb = true;
                                    } else {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnUp));
                                    }
                                } else {
                                    if (isWaitBomb) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnUp));
                                        if (isBombSetup) {
                                            stepBack = stepBack + turnDown;
                                        }
                                        isWaitBomb = false;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (!isBombSetup) {
                                moving = movingY;
                                getEnviroment(currentPostion, info);
                            }
                        }
                    } else {
                        if (typeTop == 0) {
                            try {
                                //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                if (typeRight.equals(box) && !isBombSetup || typeLeft.equals(box) && !isBombSetup || typeBottom.equals(box) && !isBombSetup) {
                                    if (typeTopRight == 0 || typeTopLeft == 0) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", setupBom + turnUp));
                                        oldPostion = currentPostion;
                                        stepBack = turnLeft;
                                        isBombSetup = true;
                                        isWaitBomb = true;
                                    } else {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnUp));
                                    }
                                } else {
                                    if (isWaitBomb) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnUp));
                                        if (isBombSetup) {
                                            stepBack = stepBack + turnDown;
                                        }
                                        isWaitBomb = false;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (typeBottom == 0) {
                            try {
                                //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                if (typeRight.equals(box) && !isBombSetup || typeTop.equals(box) && !isBombSetup || typeLeft.equals(box) && !isBombSetup) {
                                    if (typeBottomRight == 0 || typeBottomLeft == 0) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", setupBom + turnDown));
                                        oldPostion = currentPostion;
                                        stepBack = turnLeft;
                                        isBombSetup = true;
                                        isWaitBomb = true;
                                    } else {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnDown));
                                    }
                                } else {
                                    if (isWaitBomb) {
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnDown));
                                        if (isBombSetup) {
                                            stepBack = stepBack + turnUp;
                                        }
                                        isWaitBomb = false;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (!isBombSetup) {
                                moving = movingY;
                                getEnviroment(currentPostion, info);
                            }
                        }
                    }
                }
            }
        } catch (
                Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static Info setEnemyPostion(Info info) {
        try {
            info.map.getMapInfo().map.map.myArrayList.get(info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 1 : 0).map.currentPosition.map.row).myArrayList.set(info.map.getMapInfo().map.players.myArrayList.get(PLAYER_ID.contains(PLAYER_1) ? 1 : 0).map.currentPosition.map.col, box);
            System.out.println("TICKTACK_Enemy" + gson.toJson(info));
        } catch (Exception e) {
            return info;
        }
        return info;
    }

    private static Info setBombPosition(Info info) {
        try {
            for (BoomsMap booms : info.map.getMapInfo().map.bombs.myArrayList) {
                int size = info.map.getMapInfo().map.players.myArrayList.get(booms.map.getPlayerId().equals(PLAYER_1) ? 0 : 1).map.power;
                for (int i = 0; i <= size; i++) {
                    try {
                        if (info.map.getMapInfo().map.map.myArrayList.get(booms.map.getRow()).myArrayList.get(booms.map.getCol() + i) == 0) {
                            info.map.getMapInfo().map.map.myArrayList.get(booms.map.getRow()).myArrayList.set(booms.map.getCol() + i, 1);
                        }
                    } catch (Exception e) {

                    }
                    try {
                        if (info.map.getMapInfo().map.map.myArrayList.get(booms.map.getRow()).myArrayList.get(booms.map.getCol() - i) == 0) {
                            info.map.getMapInfo().map.map.myArrayList.get(booms.map.getRow()).myArrayList.set(booms.map.getCol() - i, 1);
                        }
                    } catch (Exception e) {

                    }
                    try {
                        if (info.map.getMapInfo().map.map.myArrayList.get(booms.map.getRow() + i).myArrayList.get(booms.map.getCol()) == 0) {
                            info.map.getMapInfo().map.map.myArrayList.get(booms.map.getRow() + i).myArrayList.set(booms.map.getCol(), 1);
                        }
                    } catch (Exception e) {

                    }
                    try {
                        if (info.map.getMapInfo().map.map.myArrayList.get(booms.map.getRow() - i).myArrayList.get(booms.map.getCol()) == 0) {
                            info.map.getMapInfo().map.map.myArrayList.get(booms.map.getRow() - i).myArrayList.set(booms.map.getCol(), 1);
                        }
                    } catch (Exception e) {

                    }
                }
            }
            Gson gson = new Gson();
            System.out.println("TICKTACK_Bomb" + gson.toJson(info));
            return info;
        } catch (Exception e) {
            return info;
        }
    }

    private static void setPlayer(Info info) {
        try {
            PLAYER_1 = info.map.getMapInfo().map.players.myArrayList.get(0).map.id;
            PLAYER_2 = info.map.getMapInfo().map.players.myArrayList.get(1).map.id;
        } catch (Exception e) {

        }
    }

    private static void getEnviroment1(Map_____ currentPostion, Info info) {
        try {
            Integer typeLeft = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row).myArrayList.get(currentPostion.col - 1);
            Integer typeRight = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row).myArrayList.get(currentPostion.col + 1);
            Integer typeTop = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row - 1).myArrayList.get(currentPostion.col);
            Integer typeTopLeft = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row - 1).myArrayList.get(currentPostion.col - 1);
            Integer typeBottomLeft = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row + 1).myArrayList.get(currentPostion.col - 1);
            Integer typeTopRight = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row - 1).myArrayList.get(currentPostion.col + 1);
            Integer typeBottomRight = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row + 1).myArrayList.get(currentPostion.col + 1);
            Integer typeBottom = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row + 1).myArrayList.get(currentPostion.col);
            Random rand = new Random();
            int n = rand.nextInt(3);
            switch (n) {
                case 0:
                    if (typeLeft == 0) {
                        move = move + turnLeft;
                        int n1 = rand.nextInt(1);
                        switch (n1) {
                            case 0:
                                if (typeTopLeft == 0) {
                                    move = move + turnUp;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));
                                        move = "";
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (typeBottomLeft == 0) {
                                    move = move + turnDown;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));
                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            case 1:
                                if (typeBottomLeft == 0) {
                                    move = move + turnDown;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (typeTopLeft == 0) {
                                    move = move + turnUp;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                        }
                    } else {
                        getEnviroment1(currentPostion, info);
                    }
                case 1:
                    if (typeRight == 0) {
                        move = move + turnRight;
                        int n1 = rand.nextInt(1);
                        switch (n1) {
                            case 0:
                                if (typeTopRight == 0) {
                                    move = move + turnUp;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));
                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (typeBottomRight == 0) {
                                    move = move + turnDown;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            case 1:
                                if (typeBottomRight == 0) {
                                    move = move + turnDown;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (typeTopRight == 0) {
                                    move = move + turnUp;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                        }
                    } else {
                        getEnviroment1(currentPostion, info);
                    }
                case 2:
                    if (typeBottom == 0) {
                        move = move + turnDown;
                        int n1 = rand.nextInt(1);
                        switch (n1) {
                            case 0:
                                if (typeBottomLeft == 0) {
                                    move = move + turnLeft;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (typeBottomRight == 0) {
                                    move = move + turnRight;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            case 1:
                                if (typeBottomRight == 0) {
                                    move = move + turnRight;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (typeBottomLeft == 0) {
                                    move = move + turnLeft;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                        }
                    } else {
                        getEnviroment1(currentPostion, info);
                    }
                case 3:
                    if (typeTop == 0) {
                        move = move + turnUp;
                        int n1 = rand.nextInt(1);
                        switch (n1) {
                            case 0:
                                if (typeTopLeft == 0) {
                                    move = move + turnLeft;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (typeTopRight == 0) {
                                    move = move + turnRight;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            case 1:
                                if (typeTopRight == 0) {
                                    move = move + turnRight;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (typeTopLeft == 0) {
                                    move = move + turnLeft;
                                    try {
                                        //socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", String.format("%d%d%d%d%d%d%d%d", i1,i2,i3,i4,i5,i6,i7,i8)));
                                        socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", move.toString()));

                                        move = "";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                        }
                    } else {
                        getEnviroment1(currentPostion, info);
                    }
            }

        } catch (Exception e) {

        }
    }

    private static boolean test(Map_____ currentPostion, Info info) {
        try {
            Integer typeLeft = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row).myArrayList.get(currentPostion.col - 1);
            Integer typeRight = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row).myArrayList.get(currentPostion.col + 1);
            Integer typeTop = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row - 1).myArrayList.get(currentPostion.col);
            Integer typeTopLeft = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row - 1).myArrayList.get(currentPostion.col - 1);
            Integer typeBottomLeft = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row + 1).myArrayList.get(currentPostion.col - 1);
            Integer typeTopRight = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row - 1).myArrayList.get(currentPostion.col + 1);
            Integer typeBottomRight = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row + 1).myArrayList.get(currentPostion.col + 1);
            Integer typeBottom = info.map.getMapInfo().map.map.myArrayList.get(currentPostion.row + 1).myArrayList.get(currentPostion.col);


            boolean isEatStones = false;
            for (SpoilsList spoilsList : info.map.getMapInfo().map.spoils.myArrayList) {
                if (spoilsList.map.row == currentPostion.row && spoilsList.map.col == currentPostion.col + 1 && !isBombSetup) {
                    socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnRight));
                    isEatStones = true;
                    break;
                }
                if (spoilsList.map.row == currentPostion.row && spoilsList.map.col == currentPostion.col - 1 && !isBombSetup) {
                    socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnLeft));
                    isEatStones = true;
                    break;
                }
                if (spoilsList.map.row == currentPostion.row - 1 && spoilsList.map.col == currentPostion.col && !isBombSetup) {
                    socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnUp));
                    isEatStones = true;
                    break;
                }
                if (spoilsList.map.row == currentPostion.row + 1 && spoilsList.map.col == currentPostion.col && !isBombSetup) {
                    socket.emit(DRIVE_PLAYER, new JSONObject().put("direction", turnDown));
                    isEatStones = true;
                    break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
