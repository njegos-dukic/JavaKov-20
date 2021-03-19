package logger;

import utils.Putanje;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger
{
    public static final Logger logger = Logger.getLogger("JavaKov-Logger");
    private static FileHandler logFileHandler;

    static
    {
        try
        {
            Files.deleteIfExists(Path.of(Putanje.LOG_FILE_PATH));
            logFileHandler = new FileHandler(Putanje.LOG_FILE_PATH);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        logger.addHandler(logFileHandler);
    }

    public static void log(Level lv, String error, Object e)
    {
        logger.log(lv, error, e);
    }

    public static void log(Level lv, String error)
    {
        logger.log(lv, error);
    }
}
