package test;

public class EtcTest {

    public static void main(String[] args) {
        // TODO 自動生成されたメソッド・スタブ
        System.out.println("歩3, 3, 3, 4 false".split(", ")[0].substring(1));
        読み(3);
    }

    private static void 読み(int 深度) {
        if (深度 - 1 == 0) {
            // 計算;
            System.out.println("計算します。");
        } else {
            System.out.println("準備します。");
            読み(深度 - 1);
        }
    }
}
