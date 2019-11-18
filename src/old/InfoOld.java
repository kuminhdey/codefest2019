package old;
public final class InfoOld {

    public final Map map;

    public InfoOld(Map map) {
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

                    public final ZoneMap map;

                    public Zone(ZoneMap map) {
                        this.map = map;
                    }

                    public static final class ZoneMap {

                        public final Coordinate coordinate;
                        public final Check_point check_point;
                        public final Allowed_place allowed_place;

                        public ZoneMap(Coordinate coordinate, Check_point check_point, Allowed_place allowed_place) {
                            this.coordinate = coordinate;
                            this.check_point = check_point;
                            this.allowed_place = allowed_place;
                        }

                        public static final class Coordinate {

                            public final CoordinateMap map;

                            public Coordinate(CoordinateMap map) {
                                this.map = map;
                            }

                            public static final class CoordinateMap {
                                public final Top_right top_right;
                                public final Top_left top_left;
                                public final Bottom_right bottom_right;
                                public final Bottom_left bottom_left;

                                public CoordinateMap(Top_right top_right, Top_left top_left, Bottom_right bottom_right, Bottom_left bottom_left) {
                                    this.top_right = top_right;
                                    this.top_left = top_left;
                                    this.bottom_right = bottom_right;
                                    this.bottom_left = bottom_left;
                                }

                                public static final class Top_right {

                                    public final Top_rightMap map;

                                    public Top_right(Top_rightMap map) {
                                        this.map = map;
                                    }

                                    public static final class Top_rightMap {
                                        public final int x;
                                        public final int y;

                                        public Top_rightMap(int x, int y) {
                                            this.x = x;
                                            this.y = y;
                                        }
                                    }
                                }

                                public static final class Top_left {

                                    public final Top_leftMap map;

                                    public Top_left(Top_leftMap map) {
                                        this.map = map;
                                    }

                                    public static final class Top_leftMap {
                                        public final int x;
                                        public final int y;

                                        public Top_leftMap(int x, int y) {
                                            this.x = x;
                                            this.y = y;
                                        }
                                    }
                                }

                                public static final class Bottom_right {

                                    public final Bottom_rightMap map;

                                    public Bottom_right(Bottom_rightMap map) {
                                        this.map = map;
                                    }

                                    public static final class Bottom_rightMap {
                                        public final int x;
                                        public final int y;

                                        public Bottom_rightMap(int x, int y) {
                                            this.x = x;
                                            this.y = y;
                                        }
                                    }
                                }

                                public static final class Bottom_left {

                                    public final Bottom_leftMap map;

                                    public Bottom_left(Bottom_leftMap map) {
                                        this.map = map;
                                    }

                                    public static final class Bottom_leftMap {
                                        public final int x;
                                        public final int y;

                                        public Bottom_leftMap(int x, int y) {
                                            this.x = x;
                                            this.y = y;
                                        }
                                    }
                                }
                            }
                        }

                        public static final class Check_point {

                            public final MyArrayList myArrayList[];

                            public Check_point(MyArrayList[] myArrayList) {
                                this.myArrayList = myArrayList;
                            }

                            public static final class MyArrayList {

                                public final Check_pointMap map;

                                public MyArrayList(Check_pointMap map) {
                                    this.map = map;
                                }

                                public static final class Check_pointMap {

                                    public final int x;
                                    public final int y;

                                    public Check_pointMap(int x, int y) {
                                        this.x = x;
                                        this.y = y;
                                    }
                                }
                            }
                        }

                        public static final class Allowed_place {

                            public final MyArrayList myArrayList[];

                            public Allowed_place(MyArrayList[] myArrayList) {
                                this.myArrayList = myArrayList;
                            }

                            public static final class MyArrayList {

                                public final Allowed_placeMap map;

                                public MyArrayList(Allowed_placeMap map) {
                                    this.map = map;
                                }

                                public static final class Allowed_placeMap {

                                    public final int x;
                                    public final int y;

                                    public Allowed_placeMap(int x, int y) {
                                        this.x = x;
                                        this.y = y;
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