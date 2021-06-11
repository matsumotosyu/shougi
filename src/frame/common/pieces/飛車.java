package frame.common.pieces;

import frame.common.保持者;
import frame.common.symbol.マス;
import frame.common.symbol.駒;

public class 飛車 extends 駒 {
    public 飛車(マス pos, final 保持者 owner, マス[][] マスリスト) {
        super(pos, owner, マスリスト);
        defaultSet();
    }

    @Override
    public final void 成り() {
        super.前 = false;
        super.後 = false;
        super.右 = false;
        super.左 = false;
        super.右前 = true;
        super.右後 = true;
        super.左前 = true;
        super.左後 = true;
        super.直進前 = true;
        super.直進後 = true;
        super.直進右 = true;
        super.直進左 = true;
        super.直進右前 = false;
        super.直進右後 = false;
        super.直進左前 = false;
        super.直進左後 = false;
        super.桂馬跳び右 = false;
        super.桂馬跳び左 = false;
    }

    @Override
    public final void 駒台へ移動() {
        super.駒台へ移動();
        defaultSet();
    }

    private final void defaultSet() {
        super.前 = false;
        super.後 = false;
        super.右 = false;
        super.左 = false;
        super.右前 = false;
        super.右後 = false;
        super.左前 = false;
        super.左後 = false;
        super.直進前 = true;
        super.直進後 = true;
        super.直進右 = true;
        super.直進左 = true;
        super.直進右前 = false;
        super.直進右後 = false;
        super.直進左前 = false;
        super.直進左後 = false;
        super.桂馬跳び右 = false;
        super.桂馬跳び左 = false;
    }

    @Override
    public final String toString() {
        if (成り)
            return " 竜";
        return " 飛";
    }
}
