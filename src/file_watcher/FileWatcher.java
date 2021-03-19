package file_watcher;

import GUI.StartScreen.PocetniEkranController;
import javafx.application.Platform;
import logger.MyLogger;
import utils.LogDetails;
import utils.Putanje;
import java.io.FileWriter;
import java.nio.file.*;
import java.util.logging.Level;

public class FileWatcher extends Thread
{
    private static FileWatcher posmatrac = null;
    private static boolean posmatranjeAktivno = false;

    private FileWatcher() { }

    @Override
    public void run()
    {
        while(posmatranjeAktivno)
            watchingU();
    }

    private void watchingU()
    {
        try
        {
            Path putanjaDoFoldera = Paths.get(Putanje.APP_FOLDER);
            Path putanjaDoFajla = Paths.get(Putanje.INFO_FILE_PATH);

            if (Files.notExists(putanjaDoFajla))
                Files.writeString(putanjaDoFajla, "0;0");

            WatchService watcher = FileSystems.getDefault().newWatchService();
            WatchKey kljuc;
            putanjaDoFoldera.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

            while ((kljuc = watcher.take()) != null)
            {
                for (WatchEvent<?> dogadjaj : kljuc.pollEvents())
                    if (dogadjaj.context().toString().equals(Putanje.INFO_FILE_BASENAME))
                    {
                        String[] informacije = Files.readString(putanjaDoFajla).split(";");
                        Platform.runLater(() ->
                        {
                            if (informacije.length >= 2)
                            {
                                PocetniEkranController.getMainKontroler().setBrojZarazenih(informacije[0]);
                                PocetniEkranController.getMainKontroler().setBrojIzlijecenih(informacije[1]);
                            }
                        });
                    }

                kljuc.reset();
            }
        }
        catch (Exception e)
        {
            MyLogger.log(Level.INFO, LogDetails.FW_EXCEPTION);

        }
    }

    public static FileWatcher kreirajPosmatranje()
    {
        upisiUFajl(0, 0);

        if (posmatrac == null)
            posmatrac = new FileWatcher();

        posmatranjeAktivno = true;
        return posmatrac;
    }

    public static void zaustaviPosmatranje()
    {
        posmatranjeAktivno = false;
    }

    public static void upisiUFajl(int zarazeni, int izlijeceni)
    {
        try
        {
            FileWriter infoFile = new FileWriter(Putanje.INFO_FILE_PATH);
            infoFile.append(String.valueOf(zarazeni) + ';' + izlijeceni);
            infoFile.flush();
            infoFile.close();
        }
        catch (Exception e)
        {
            MyLogger.log(Level.INFO, LogDetails.INFO_FILE_EXCEPTION);
        }

    }
}
