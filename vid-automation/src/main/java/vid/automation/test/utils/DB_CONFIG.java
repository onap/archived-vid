package vid.automation.test.utils;

public class DB_CONFIG {
    public static String url = String.format("jdbc:mariadb://%s:%d/vid_portal",
            System.getProperty("DB_HOST", System.getProperty("VID_HOST", "10.0.0.10" )),
            Integer.valueOf(System.getProperty("DB_PORT", "3306"))
    );
    public static String username = "euser";
    public static String password = "euser";
}
