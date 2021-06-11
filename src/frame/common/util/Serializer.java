package frame.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {
    public static Object deepCopy(Object o) throws IOException, ClassNotFoundException {

        // オブジェクトを符号化し、バイト配列に書き込み
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);

        // 符号化されたオブジェクトのデータを保持する配列を取得
        byte[] buff = baos.toByteArray();

        // バイト配列から、オブジェクトを複合化
        ByteArrayInputStream bais = new ByteArrayInputStream(buff);
        ObjectInputStream os = new ObjectInputStream(bais);
        Object copy = os.readObject();
        return copy;
        // 例外処理、ストリームのクローズ等は省略
    }

}
