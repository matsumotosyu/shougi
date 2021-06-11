package frame.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import frame.common.util.Serializer;

public class MoveSearcher implements Runnable {
    public 盤 基盤;
    public int 深度;
    public static AtomicInteger スレッド数 = new AtomicInteger(0);
    public static ConcurrentLinkedQueue<MoveSearcher> 継ぎ盤キュー;// TODO 実装見直し
    public static ExecutorService exec = Executors.newFixedThreadPool(1000);

    @Override
    public void run() {
        try {
            開始();
            if (深度 == 0) {
                基盤.評価出力();
                終了();
                return;
            }
            switch (基盤.手番) {
            case 先手:
                List<String> 先手候補手リスト = 基盤.先手候補手リスト取得();
                for (String 候補手 : 先手候補手リスト) {
                    盤 継ぎ盤 = (盤) (Serializer.deepCopy(基盤));
                    継ぎ盤.盤ID = Long.toString(System.currentTimeMillis()).substring(6);
                    if (基盤.評価対象 != null) {
                        継ぎ盤.評価対象 = 基盤.評価対象 + "&" + 候補手;
                    } else {
                        継ぎ盤.評価対象 = 候補手;
                    }
//                    System.out.println(継ぎ盤.評価対象);
                    継ぎ盤.先手指す(候補手);
                    MoveSearcher tmp = new MoveSearcher();
                    tmp.基盤 = 継ぎ盤;
                    tmp.深度 = 深度 - 1;
                    exec.execute(tmp);
//                    Thread compute = new Thread(tmp);
//                    compute.start();
                }
                break;
            case 後手:
                List<String> 後手候補手リスト = 基盤.後手候補手リスト取得();
                for (String 候補手 : 後手候補手リスト) {
                    盤 継ぎ盤 = (盤) (Serializer.deepCopy(基盤));
                    継ぎ盤.盤ID = Long.toString(System.currentTimeMillis()).substring(6);
                    if (基盤.評価対象 != null) {
                        継ぎ盤.評価対象 = 基盤.評価対象 + "&" + 候補手;
                    } else {
                        継ぎ盤.評価対象 = 候補手;
                    }
//                    System.out.println(継ぎ盤.評価対象);
                    継ぎ盤.後手指す(候補手);
                    MoveSearcher tmp = new MoveSearcher();
                    tmp.基盤 = 継ぎ盤;
                    tmp.深度 = 深度 - 1;
                    exec.execute(tmp);
//                    Thread compute = new Thread(tmp);
//                    compute.start();
                }
                break;
            }
            終了();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public String selectNextMove() throws ClassNotFoundException, IOException, SQLException {
        String 起点の手 = null;
        PreparedStatement selectPreparedStatement = null;
        String SelectQuery = null;
        switch (GameMaster.読みの深度 % 2) {
        case 0:
            SelectQuery = "SELECT KITEN FROM (SELECT KITEN,MIN(SENTE_SCORE_RELATIVE) AS SENTE_SCORE_RELATIVE FROM SCORE GROUP BY KITEN) ORDER BY SENTE_SCORE_RELATIVE DESC LIMIT 1";
            break;
        case 1:
            SelectQuery = "SELECT KITEN FROM (SELECT KITEN,MAX(SENTE_SCORE_RELATIVE) AS SENTE_SCORE_RELATIVE FROM SCORE GROUP BY KITEN ) ORDER BY SENTE_SCORE_RELATIVE DESC LIMIT 1";
            break;
        }
        selectPreparedStatement = 基盤.connection.prepareStatement(SelectQuery);
        ResultSet rs = selectPreparedStatement.executeQuery();
        System.out.println("H2 Database inserted through PreparedStatement");
        List<String[]> rtn = new ArrayList<String[]>();
        while (rs.next()) {
            起点の手 = rs.getString("KITEN");
        }
        scoreDelete();

        return 起点の手;

    }

    private void scoreDelete() throws SQLException {
        PreparedStatement selectPreparedStatement = null;
        String SelectQuery = "SELECT KITEN,KOURYO,SENTE_SCORE,GOTE_SCORE,SENTE_SCORE_RELATIVE,GOTE_SCORE_RELATIVE FROM SCORE";
        selectPreparedStatement = 基盤.connection.prepareStatement(SelectQuery);
        ResultSet rs = selectPreparedStatement.executeQuery();
        System.out.println("H2 Database inserted through PreparedStatement");
        File f = new File("c:\\tmp\\test1.txt");
        try (FileOutputStream fo = new FileOutputStream(f)) {
            while (rs.next()) {
                fo.write((rs.getString("KITEN") + "," +
                        rs.getString("KOURYO") + "," +
                        rs.getString("SENTE_SCORE") + "," +
                        rs.getString("GOTE_SCORE") + "," +
                        rs.getString("SENTE_SCORE_RELATIVE") + "," +
                        rs.getString("GOTE_SCORE_RELATIVE") + "\r\n").getBytes());
                System.out.println(rs.getString("KITEN") + "," +
                        rs.getString("KOURYO") + "," +
                        rs.getString("SENTE_SCORE") + "," +
                        rs.getString("GOTE_SCORE") + "," +
                        rs.getString("SENTE_SCORE_RELATIVE") + "," +
                        rs.getString("GOTE_SCORE_RELATIVE"));
            }
            selectPreparedStatement.close();
        } catch (Exception e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

        PreparedStatement deletePreparedStatement = null;
        String DeleteQuery = "DELETE FROM SCORE";
        deletePreparedStatement = 基盤.connection.prepareStatement(DeleteQuery);
        deletePreparedStatement.executeUpdate();
        deletePreparedStatement.close();
        基盤.connection.commit();
    }

    public void 開始() throws SQLException {
        PreparedStatement insertPreparedStatement = null;
        スレッド数.incrementAndGet();
        String InsertQuery = "INSERT INTO STATE"
                + "(id, start,end) values"
                + "(?,?,?)";
        try {
            基盤.connection.setAutoCommit(false);
            insertPreparedStatement = 基盤.connection.prepareStatement(InsertQuery);
            insertPreparedStatement.setInt(1, Integer.parseInt(基盤.盤ID));
            insertPreparedStatement.setInt(2, 1);
            insertPreparedStatement.setInt(3, 0);
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();

//            基盤.connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void 終了() throws SQLException {
        PreparedStatement updatePreparedStatement = null;

        String UpdateQuery = "UPDATE STATE SET start = ?,end = ? WHERE ID = ?";
        try {
            基盤.connection.setAutoCommit(false);
            updatePreparedStatement = 基盤.connection.prepareStatement(UpdateQuery);
            updatePreparedStatement.setInt(1, 1);
            updatePreparedStatement.setInt(2, 1);
            updatePreparedStatement.setInt(3, Integer.parseInt(基盤.盤ID));
            updatePreparedStatement.executeUpdate();
            updatePreparedStatement.close();

//            基盤.connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        スレッド数.decrementAndGet();
        System.gc();
    }
}
