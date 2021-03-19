package GUI.Statistics;

import enumi.Pol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import logger.MyLogger;
import osobe.Stanovnik;
import simulacija.Simulacija;
import utils.LogDetails;
import utils.Putanje;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class StatistikaController
{
    @FXML
    private TableView<Entry> tabela;

    @FXML
    private Button exportuj;

    @FXML
    private Button zavrsi;

    private Entry djeca;
    private Entry odrasli;
    private Entry stari;
    private Entry ukupno;

    @FXML
    void export(ActionEvent event)
    {
        try
        {
            Files.deleteIfExists(Path.of(Putanje.PUTANJA_DO_CSV));
            FileWriter csvWriter = new FileWriter(Putanje.PUTANJA_DO_CSV);
            csvWriter.append("Tip,Zarazeni,Muskarci,Zene" + System.lineSeparator());
            csvWriter.append(djeca.getTip() + "," + djeca.getBrojZarazenih() + "," + djeca.getBrojMuskaraca() + "," + djeca.getBrojZena() + System.lineSeparator());
            csvWriter.append(odrasli.getTip() + "," + odrasli.getBrojZarazenih() + "," + odrasli.getBrojMuskaraca() + "," + odrasli.getBrojZena() + System.lineSeparator());
            csvWriter.append(stari.getTip() + "," + stari.getBrojZarazenih() + "," + stari.getBrojMuskaraca() + "," + stari.getBrojZena() + System.lineSeparator());
            csvWriter.append(ukupno.getTip() + "," + ukupno.getBrojZarazenih() + "," + ukupno.getBrojMuskaraca() + "," + ukupno.getBrojZena() + System.lineSeparator());
            csvWriter.flush();
            csvWriter.close();
        }
        catch (Exception e)
        {
            MyLogger.log(Level.INFO, LogDetails.CSV_EXCEPTION);
        }
    }

    @FXML
    void kraj(ActionEvent event) { ((Button) event.getSource()).getScene().getWindow().hide(); }

    public void popuniInfo()
    {
        int brojacZarazenihDjecaka = 0;
        int brojacZarazenihDjevojcica = 0;
        int brojacZarazenihMuskaraca = 0;
        int brojacZarazenihZena = 0;
        int brojacZarazenihBaka = 0;
        int brojacZarazenihDeka = 0;

        for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
        {
            if (s.getZarazen())
            {
                if (s.getPol() == Pol.MUSKI)
                {
                    if (s.getID() >= 100 && s.getID() < 400)
                        brojacZarazenihDjecaka++;

                    else if (s.getID() >= 400 && s.getID() < 700)
                        brojacZarazenihMuskaraca++;

                    else if (s.getID() >= 700)
                        brojacZarazenihDeka++;
                }

                else
                {
                    if (s.getID() >= 100 && s.getID() < 400)
                        brojacZarazenihDjevojcica++;

                    else if (s.getID() >= 400 && s.getID() < 700)
                        brojacZarazenihZena++;

                    else if (s.getID() >= 700)
                        brojacZarazenihBaka++;
                }
            }
        }

        tabela.getItems().clear();

        djeca = new Entry("Djeca", brojacZarazenihDjecaka, brojacZarazenihDjevojcica);
        odrasli = new Entry("Odrasli", brojacZarazenihMuskaraca, brojacZarazenihZena);
        stari = new Entry("Stari", brojacZarazenihDeka, brojacZarazenihBaka);
        ukupno = new Entry("Ukupno", brojacZarazenihDjecaka + brojacZarazenihMuskaraca + brojacZarazenihDeka, brojacZarazenihDjevojcica + brojacZarazenihZena + brojacZarazenihBaka);

        TableColumn tipColumn = new TableColumn("Tip");
        tipColumn.setCellValueFactory(new PropertyValueFactory<>("tip"));

        TableColumn zarazeniColumn = new TableColumn("Zaraženi");
        zarazeniColumn.setCellValueFactory(new PropertyValueFactory<>("brojZarazenih"));

        TableColumn muskarciColumn = new TableColumn("Muškarci");
        muskarciColumn.setCellValueFactory(new PropertyValueFactory<>("brojMuskaraca"));

        TableColumn zeneColumn = new TableColumn("Žene");
        zeneColumn.setCellValueFactory(new PropertyValueFactory<>("brojZena"));

        tabela.getColumns().addAll(tipColumn, zarazeniColumn, muskarciColumn, zeneColumn);

        tabela.getItems().add(djeca);
        tabela.getItems().add(odrasli);
        tabela.getItems().add(stari);
        tabela.getItems().add(ukupno);
    }
}
