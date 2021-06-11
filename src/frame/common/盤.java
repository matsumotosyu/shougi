package frame.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import frame.common.cells.Cell_11;
import frame.common.cells.Cell_12;
import frame.common.cells.Cell_13;
import frame.common.cells.Cell_14;
import frame.common.cells.Cell_15;
import frame.common.cells.Cell_16;
import frame.common.cells.Cell_17;
import frame.common.cells.Cell_18;
import frame.common.cells.Cell_19;
import frame.common.cells.Cell_21;
import frame.common.cells.Cell_22;
import frame.common.cells.Cell_23;
import frame.common.cells.Cell_24;
import frame.common.cells.Cell_25;
import frame.common.cells.Cell_26;
import frame.common.cells.Cell_27;
import frame.common.cells.Cell_28;
import frame.common.cells.Cell_29;
import frame.common.cells.Cell_31;
import frame.common.cells.Cell_32;
import frame.common.cells.Cell_33;
import frame.common.cells.Cell_34;
import frame.common.cells.Cell_35;
import frame.common.cells.Cell_36;
import frame.common.cells.Cell_37;
import frame.common.cells.Cell_38;
import frame.common.cells.Cell_39;
import frame.common.cells.Cell_41;
import frame.common.cells.Cell_42;
import frame.common.cells.Cell_43;
import frame.common.cells.Cell_44;
import frame.common.cells.Cell_45;
import frame.common.cells.Cell_46;
import frame.common.cells.Cell_47;
import frame.common.cells.Cell_48;
import frame.common.cells.Cell_49;
import frame.common.cells.Cell_51;
import frame.common.cells.Cell_52;
import frame.common.cells.Cell_53;
import frame.common.cells.Cell_54;
import frame.common.cells.Cell_55;
import frame.common.cells.Cell_56;
import frame.common.cells.Cell_57;
import frame.common.cells.Cell_58;
import frame.common.cells.Cell_59;
import frame.common.cells.Cell_61;
import frame.common.cells.Cell_62;
import frame.common.cells.Cell_63;
import frame.common.cells.Cell_64;
import frame.common.cells.Cell_65;
import frame.common.cells.Cell_66;
import frame.common.cells.Cell_67;
import frame.common.cells.Cell_68;
import frame.common.cells.Cell_69;
import frame.common.cells.Cell_71;
import frame.common.cells.Cell_72;
import frame.common.cells.Cell_73;
import frame.common.cells.Cell_74;
import frame.common.cells.Cell_75;
import frame.common.cells.Cell_76;
import frame.common.cells.Cell_77;
import frame.common.cells.Cell_78;
import frame.common.cells.Cell_79;
import frame.common.cells.Cell_81;
import frame.common.cells.Cell_82;
import frame.common.cells.Cell_83;
import frame.common.cells.Cell_84;
import frame.common.cells.Cell_85;
import frame.common.cells.Cell_86;
import frame.common.cells.Cell_87;
import frame.common.cells.Cell_88;
import frame.common.cells.Cell_89;
import frame.common.cells.Cell_91;
import frame.common.cells.Cell_92;
import frame.common.cells.Cell_93;
import frame.common.cells.Cell_94;
import frame.common.cells.Cell_95;
import frame.common.cells.Cell_96;
import frame.common.cells.Cell_97;
import frame.common.cells.Cell_98;
import frame.common.cells.Cell_99;
import frame.common.pieces.桂馬;
import frame.common.pieces.歩;
import frame.common.pieces.玉;
import frame.common.pieces.角;
import frame.common.pieces.金;
import frame.common.pieces.銀;
import frame.common.pieces.飛車;
import frame.common.pieces.香車;
import frame.common.symbol.マス;
import frame.common.symbol.駒;

public class 盤 implements Serializable {
    private static final long POLLING_INTERVAL = 10L;
    public String 盤ID;
    public String 評価対象 = null;
    public 保持者 手番 = 保持者.先手;
    public int 手数 = 0;
    public int 先手評価点;
    public int 後手評価点;

