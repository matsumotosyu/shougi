package frame.common.symbol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import frame.common.保持者;
import frame.common.盤;
import frame.common.pieces.桂馬;
import frame.common.pieces.歩;
import frame.common.pieces.香車;

public class 駒 implements Serializable {
    protected マス current;
    public 保持者 owner;
    public boolean 成り = false;
    public boolean 台上 = false;
    public List<マス> movableCells;
    public boolean 実行モード_移動可能マス再計算 = false;
    public boolean 実行モード_評価点再計算 = false;
    public boolean 計算中 = false;
    public int 評価点 = 0;
    protected boolean 前;
    protected boolean 後;
    protected boolean 右;
    protected boolean 左;
    protected boolean 右前;
    protected boolean 右後;
    protected boolean 左前;
    protected boolean 左後;
    protected boolean 直進前;
    protected boolean 直進後;
    protected boolean 直進右;
    protected boolean 直進左;
    protected boolean 直進右前;
    protected boolean 直進右後;
    protected boolean 直進左前;
    protected boolean 直進左後;
    protected boolean 桂馬跳び右;
    protected boolean 桂馬跳び左;
    public マス[][] マスリスト;

    public 駒(マス pos, 保持者 owner, マス[][] マスリスト) {
        this.current = pos;
        this.owner = owner;
        this.マスリスト = マスリスト;
        this.movableCells = new ArrayList<マス>();
    }

    public void move(マス pos) {
        // 今居るマスをクリーン
        this.current.先手の駒あり = false;
        this.current.後手の駒あり = false;
        this.current.居駒 = null;
        pos.居駒 = this;
        this.current = pos;
        switch (owner) {
        case 先手:
            pos.先手の駒あり = true;
            pos.後手の駒あり = false;
            break;
        case 後手:
            pos.先手の駒あり = false;
            pos.後手の駒あり = true;
            break;
        }
    }

    public void put(マス pos) {
        pos.居駒 = this;
        this.current = pos;
        switch (owner) {
        case 先手:
            pos.先手の駒あり = true;
            pos.後手の駒あり = false;
            break;
        case 後手:
            pos.先手の駒あり = false;
            pos.後手の駒あり = true;
            break;
        }
        台上 = false;
    }

    public void 成り() {
    }

    public void 駒台へ移動() {
        台上 = true;
    }

