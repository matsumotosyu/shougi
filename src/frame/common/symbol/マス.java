package frame.common.symbol;

import java.io.Serializable;

import frame.common.盤;

public class マス implements Serializable  {
    protected byte 筋;
    protected byte 段;
    public boolean 先手の駒あり;
    public boolean 後手の駒あり;
    public 駒 居駒 = null;
    public int マス点 = 0;
    public boolean 計算中 = false;
    public マス 先手玉のマス;
    public マス 後手玉のマス;

    public マス(final byte 筋, final byte 段) {
        this.筋 = 筋;
        this.段 = 段;
    }

    public byte get筋() {
        return 筋;
    }

    public byte get段() {
        return 段;
    }

    public void run() {
//        System.out.println("マス" + 筋 + ":" + 段 + "計算開始");
//        計算中 = true;
        // 先手、後手の玉の位置から自分の点数を計算
        byte 先手玉からの筋の差 = (byte) Math.abs(先手玉のマス.筋 - 筋);
        byte 先手玉からの段の差 = (byte) Math.abs(先手玉のマス.段 - 段);
        int 先手玉からの相対点数 = 盤.相対評価点[先手玉からの筋の差][先手玉からの段の差];
        byte 後手玉からの筋の差 = (byte) Math.abs(後手玉のマス.筋 - 筋);
        byte 後手玉からの段の差 = (byte) Math.abs(後手玉のマス.段 - 段);
        int 後手玉からの相対点数 = 盤.相対評価点[後手玉からの筋の差][後手玉からの段の差];
        マス点 = 先手玉からの相対点数 + 後手玉からの相対点数;
        計算中 = false;
//        System.out.println("マス" + 筋 + ":" + 段 + "計算完了");
    }

}
