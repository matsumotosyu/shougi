package frame.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import frame.common.util.Grapher;

/**
 * TODO リスト
 * ・DBではなくマップにする
 * ・不要な読みを削除する（α-β）
 * @author xiau
 *
 */
public class GameMaster {
    public static final int 読みの深度 = 4;
    private static 保持者 先手後手 = 保持者.先手;
    private static 盤 gm = new 盤();

    public static void main(String[] args)
            throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        try {
            scoreDelete();
            gm.盤ID = "1";
            gm.init();
            switch (先手後手) {
            case 先手:
                while (true) {
                    long startTime = System.currentTimeMillis();
                    MoveSearcher ms = new MoveSearcher();
                    ms.基盤 = gm;
                    ms.深度 = 読みの深度;
                    Thread compute = new Thread(ms);
                    compute.start();
                    Thread.sleep(5000L);
                    while (true) {
                        Thread.sleep(5000L);
                        if (isAllComputed()) {
                            break;
                        }
                    }
                    String 返し手 = ms.selectNextMove();
                    gm.先手指す(返し手);
                    System.out.println("返し手：" + 返し手 + ", 考慮時間:" + (System.currentTimeMillis() - startTime));
                    Grapher g = new Grapher();
                    g.graphNow(gm.マスリスト);

                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("なんか入力してください。");
                    String 入力値 = new String(in.readLine());
                    gm.後手指す(入力値);
                    g.graphNow(gm.マスリスト);
                    scoreDelete();
                }
            case 後手:
                while (true) {
                    long startTime = System.currentTimeMillis();
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("なんか入力してください。");
                    String 入力値 = new String(in.readLine());
                    gm.先手指す(入力値);
                    Grapher g = new Grapher();
                    g.graphNow(gm.マスリスト);

                    MoveSearcher ms = new MoveSearcher();
                    ms.基盤 = gm;
                    ms.深度 = 読みの深度;
                    Thread compute = new Thread(ms);
                    compute.start();
                    Thread.sleep(10000L);
                    while (true) {
                        Thread.sleep(1000L);
                        if (isAllComputed()) {
                            break;
                        }
                    }
                    String 返し手 = ms.selectNextMove();
                    System.out.println("返し手：" + 返し手 + ", 考慮時間:" + (System.currentTimeMillis() - startTime));
                    gm.後手指す(返し手);
                    g.graphNow(gm.マスリスト);

                    scoreDelete();
                }
            default:
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            scoreDelete();
        }

    }

    private static boolean isAllComputed() throws SQLException {
        boolean rtn = false;
        PreparedStatement selectPreparedStatement = null;
        String SelectQuery = "SELECT COUNT(*) FROM STATE WHERE start = 1 AND end = 0";
        selectPreparedStatement = gm.connection.prepareStatement(SelectQuery);
        ResultSet rs = selectPreparedStatement.executeQuery();
        System.out.println("H2 Database inserted through PreparedStatement");
        while (rs.next()) {
            System.out.println("計算実行中:" +
                    rs.getString("COUNT(*)") + "件");
            if (rs.getInt("COUNT(*)") == 0) {
                rtn = true;
            } else {
                rtn = false;
            }
        }
        SelectQuery = "SELECT COUNT(*) FROM STATE WHERE start = 1 AND end = 1";
        selectPreparedStatement = gm.connection.prepareStatement(SelectQuery);
        rs = selectPreparedStatement.executeQuery();
        System.out.println("H2 Database inserted through PreparedStatement");
        while (rs.next()) {
            System.out.println("計算完了:" +
                    rs.getString("COUNT(*)") + "件");
        }

        //        gm.connection.commit();
        return rtn;
    }

    private static void scoreDelete() throws SQLException {
        PreparedStatement deletePreparedStatement = null;
        String DeleteQuery = "DELETE FROM STATE";
        deletePreparedStatement = gm.connection.prepareStatement(DeleteQuery);
        deletePreparedStatement.executeUpdate();

        DeleteQuery = "DELETE FROM SCORE";
        deletePreparedStatement = gm.connection.prepareStatement(DeleteQuery);
        deletePreparedStatement.executeUpdate();

        deletePreparedStatement.close();
        gm.connection.commit();
    }
}