    static Connection connection;
    static {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
            //            PreparedStatement createPreparedStatement = null;
            //            String CreateQuery = "CREATE TABLE SCORE(id int primary key, kiten varchar(255), kouryo varchar(255), sente_score int, gote_score int,sente_score_relative int,gote_score_relative int)";
            PreparedStatement deletePreparedStatement = null;
            String DeleteQuery = "DELETE FROM SCORE";
            deletePreparedStatement = connection.prepareStatement(DeleteQuery);
            deletePreparedStatement.executeUpdate();
            deletePreparedStatement.close();

            DeleteQuery = "DELETE FROM STATE";
            deletePreparedStatement = connection.prepareStatement(DeleteQuery);
            deletePreparedStatement.executeUpdate();
            deletePreparedStatement.close();
            connection.commit();
            //            //            String SelectQuery = "select * from PERSON";
            //            connection.setAutoCommit(false);
            //            createPreparedStatement = connection.prepareStatement(CreateQuery);
            //            createPreparedStatement.executeUpdate();
            //            createPreparedStatement.close();
            //            connection.commit();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static マス 先手玉のマス = null;
    public static マス 後手玉のマス = null;
    public List<駒> 先手の盤上 = new ArrayList<駒>();
    public List<駒> 後手の盤上 = new ArrayList<駒>();
    public List<駒> 先手の駒台 = new ArrayList<駒>();
    public List<駒> 後手の駒台 = new ArrayList<駒>();

    public static final int[][] 相対評価点 = {
            { 13, 12, 11, 10, 9, 8, 7, 6, 5 },
            { 12, 12, 11, 10, 9, 8, 7, 6, 5 },
            { 11, 11, 11, 10, 9, 8, 7, 6, 5 },
            { 10, 10, 10, 10, 9, 8, 7, 6, 5 },
            { 9, 9, 9, 9, 9, 8, 7, 6, 5 },
            { 8, 8, 8, 8, 8, 8, 7, 6, 5 },
            { 7, 7, 7, 7, 7, 7, 7, 6, 5 },
            { 6, 6, 6, 6, 6, 6, 6, 6, 5 },
            { 5, 5, 5, 5, 5, 5, 5, 5, 5 } };

    public static final int[][] 先手絶対評価点 = {
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    public static final int[][] 後手絶対評価点 = {
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

    public マス[][] マスリスト;

    public void init() {
        マスリスト = new マス[][] { { null, null, null, null, null, null, null, null, null, null },
                { null, new Cell_11(), new Cell_12(), new Cell_13(), new Cell_14(), new Cell_15(), new Cell_16(),
                        new Cell_17(), new Cell_18(), new Cell_19() },
                { null, new Cell_21(), new Cell_22(), new Cell_23(), new Cell_24(), new Cell_25(), new Cell_26(),
                        new Cell_27(), new Cell_28(), new Cell_29() },
                { null, new Cell_31(), new Cell_32(), new Cell_33(), new Cell_34(), new Cell_35(), new Cell_36(),
                        new Cell_37(), new Cell_38(), new Cell_39() },
                { null, new Cell_41(), new Cell_42(), new Cell_43(), new Cell_44(), new Cell_45(), new Cell_46(),
                        new Cell_47(), new Cell_48(), new Cell_49() },
                { null, new Cell_51(), new Cell_52(), new Cell_53(), new Cell_54(), new Cell_55(), new Cell_56(),
                        new Cell_57(), new Cell_58(), new Cell_59() },
                { null, new Cell_61(), new Cell_62(), new Cell_63(), new Cell_64(), new Cell_65(), new Cell_66(),
                        new Cell_67(), new Cell_68(), new Cell_69() },
                { null, new Cell_71(), new Cell_72(), new Cell_73(), new Cell_74(), new Cell_75(), new Cell_76(),
                        new Cell_77(), new Cell_78(), new Cell_79() },
                { null, new Cell_81(), new Cell_82(), new Cell_83(), new Cell_84(), new Cell_85(), new Cell_86(),
                        new Cell_87(), new Cell_88(), new Cell_89() },
                { null, new Cell_91(), new Cell_92(), new Cell_93(), new Cell_94(), new Cell_95(), new Cell_96(),
                        new Cell_97(), new Cell_98(), new Cell_99() }
        };

        // 後手版の駒生成
        玉 後手_玉 = new 玉(マスリスト[5][1], 保持者.後手, マスリスト);
        玉 先手_玉 = new 玉(マスリスト[5][9], 保持者.先手, マスリスト);

        金 後手_金_右 = new 金(マスリスト[6][1], 保持者.後手, マスリスト);
        金 後手_金_左 = new 金(マスリスト[4][1], 保持者.後手, マスリスト);
        金 先手_金_右 = new 金(マスリスト[4][9], 保持者.先手, マスリスト);
        金 先手_金_左 = new 金(マスリスト[6][9], 保持者.先手, マスリスト);

        銀 後手_銀_右 = new 銀(マスリスト[7][1], 保持者.後手, マスリスト);
        銀 後手_銀_左 = new 銀(マスリスト[3][1], 保持者.後手, マスリスト);
        銀 先手_銀_右 = new 銀(マスリスト[3][9], 保持者.先手, マスリスト);
        銀 先手_銀_左 = new 銀(マスリスト[7][9], 保持者.先手, マスリスト);

        桂馬 後手_桂馬_右 = new 桂馬(マスリスト[8][1], 保持者.後手, マスリスト);
        桂馬 後手_桂馬_左 = new 桂馬(マスリスト[2][1], 保持者.後手, マスリスト);
        桂馬 先手_桂馬_右 = new 桂馬(マスリスト[2][9], 保持者.先手, マスリスト);
        桂馬 先手_桂馬_左 = new 桂馬(マスリスト[8][9], 保持者.先手, マスリスト);

        香車 後手_香車_右 = new 香車(マスリスト[9][1], 保持者.後手, マスリスト);
        香車 後手_香車_左 = new 香車(マスリスト[1][1], 保持者.後手, マスリスト);
        香車 先手_香車_右 = new 香車(マスリスト[1][9], 保持者.先手, マスリスト);
        香車 先手_香車_左 = new 香車(マスリスト[9][9], 保持者.先手, マスリスト);

        角 後手_角 = new 角(マスリスト[2][2], 保持者.後手, マスリスト);
        角 先手_角 = new 角(マスリスト[8][8], 保持者.先手, マスリスト);

        飛車 後手_飛車 = new 飛車(マスリスト[8][2], 保持者.後手, マスリスト);
        飛車 先手_飛車 = new 飛車(マスリスト[2][8], 保持者.先手, マスリスト);

        歩 後手_歩_1 = new 歩(マスリスト[1][3], 保持者.後手, マスリスト);
        歩 先手_歩_1 = new 歩(マスリスト[9][7], 保持者.先手, マスリスト);
        歩 後手_歩_2 = new 歩(マスリスト[2][3], 保持者.後手, マスリスト);
        歩 先手_歩_2 = new 歩(マスリスト[8][7], 保持者.先手, マスリスト);
        歩 後手_歩_3 = new 歩(マスリスト[3][3], 保持者.後手, マスリスト);
        歩 先手_歩_3 = new 歩(マスリスト[7][7], 保持者.先手, マスリスト);
        歩 後手_歩_4 = new 歩(マスリスト[4][3], 保持者.後手, マスリスト);
        歩 先手_歩_4 = new 歩(マスリスト[6][7], 保持者.先手, マスリスト);
        歩 後手_歩_5 = new 歩(マスリスト[5][3], 保持者.後手, マスリスト);
        歩 先手_歩_5 = new 歩(マスリスト[5][7], 保持者.先手, マスリスト);
        歩 後手_歩_6 = new 歩(マスリスト[6][3], 保持者.後手, マスリスト);
        歩 先手_歩_6 = new 歩(マスリスト[4][7], 保持者.先手, マスリスト);
        歩 後手_歩_7 = new 歩(マスリスト[7][3], 保持者.後手, マスリスト);
        歩 先手_歩_7 = new 歩(マスリスト[3][7], 保持者.先手, マスリスト);
        歩 後手_歩_8 = new 歩(マスリスト[8][3], 保持者.後手, マスリスト);
        歩 先手_歩_8 = new 歩(マスリスト[2][7], 保持者.先手, マスリスト);
        歩 後手_歩_9 = new 歩(マスリスト[9][3], 保持者.後手, マスリスト);
        歩 先手_歩_9 = new 歩(マスリスト[1][7], 保持者.先手, マスリスト);

        マスリスト[5][1].居駒 = 後手_玉;
        マスリスト[5][1].後手の駒あり = true;
        マスリスト[5][9].居駒 = 先手_玉;
        マスリスト[5][9].先手の駒あり = true;
        マスリスト[6][1].居駒 = 後手_金_右;
        マスリスト[6][1].後手の駒あり = true;
        マスリスト[4][1].居駒 = 後手_金_左;
        マスリスト[4][1].後手の駒あり = true;
        マスリスト[4][9].居駒 = 先手_金_右;
        マスリスト[4][9].先手の駒あり = true;
        マスリスト[6][9].居駒 = 先手_金_左;
        マスリスト[6][9].先手の駒あり = true;

        マスリスト[7][1].居駒 = 後手_銀_右;
        マスリスト[7][1].後手の駒あり = true;
        マスリスト[3][1].居駒 = 後手_銀_左;
        マスリスト[3][1].後手の駒あり = true;
        マスリスト[3][9].居駒 = 先手_銀_右;
        マスリスト[3][9].先手の駒あり = true;
        マスリスト[7][9].居駒 = 先手_銀_左;
        マスリスト[7][9].先手の駒あり = true;

        マスリスト[8][1].居駒 = 後手_桂馬_右;
        マスリスト[8][1].後手の駒あり = true;
        マスリスト[2][1].居駒 = 後手_桂馬_左;
        マスリスト[2][1].後手の駒あり = true;
        マスリスト[2][9].居駒 = 先手_桂馬_右;
        マスリスト[2][9].先手の駒あり = true;
        マスリスト[8][9].居駒 = 先手_桂馬_左;
        マスリスト[8][9].先手の駒あり = true;

        マスリスト[9][1].居駒 = 後手_香車_右;
        マスリスト[9][1].後手の駒あり = true;
        マスリスト[1][1].居駒 = 後手_香車_左;
        マスリスト[1][1].後手の駒あり = true;
        マスリスト[1][9].居駒 = 先手_香車_右;
        マスリスト[1][9].先手の駒あり = true;
        マスリスト[9][9].居駒 = 先手_香車_左;
        マスリスト[9][9].先手の駒あり = true;

        マスリスト[2][2].居駒 = 後手_角;
        マスリスト[2][2].後手の駒あり = true;
        マスリスト[8][8].居駒 = 先手_角;
        マスリスト[8][8].先手の駒あり = true;

        マスリスト[8][2].居駒 = 後手_飛車;
        マスリスト[8][2].後手の駒あり = true;
        マスリスト[2][8].居駒 = 先手_飛車;
        マスリスト[2][8].先手の駒あり = true;

        マスリスト[1][3].居駒 = 後手_歩_1;
        マスリスト[1][3].後手の駒あり = true;
        マスリスト[9][7].居駒 = 先手_歩_1;
        マスリスト[9][7].先手の駒あり = true;

        マスリスト[2][3].居駒 = 後手_歩_2;
        マスリスト[2][3].後手の駒あり = true;
        マスリスト[8][7].居駒 = 先手_歩_2;
        マスリスト[8][7].先手の駒あり = true;

        マスリスト[3][3].居駒 = 後手_歩_3;
        マスリスト[3][3].後手の駒あり = true;
        マスリスト[7][7].居駒 = 先手_歩_3;
        マスリスト[7][7].先手の駒あり = true;

        マスリスト[4][3].居駒 = 後手_歩_4;
        マスリスト[4][3].後手の駒あり = true;
        マスリスト[6][7].居駒 = 先手_歩_4;
        マスリスト[6][7].先手の駒あり = true;

        マスリスト[5][3].居駒 = 後手_歩_5;
        マスリスト[5][3].後手の駒あり = true;
        マスリスト[5][7].居駒 = 先手_歩_5;
        マスリスト[5][7].先手の駒あり = true;

        マスリスト[6][3].居駒 = 後手_歩_6;
        マスリスト[6][3].後手の駒あり = true;
        マスリスト[4][7].居駒 = 先手_歩_6;
        マスリスト[4][7].先手の駒あり = true;

        マスリスト[7][3].居駒 = 後手_歩_7;
        マスリスト[7][3].後手の駒あり = true;
        マスリスト[3][7].居駒 = 先手_歩_7;
        マスリスト[3][7].先手の駒あり = true;

        マスリスト[8][3].居駒 = 後手_歩_8;
        マスリスト[8][3].後手の駒あり = true;
        マスリスト[2][7].居駒 = 先手_歩_8;
        マスリスト[2][7].先手の駒あり = true;

        マスリスト[9][3].居駒 = 後手_歩_9;
        マスリスト[9][3].後手の駒あり = true;
        マスリスト[1][7].居駒 = 先手_歩_9;
        マスリスト[1][7].先手の駒あり = true;

        後手玉のマス = マスリスト[5][1];
        先手玉のマス = マスリスト[5][9];

        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                tmp.後手玉のマス = 後手玉のマス;
                tmp.先手玉のマス = 先手玉のマス;
            }
        }
        this.再計算();
    }

    public void 先手指す(String 次の手) {
        先手指す(Integer.parseInt(次の手.split(", ")[1]), Integer.parseInt(次の手.split(", ")[2]),
                Integer.parseInt(次の手.split(", ")[3]), Integer.parseInt(次の手.split(", ")[4]),
                Boolean.parseBoolean(次の手.split(", ")[5]));
    }

    public void 後手指す(String 次の手) {
        後手指す(Integer.parseInt(次の手.split(", ")[1]), Integer.parseInt(次の手.split(", ")[2]),
                Integer.parseInt(次の手.split(", ")[3]), Integer.parseInt(次の手.split(", ")[4]),
                Boolean.parseBoolean(次の手.split(", ")[5]));
    }

    public void 先手指す(final int 元筋, final int 元段, final int 先筋, final int 先段, final boolean 成り) {
        this.先手指す(マスリスト[元筋][元段].居駒, マスリスト[先筋][先段], 成り);
    }

    public void 後手指す(final int 元筋, final int 元段, final int 先筋, final int 先段, final boolean 成り) {
        this.後手指す(マスリスト[元筋][元段].居駒, マスリスト[先筋][先段], 成り);
    }

    public void 先手指す(駒 先手の駒, マス 移動先, boolean 成り) {
        if (先手の駒 instanceof 玉) {
            先手玉のマス = 移動先;
            for (int i = 1; i < 10; i++) {
                for (int j = 1; j < 10; j++) {
                    マス tmp = マスリスト[i][j];
                    tmp.先手玉のマス = 先手玉のマス;
                }
            }
        }
        if (移動先.後手の駒あり) {
            移動先.居駒.成り = false;
            移動先.居駒.owner = 保持者.先手;
            先手の駒台.add(移動先.居駒);
            移動先.居駒.駒台へ移動();
        }
        先手の駒.move(移動先);
        先手の駒.成り = 成り;
        if (先手の駒.成り) {
            先手の駒.成り();
        }
        手番 = 保持者.後手;
        手数++;
        this.再計算();
    }

    public void 後手指す(駒 後手の駒, マス 移動先, boolean 成り) {
        if (後手の駒 instanceof 玉) {
            後手玉のマス = 移動先;
            for (int i = 1; i < 10; i++) {
                for (int j = 1; j < 10; j++) {
                    マス tmp = マスリスト[i][j];
                    tmp.後手玉のマス = 後手玉のマス;
                }
            }
        }
        if (移動先.先手の駒あり) {
            移動先.居駒.成り = false;
            移動先.居駒.owner = 保持者.後手;
            移動先.居駒.駒台へ移動();
            後手の駒台.add(移動先.居駒);
        }
        後手の駒.move(移動先);
        後手の駒.成り = 成り;
        if (後手の駒.成り) {
            後手の駒.成り();
        }
        手番 = 保持者.先手;
        手数++;
        this.再計算();
    }

    public void 先手打つ(駒 先手の駒, マス 打ち先) {
        先手の駒台.remove(先手の駒);
        先手の駒.put(打ち先);
        手番 = 保持者.後手;
        手数++;
        this.再計算();
    }

    public void 後手打つ(駒 後手の駒, マス 打ち先) {
        後手の駒台.remove(後手の駒);
        後手の駒.put(打ち先);
        手番 = 保持者.先手;
        手数++;
        this.再計算();
    }

    public List<String> 後手候補手リスト取得() {
        List<String> 候補手リスト = new ArrayList<String>();
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                if (tmp.後手の駒あり) {
                    for (マス tmp2 : tmp.居駒.movableCells) {
                        if (!tmp2.後手の駒あり) {
                            String 候補手 = tmp.居駒.toString() + ", " + tmp.get筋() + ", " + tmp.get段() + ", "
                                    + tmp2.get筋()
                                    + ", "
                                    + tmp2.get段() + ", "
                                    + "false";
                            候補手リスト.add(候補手);
                        }
                    }
                }
            }
        }
        return 候補手リスト;
    }

    public List<String> 先手候補手リスト取得() {
        List<String> 候補手リスト = new ArrayList<String>();
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                if (tmp.先手の駒あり) {
                    for (マス tmp2 : tmp.居駒.movableCells) {
                        if (!tmp2.先手の駒あり) {
                            String 候補手 = tmp.居駒.toString() + ", " + tmp.get筋() + ", " + tmp.get段() + ", "
                                    + tmp2.get筋()
                                    + ", "
                                    + tmp2.get段() + ", "
                                    + "false";
                            候補手リスト.add(候補手);
                        }
                    }
                }
            }
        }
        return 候補手リスト;
    }

    private void 再計算() {
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                tmp.run();
            }
        }
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                if (tmp.居駒 != null) {
                    tmp.居駒.実行モード_移動可能マス再計算 = true;
                    tmp.居駒.実行モード_評価点再計算 = false;
                    tmp.居駒.run();
                }
            }
        }
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                if (tmp.居駒 != null) {
                    tmp.居駒.実行モード_移動可能マス再計算 = false;
                    tmp.居駒.実行モード_評価点再計算 = true;
                    tmp.居駒.run();
                }
            }
        }
        /* 持ち駒の評価 */
        //////////////////////////////////
        for (駒 tmp : 先手の駒台) {
            tmp.実行モード_移動可能マス再計算 = true;
            tmp.実行モード_評価点再計算 = false;
            tmp.run();
        }
        for (駒 tmp : 先手の駒台) {
            tmp.実行モード_移動可能マス再計算 = false;
            tmp.実行モード_評価点再計算 = true;
            tmp.run();
        }
        for (駒 tmp : 後手の駒台) {
            tmp.実行モード_移動可能マス再計算 = true;
            tmp.実行モード_評価点再計算 = false;
            tmp.run();
        }
        for (駒 tmp : 後手の駒台) {
            tmp.実行モード_移動可能マス再計算 = false;
            tmp.実行モード_評価点再計算 = true;
            tmp.run();
        }
        //////////////////////////////////

        先手評価点 = 0;
        後手評価点 = 0;
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                if (tmp.居駒 != null) {
                    switch (tmp.居駒.owner) {
                    case 先手:
                        先手評価点 += tmp.居駒.評価点;
                        break;
                    case 後手:
                        後手評価点 += tmp.居駒.評価点;
                        break;
                    }
                }
            }
        }
        for (駒 tmp : 先手の駒台) {
            先手評価点 += tmp.評価点;
        }
        for (駒 tmp : 後手の駒台) {
            後手評価点 += tmp.評価点;
        }
    }

    public void 評価出力() throws SQLException {
        PreparedStatement insertPreparedStatement = null;

        String InsertQuery = "INSERT INTO SCORE"
                + "(id, kiten,kouryo,sente_score,gote_score,sente_score_relative,gote_score_relative) values"
                + "(?,?,?,?,?,?,?)";
        try {
            connection.setAutoCommit(false);
            insertPreparedStatement = connection.prepareStatement(InsertQuery);
            insertPreparedStatement.setInt(1, Integer.parseInt(盤ID));
            insertPreparedStatement.setString(2, 評価対象.split("&")[0]);
            insertPreparedStatement.setString(3, 評価対象);
            insertPreparedStatement.setInt(4, 先手評価点);
            insertPreparedStatement.setInt(5, 後手評価点);
            insertPreparedStatement.setInt(6, 先手評価点 - 後手評価点);
            insertPreparedStatement.setInt(7, 後手評価点 - 先手評価点);
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();

            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

}
