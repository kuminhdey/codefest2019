package old;
public final class Info {

    public final Map map;

    public Info(Map map) {
        this.map = map;
    }

    public static final class Map {
        public final int code;
        public final String status;
        public final RoomInfo roomInfo;

        public Map(int code, String status, RoomInfo roomInfo) {
            this.code = code;
            this.status = status;
            this.roomInfo = roomInfo;
        }

        public static final class RoomInfo {

            public final RoomMap map;

            public RoomInfo(RoomMap map) {
                this.map = map;
            }

            public static final class RoomMap {

                public final int round_status;
                public final int round_time;
                public final Foods foods;
                public final Zone zone;
                public final Players players;
                public final RoomMapMap map;
                public final Wall wall;
                public final int speed;

                public RoomMap(int round_status, int round_time, Foods foods, Zone zone,
                               Players players, RoomMapMap map, Wall wall, int speed) {
                    this.round_status = round_status;
                    this.round_time = round_time;
                    this.foods = foods;
                    this.zone = zone;
                    this.players = players;
                    this.map = map;
                    this.wall = wall;
                    this.speed = speed;
                }

                public static final class Foods {

                    public final MyArrayList myArrayList[];

                    public Foods(MyArrayList[] myArrayList) {
                        this.myArrayList = myArrayList;
                    }

                    public static final class MyArrayList {

                        public final MyArrayListMap map;

                        public MyArrayList(MyArrayListMap map) {
                            this.map = map;
                        }

                        public static final class MyArrayListMap {

                            public final Coordinate coordinate;
                            public final String id;
                            public final String type;

                            public MyArrayListMap(Coordinate coordinate,
                                                  String id, String type) {
                                this.coordinate = coordinate;
                                this.id = id;
                                this.type = type;
                            }

                            public static final class Coordinate {

                                public final CoordinateMap map;

                                public Coordinate(CoordinateMap map) {
                                    this.map = map;
                                }

                                public static final class CoordinateMap {
                                    public final int x;
                                    public final int y;

                                    public CoordinateMap(int x, int y) {
                                        this.x = x;
                                        this.y = y;
                                    }

                                }

                            }

                        }

                    }

                }

                public static final class Zone {

                    public final MyArrayList[] myArrayList;

                    public Zone(MyArrayList[] myArrayList) {
                        this.myArrayList = myArrayList;
                    }

                    public static final class MyArrayList {

                        public final MyArrayListMap map;

                        public MyArrayList(MyArrayListMap map) {
                            this.map = map;
                        }

                        public static final class MyArrayListMap {

                            public final BLOCK_ZONE BLOCK_ZONE;

                            public final WIN_ZONE WIN_ZONE;

                            public final String TYPE;

                            public MyArrayListMap(MyArrayListMap.BLOCK_ZONE BLOCK_ZONE, MyArrayListMap.WIN_ZONE WIN_ZONE, String TYPE) {
                                this.BLOCK_ZONE = BLOCK_ZONE;
                                this.WIN_ZONE = WIN_ZONE;
                                this.TYPE = TYPE;
                            }

                            public static final class BLOCK_ZONE {

                                public final BLOCK_ZONE_MyArrayList[] myArrayList;

                                public BLOCK_ZONE(BLOCK_ZONE_MyArrayList[] myArrayList) {
                                    this.myArrayList = myArrayList;
                                }

                                public static final class BLOCK_ZONE_MyArrayList {

                                    public final BLOCK_ZONE_MyArrayListMap map;

                                    public BLOCK_ZONE_MyArrayList(BLOCK_ZONE_MyArrayListMap map) {
                                        this.map = map;
                                    }

                                    public static final class BLOCK_ZONE_MyArrayListMap {
                                        public final int x;
                                        public final int y;

                                        public BLOCK_ZONE_MyArrayListMap(int x, int y) {
                                            this.x = x;
                                            this.y = y;
                                        }
                                    }
                                }
                            }

                            public static final class WIN_ZONE {

                                public final WIN_ZONE_MyArrayList[] myArrayList;

                                public WIN_ZONE(WIN_ZONE_MyArrayList[] myArrayList) {
                                    this.myArrayList = myArrayList;
                                }

                                public static final class WIN_ZONE_MyArrayList {

                                    public final WIN_ZONE_MyArrayListMap map;

                                    public WIN_ZONE_MyArrayList(WIN_ZONE_MyArrayListMap map) {
                                        this.map = map;
                                    }

                                    public static final class WIN_ZONE_MyArrayListMap {
                                        public final int x;
                                        public final int y;

                                        public WIN_ZONE_MyArrayListMap(int x, int y) {
                                            this.x = x;
                                            this.y = y;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                public static final class Players {

                    public final MyArrayList myArrayList[];

                    public Players(MyArrayList[] myArrayList) {
                        this.myArrayList = myArrayList;
                    }

                    public static final class MyArrayList {

                        public final MyArrayListMap map;

                        public MyArrayList(MyArrayListMap map) {
                            this.map = map;
                        }

                        public static final class MyArrayListMap {

                            public final int score;
                            public final String name;
                            public final String direction;
                            public final Segments segments;

                            public MyArrayListMap(int score, String name,
                                                  String direction, Segments segments) {
                                this.score = score;
                                this.name = name;
                                this.direction = direction;
                                this.segments = segments;
                            }

                            public static final class Segments {

                                public final SegmentsMyArrayList myArrayList[];

                                public Segments(
                                        SegmentsMyArrayList[] myArrayList) {
                                    this.myArrayList = myArrayList;
                                }

                                public static final class SegmentsMyArrayList {

                                    public final SegmentsMyArrayListMap map;

                                    public SegmentsMyArrayList(
                                            SegmentsMyArrayListMap map) {
                                        this.map = map;
                                    }

                                    public static final class SegmentsMyArrayListMap {

                                        public final int x;
                                        public final int y;

                                        public SegmentsMyArrayListMap(int x,
                                                                      int y) {
                                            this.x = x;
                                            this.y = y;
                                        }

                                    }

                                }

                            }

                        }

                    }

                }

                public static final class RoomMapMap {

                    public final RoomMapMapMap map;

                    public RoomMapMap(RoomMapMapMap map) {
                        this.map = map;
                    }

                    public static final class RoomMapMapMap {

                        public final int horizontal;
                        public final int vertical;

                        public RoomMapMapMap(int horizontal, int vertical) {
                            this.horizontal = horizontal;
                            this.vertical = vertical;
                        }

                    }

                }

                public static final class Wall {

                    public final MyArrayList myArrayList[];

                    public Wall(MyArrayList[] myArrayList) {
                        this.myArrayList = myArrayList;
                    }

                    public static final class MyArrayList {

                        public final WallMap map;

                        public MyArrayList(WallMap map) {
                            this.map = map;
                        }

                        public static final class WallMap {

                            public final int x;
                            public final int y;

                            public WallMap(int x, int y) {
                                this.x = x;
                                this.y = y;
                            }
                        }
                    }
                }
            }
        }
    }

}