    public void resetMovableCells() {
        switch (owner) {
        case 先手:
            movableCells.clear();
            if (台上) {
                if (this instanceof 歩) {
                    for (int i = 1; i < 10; i++) {
//                        boolean 二歩 = false;
//                        for (int j = 1; j < 10; j++) {
//                            マス tmp = マスリスト[i][j];
//                            if (tmp.居駒 != null) {
//                                if (tmp.先手の駒あり && tmp.居駒 instanceof 歩 && !((歩) tmp.居駒).成り) {
//                                    二歩 = true;
//                                }
//                            }
//                        }
//                        if (二歩) {
//                            continue;
//                        }

                        for (int j = 2; j < 10; j++) {
                            マス tmp = マスリスト[i][j];
                            if (tmp.居駒 == null) {
                                movableCells.add(tmp);
                            }
                        }
                    }
                } else if (this instanceof 桂馬) {
                    for (int i = 1; i < 10; i++) {
                        for (int j = 3; j < 10; j++) {
                            マス tmp = マスリスト[i][j];
                            if (tmp.居駒 == null) {
                                movableCells.add(tmp);
                            }
                        }
                    }
                } else if (this instanceof 香車) {
                    for (int i = 1; i < 10; i++) {
                        for (int j = 2; j < 10; j++) {
                            マス tmp = マスリスト[i][j];
                            if (tmp.居駒 == null) {
                                movableCells.add(tmp);
                            }
                        }
                    }
                } else {
                    for (int i = 1; i < 10; i++) {
                        for (int j = 1; j < 10; j++) {
                            マス tmp = マスリスト[i][j];
                            if (tmp.居駒 == null) {
                                movableCells.add(tmp);
                            }
                        }
                    }
                }

            } else {
                if (後) {
                    // 後
                    if (this.current.get段() + 1 < 10) {
                        マス moveTo = マスリスト[this.current.get筋()][this.current.get段() + 1];
                        movableCells.add(moveTo);
                    }
                }
                if (右後) {
                    // 右後
                    if (this.current.get筋() - 1 > 0 && this.current.get段() + 1 < 10) {
                        マス moveTo = マスリスト[this.current.get筋() - 1][this.current.get段() + 1];
                        movableCells.add(moveTo);
                    }
                }
                if (左後) {
                    // 左後
                    if (this.current.get筋() + 1 < 10 && this.current.get段() + 1 < 10) {
                        マス moveTo = マスリスト[this.current.get筋() + 1][this.current.get段() + 1];
                        movableCells.add(moveTo);
                    }
                }
                if (前) {
                    // 前
                    if (this.current.get段() - 1 > 0) {
                        マス moveTo = マスリスト[this.current.get筋()][this.current.get段() - 1];
                        movableCells.add(moveTo);
                    }
                }
                if (右) {
                    // 右
                    if (this.current.get筋() - 1 > 0) {
                        マス moveTo = マスリスト[this.current.get筋() - 1][this.current.get段()];
                        movableCells.add(moveTo);
                    }
                }
                if (左) {
                    // 左
                    if (this.current.get筋() + 1 < 10) {
                        マス moveTo = マスリスト[this.current.get筋() + 1][this.current.get段()];
                        movableCells.add(moveTo);
                    }
                }
                if (右前) {
                    // 右前
                    if (this.current.get筋() - 1 > 0 && this.current.get段() - 1 > 0) {
                        マス moveTo = マスリスト[this.current.get筋() - 1][this.current.get段() - 1];
                        movableCells.add(moveTo);
                    }
                }
                if (左前) {
                    // 左前
                    if (this.current.get筋() + 1 < 10 && this.current.get段() - 1 > 0) {
                        マス moveTo = マスリスト[this.current.get筋() + 1][this.current.get段() - 1];
                        movableCells.add(moveTo);
                    }
                }
                if (直進後) {
                    // 直進後
                    for (int i = 1; this.current.get段() + i < 10; i++) {
                        マス moveTo = マスリスト[this.current.get筋()][this.current.get段() + i];
                        if (moveTo.先手の駒あり || moveTo.後手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進前) {
                    // 直進前
                    for (int i = 1; this.current.get段() - i > 0; i++) {
                        マス moveTo = マスリスト[this.current.get筋()][this.current.get段() - i];
                        if (moveTo.先手の駒あり || moveTo.後手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進右) {
                    // 直進右
                    for (int i = 1; this.current.get筋() - i > 0; i++) {
                        マス moveTo = マスリスト[this.current.get筋() - i][this.current.get段()];
                        if (moveTo.先手の駒あり || moveTo.後手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進左) {
                    // 直進左
                    for (int i = 1; this.current.get筋() + i < 10; i++) {
                        マス moveTo = マスリスト[this.current.get筋() + i][this.current.get段()];
                        if (moveTo.先手の駒あり || moveTo.後手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進右後) {
                    // 直進右後
                    for (int i = 1; this.current.get筋() - i > 0 && this.current.get段() + i < 10; i++) {
                        マス moveTo = マスリスト[this.current.get筋() - i][this.current.get段() + i];
                        if (moveTo.先手の駒あり || moveTo.後手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進左後) {
                    // 直進左後
                    for (int i = 1; this.current.get筋() + i < 10 && this.current.get段() + i < 10; i++) {
                        マス moveTo = マスリスト[this.current.get筋() + i][this.current.get段() + i];
                        if (moveTo.先手の駒あり || moveTo.後手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進右前) {
                    // 直進右前
                    for (int i = 1; this.current.get筋() - i > 0 && this.current.get段() - i > 0; i++) {
                        マス moveTo = マスリスト[this.current.get筋() - i][this.current.get段() - i];
                        if (moveTo.先手の駒あり || moveTo.後手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進左前) {
                    // 直進左前
                    for (int i = 1; this.current.get筋() + i < 10 && this.current.get段() - i > 0; i++) {
                        マス moveTo = マスリスト[this.current.get筋() + i][this.current.get段() - i];
                        if (moveTo.先手の駒あり || moveTo.後手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (桂馬跳び右) {
                    // 桂馬跳び右
                    if (this.current.get筋() - 1 > 0 && this.current.get段() - 2 > 0) {
                        マス moveTo = マスリスト[this.current.get筋() - 1][this.current.get段() - 2];
                        movableCells.add(moveTo);
                    }
                }
                if (桂馬跳び左) {
                    // 桂馬跳び左
                    if (this.current.get筋() + 1 < 10 && this.current.get段() - 2 > 0) {
                        マス moveTo = マスリスト[this.current.get筋() + 1][this.current.get段() - 2];
                        movableCells.add(moveTo);
                    }
                }
            }
            break;
        case 後手:
            movableCells.clear();
            if (台上) {
                if (this instanceof 歩) {
                    for (int i = 1; i < 10; i++) {
//                        boolean 二歩 = false;
//                        for (int j = 1; j < 10; j++) {
//                            マス tmp = マスリスト[i][j];
//                            if (tmp.居駒 != null) {
//                                if (tmp.後手の駒あり && tmp.居駒 instanceof 歩 && !((歩) tmp.居駒).成り) {
//                                    二歩 = true;
//                                }
//                            }
//                        }
//                        if (二歩) {
//                            continue;
//                        }

                        for (int j = 1; j < 9; j++) {
                            マス tmp = マスリスト[i][j];
                            if (tmp.居駒 == null) {
                                movableCells.add(tmp);
                            }
                        }
                    }
                } else if (this instanceof 桂馬) {
                    for (int i = 1; i < 10; i++) {
                        for (int j = 1; j < 7; j++) {
                            マス tmp = マスリスト[i][j];
                            if (tmp.居駒 == null) {
                                movableCells.add(tmp);
                            }
                        }
                    }
                } else if (this instanceof 香車) {
                    for (int i = 1; i < 10; i++) {
                        for (int j = 1; j < 9; j++) {
                            マス tmp = マスリスト[i][j];
                            if (tmp.居駒 == null) {
                                movableCells.add(tmp);
                            }
                        }
                    }
                } else {
                    for (int i = 1; i < 10; i++) {
                        for (int j = 1; j < 10; j++) {
                            マス tmp = マスリスト[i][j];
                            if (tmp.居駒 == null) {
                                movableCells.add(tmp);
                            }
                        }
                    }
                }

            } else {
                if (後) {
                    // 後
                    if (this.current.get段() - 1 > 0) {
                        マス moveTo = マスリスト[this.current.get筋()][this.current.get段() - 1];
                        movableCells.add(moveTo);
                    }
                }
                if (右後) {
                    // 右後
                    if (this.current.get筋() + 1 < 10 && this.current.get段() - 1 > 0) {
                        マス moveTo = マスリスト[this.current.get筋() + 1][this.current.get段() - 1];
                        movableCells.add(moveTo);
                    }
                }
                if (左後) {
                    // 左後
                    if (this.current.get筋() - 1 > 0 && this.current.get段() - 1 > 0) {
                        マス moveTo = マスリスト[this.current.get筋() - 1][this.current.get段() - 1];
                        movableCells.add(moveTo);
                    }
                }
                if (前) {
                    // 前
                    if (this.current.get段() + 1 < 10) {
                        マス moveTo = マスリスト[this.current.get筋()][this.current.get段() + 1];
                        movableCells.add(moveTo);
                    }
                }
                if (右) {
                    // 右
                    if (this.current.get筋() + 1 < 10) {
                        マス moveTo = マスリスト[this.current.get筋() + 1][this.current.get段()];
                        movableCells.add(moveTo);
                    }
                }
                if (左) {
                    // 左
                    if (this.current.get筋() - 1 > 0) {
                        マス moveTo = マスリスト[this.current.get筋() - 1][this.current.get段()];
                        movableCells.add(moveTo);
                    }
                }
                if (右前) {
                    // 右前
                    if (this.current.get筋() + 1 < 10 && this.current.get段() + 1 < 10) {
                        マス moveTo = マスリスト[this.current.get筋() + 1][this.current.get段() + 1];
                        movableCells.add(moveTo);
                    }
                }
                if (左前) {
                    // 左前
                    if (this.current.get筋() - 1 > 0 && this.current.get段() + 1 < 10) {
                        マス moveTo = マスリスト[this.current.get筋() - 1][this.current.get段() + 1];
                        movableCells.add(moveTo);
                    }
                }
                if (直進後) {
                    // 直進後
                    for (int i = 1; this.current.get段() - i > 0; i++) {
                        マス moveTo = マスリスト[this.current.get筋()][this.current.get段() - i];
                        if (moveTo.後手の駒あり || moveTo.先手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進前) {
                    // 直進前
                    for (int i = 1; this.current.get段() + i < 10; i++) {
                        マス moveTo = マスリスト[this.current.get筋()][this.current.get段() + i];
                        if (moveTo.後手の駒あり || moveTo.先手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進右) {
                    // 直進右
                    for (int i = 1; this.current.get筋() + i < 10; i++) {
                        マス moveTo = マスリスト[this.current.get筋() + i][this.current.get段()];
                        if (moveTo.後手の駒あり || moveTo.先手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進左) {
                    // 直進左
                    for (int i = 1; this.current.get筋() - i > 0; i++) {
                        マス moveTo = マスリスト[this.current.get筋() - i][this.current.get段()];
                        if (moveTo.後手の駒あり || moveTo.先手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進右後) {
                    // 直進右後
                    for (int i = 1; this.current.get筋() + i < 10 && this.current.get段() - i > 0; i++) {
                        マス moveTo = マスリスト[this.current.get筋() + i][this.current.get段() - i];
                        if (moveTo.後手の駒あり || moveTo.先手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進左後) {
                    // 直進左後
                    for (int i = 1; this.current.get筋() - i > 0 && this.current.get段() - i > 0; i++) {
                        マス moveTo = マスリスト[this.current.get筋() - i][this.current.get段() - i];
                        if (moveTo.後手の駒あり || moveTo.先手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進右前) {
                    // 直進右前
                    for (int i = 1; this.current.get筋() + i < 10 && this.current.get段() + i < 10; i++) {
                        マス moveTo = マスリスト[this.current.get筋() + i][this.current.get段() + i];
                        if (moveTo.後手の駒あり || moveTo.先手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (直進左前) {
                    // 直進左前
                    for (int i = 1; this.current.get筋() - i > 0 && this.current.get段() + i < 10; i++) {
                        マス moveTo = マスリスト[this.current.get筋() - i][this.current.get段() + i];
                        if (moveTo.後手の駒あり || moveTo.先手の駒あり) {
                            movableCells.add(moveTo);
                            break;
                        } else {
                            movableCells.add(moveTo);
                        }
                    }
                }
                if (桂馬跳び右) {
                    // 桂馬跳び右
                    if (this.current.get筋() + 1 < 10 && this.current.get段() + 2 < 10) {
                        マス moveTo = マスリスト[this.current.get筋() + 1][this.current.get段() + 2];
                        movableCells.add(moveTo);
                    }
                }
                if (桂馬跳び左) {
                    // 桂馬跳び左
                    if (this.current.get筋() - 1 > 0 && this.current.get段() + 2 < 10) {
                        マス moveTo = マスリスト[this.current.get筋() - 1][this.current.get段() + 2];
                        movableCells.add(moveTo);
                    }
                }
            }
            break;
        }
    }

    public void run() {
        this.計算中 = true;
        評価点 = 0;
        // 移動可能なマス、自分の評価店を計算
        if (実行モード_移動可能マス再計算) {
            resetMovableCells();
        }
        if (実行モード_評価点再計算) {
            for (マス tmp : movableCells) {
                評価点 += tmp.マス点;
                switch (owner) {
                case 先手:
                    評価点 += 盤.先手絶対評価点[tmp.筋][tmp.段];
                    if (/*tmp.後手の駒あり || */tmp.先手の駒あり) {
                        for (マス tmp2 : tmp.居駒.movableCells) {
                            評価点 += tmp2.マス点;
                            評価点 += 盤.先手絶対評価点[tmp2.筋][tmp2.段];
                        }
                    }
                    break;
                case 後手:
                    評価点 += 盤.後手絶対評価点[tmp.筋][tmp.段];
                    if (tmp.後手の駒あり /*|| tmp.先手の駒あり*/) {
                        for (マス tmp2 : tmp.居駒.movableCells) {
                            評価点 += tmp2.マス点;
                            評価点 += 盤.後手絶対評価点[tmp2.筋][tmp2.段];
                        }
                    }
                    break;
                }
            }
        }
        this.計算中 = false;
    }

    @Override
    public String toString() {
        return "";
    }
}
