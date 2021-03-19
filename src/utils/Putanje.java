package utils;

import java.io.File;

public final class Putanje
{
    private Putanje() { }
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String APP_FOLDER = PROJECT_PATH + File.separatorChar + "app";
    public static final String INFO_FILE_BASENAME = "Simulacija.info";
    public static final String INFO_FILE_PATH = APP_FOLDER + File.separatorChar + INFO_FILE_BASENAME;
    public static final String LOG_FILE_BASENAME = "JavaKov-20.log";
    public static final String LOG_FILE_PATH = APP_FOLDER + File.separatorChar + LOG_FILE_BASENAME;
    public static final String CSV_FILE_BASENAME = "Statistika.csv";
    public static final String PUTANJA_DO_CSV = APP_FOLDER + File.separatorChar + CSV_FILE_BASENAME;
    public static final String SERIALIZATION_FILE_BASENAME = "Serijalizacija.ser";
    public static final String PUTANJA_DO_SERIALIZATION = APP_FOLDER + File.separatorChar + SERIALIZATION_FILE_BASENAME;
}

