package frame.common.util;

import frame.common.symbol.マス;
import frame.common.symbol.駒;

public class Grapher {
    public void graphAll(マス[][] マスリスト) {
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                駒 tmp = マスリスト[i][j].居駒;
                if (tmp != null) {
                    this.graph(tmp, マスリスト);
                }
            }
        }
    }

    public void graph(駒 piece, マス[][] マスリスト) {
        System.out.println(piece.owner + ":" + piece.toString() + ":評価値=" + piece.評価点);
        StringBuilder sb = new StringBuilder();
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 91| 81| 71| 61| 51| 41| 31| 21| 11|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 92| 82| 72| 62| 52| 42| 32| 22| 12|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 93| 83| 73| 63| 53| 43| 33| 23| 13|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 94| 84| 74| 64| 54| 44| 34| 24| 14|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 95| 85| 75| 65| 55| 45| 35| 25| 15|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 96| 86| 76| 66| 56| 46| 36| 26| 16|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 97| 87| 77| 67| 57| 47| 37| 27| 17|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 98| 88| 78| 68| 58| 48| 38| 28| 18|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 99| 89| 79| 69| 59| 49| 39| 29| 19|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        String template = sb.toString();
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                String replaceTgt = " " + tmp.get筋() + tmp.get段();
                if (tmp.居駒 != null) {
                    template = template.replaceAll(replaceTgt, tmp.居駒.toString());
                }
            }
        }
        for (マス tmp : piece.movableCells) {
            String replaceTgt = " " + tmp.get筋() + tmp.get段();
            if (tmp.居駒 == null) {
                template = template.replaceAll(replaceTgt, " * ");
            }
        }
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                String replaceTgt = " " + tmp.get筋() + tmp.get段();
                if (tmp.居駒 == null) {
                    template = template.replaceAll(replaceTgt, "   ");
                }
            }
        }
        System.out.println(template);

    }

    public void graphNow(マス[][] マスリスト) {
        StringBuilder sb = new StringBuilder();
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 91| 81| 71| 61| 51| 41| 31| 21| 11|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 92| 82| 72| 62| 52| 42| 32| 22| 12|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 93| 83| 73| 63| 53| 43| 33| 23| 13|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 94| 84| 74| 64| 54| 44| 34| 24| 14|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 95| 85| 75| 65| 55| 45| 35| 25| 15|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 96| 86| 76| 66| 56| 46| 36| 26| 16|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 97| 87| 77| 67| 57| 47| 37| 27| 17|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 98| 88| 78| 68| 58| 48| 38| 28| 18|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        sb.append("| 99| 89| 79| 69| 59| 49| 39| 29| 19|" + "\r\n");
        sb.append("+---+---+---+---+---+---+---+---+---+" + "\r\n");
        String template = sb.toString();
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                String replaceTgt = " " + tmp.get筋() + tmp.get段();
                if (tmp.居駒 != null) {
                    template = template.replaceAll(replaceTgt, tmp.居駒.toString());
                }
            }
        }
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                String replaceTgt = " " + tmp.get筋() + tmp.get段();
                if (tmp.居駒 == null) {
                    template = template.replaceAll(replaceTgt, "   ");
                }
            }
        }
        System.out.println(template);

        template = sb.toString();
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                マス tmp = マスリスト[i][j];
                String replaceTgt = " " + tmp.get筋() + tmp.get段();
                template = template.replaceAll(replaceTgt, " "+String.valueOf(tmp.マス点));
            }
        }
        System.out.println(template);
    }
}
