package cat.babot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Util {

    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        String[] dataBullet = sdf.format(new Date()).split(" ");
        String date = dataBullet[0] + " " + dataBullet[1];
        String timeZone= dataBullet[2];
        return date + " UTC " + timeZone;
    }
}
