package simulacija;

import objekti.Ambulanta;
import objekti.KontrolniPunkt;
import objekti.Kuca;
import osobe.Stanovnik;
import vozila.AmbulantnaKola;

public class Info
{
    public static void dodajInfo(String filename)
    {
        Simulacija.dohvatiSimulaciju().info(filename);

        for (Stanovnik s : Simulacija.dohvatiSimulaciju().getStanovnici())
            s.info(filename);

        for (Kuca k : Simulacija.dohvatiSimulaciju().getKuce())
            k.info(filename);

        for (Ambulanta a : Simulacija.dohvatiSimulaciju().getAmbulante())
            a.info(filename);

        for (KontrolniPunkt p : Simulacija.dohvatiSimulaciju().getPunktovi())
            p.info(filename);

        for (AmbulantnaKola ak : Simulacija.dohvatiSimulaciju().getAmbulantnaKola())
            ak.info(filename);
    }

//    public static void deserializujIzInfo()
//    {
//        if (!serialized)
//            return;
//
//        try
//        {
//            Path path = Paths.get(Putanje.PUTANJA_DO_SERIALIZATION);
//            BufferedReader bufferedReader = Files.newBufferedReader(path);
//
//            String curLine;
//            while ((curLine = bufferedReader.readLine()) != null)
//            {
//                if (curLine.equals("Simulacija:"))
//                {
//                    int brojStanovnika = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    Simulacija.setBrojStanovinka(brojStanovnika);
//
//                    int velicinaGrada = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    Simulacija.kreirajSimulaciju().setVelicinaGrada(velicinaGrada);
//
//                    FileWatcher.upisiUFajl(0, 0);
//
//                    int brojZarazenih = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    for (int i = 0; i < brojZarazenih; i++)
//                        Simulacija.incrementTrenutniBrojZarazenih();
//
//                    int brojIzlijecenih = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    for (int i = 0; i < brojIzlijecenih; i++)
//                        Simulacija.incrementBrojIzlijecenih();
//
//                    LocalDateTime vrijemePocetka = LocalDateTime.parse(bufferedReader.readLine().split(": ")[1]);
//                    Simulacija.postaviVrijemePocetka(vrijemePocetka);
//                }
//
//                else if (curLine.equals("Kuca:"))
//                {
//                    int lokacijaKuceX = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int lokacijaKuceY = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int IDKuce = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int brojUkucana = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int brojacKuca = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//
//                    Kuca k = new Kuca(lokacijaKuceX, lokacijaKuceY, IDKuce, brojUkucana);
//                }
//
//                else if (curLine.equals("Ambulanta:"))
//                {
//                    int lokacijaAmbulanteX = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int lokacijaAmbulanteY = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int kapacitet = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int brojacAmbulanti = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//
//                    Ambulanta a = new Ambulanta(lokacijaAmbulanteX, lokacijaAmbulanteY, kapacitet, brojacAmbulanti);
//                }
//
//                else if (curLine.equals("Punkt:"))
//                {
//                    int lokacijaPunktaX = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int lokacijaPunktaY = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//
//                    KontrolniPunkt kp = new KontrolniPunkt(lokacijaPunktaX, lokacijaPunktaY, 1);
//                }
//
//                else if (curLine.equals("Kola:"))
//                {
//                    int lokacijaX = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int lokacijaY = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int lokacijaAmbulanteX = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int lokacijaAmbulanteY = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int lokacijaAmbulante = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//
//                    Ambulanta amb = null;
//                    for (Ambulanta a : Simulacija.kreirajSimulaciju().getAmbulante())
//                        if (lokacijaAmbulante == a.getLokacija())
//                            amb = a;
//
//                    String putanja = bufferedReader.readLine().split(": ")[1];
//
//                    ArrayList<Integer> put = new ArrayList<>();
//                    if (putanja.length() > 2)
//                    {
//                        putanja = putanja.substring(1, putanja.length() - 1);
//                        putanja = putanja.replace(" ", "");
//                        String[] pa = putanja.split(",");
//
//                        for (String value : pa)
//                            put.add(Integer.parseInt(value));
//                    }
//
//                    boolean uPutu = Boolean.parseBoolean(bufferedReader.readLine().split(": ")[1]);
//                    boolean aktivan = Boolean.parseBoolean(bufferedReader.readLine().split(": ")[1]);
//                    String trenutnoPrevozimoStanovnika = bufferedReader.readLine().split(": ")[1];
//
//                    Stanovnik trenutnoPrevozimo = null;
//                    if (!trenutnoPrevozimoStanovnika.equals("null"))
//                        for (Stanovnik s : Simulacija.kreirajSimulaciju().getStanovnici())
//                            if (s.getID() == Integer.parseInt(trenutnoPrevozimoStanovnika))
//                                trenutnoPrevozimo = s;
//
//                    AmbulantnaKola a = new AmbulantnaKola(lokacijaX, lokacijaY, lokacijaAmbulanteX, lokacijaAmbulanteY, amb, put, uPutu, aktivan, trenutnoPrevozimo);
//                }
//
//                else if (curLine.equals("Stanovnik:"))
//                {
//                    Stanovnik s = null;
//
//                    int ID = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//
//                    if (ID < 400)
//                        s = new Dijete();
//                    else if (ID > 400 && ID < 700)
//                        s = new Odrasli();
//                    else if (ID > 700)
//                        s = new Stari();
//
//                    String ime = bufferedReader.readLine().split(": ")[1];
//                    String prezime = bufferedReader.readLine().split(": ")[1];
//                    int godine = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int godinaRodjenja = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    String pol = bufferedReader.readLine().split(": ")[1];
//                    int idKuce = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int lokacijaKuceX = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int lokacijaKuceY = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    double temperatura = Double.parseDouble(bufferedReader.readLine().split(": ")[1]);
//                    int radijusKretanja = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int radijusGore = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int radijusDole = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int radijusLijevo = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int radijusDesno = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int lokacijaX = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int lokacijaY = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    boolean kretanjeOmoguceno = Boolean.parseBoolean(bufferedReader.readLine().split(": ")[1]);
//                    String smjerGUI = bufferedReader.readLine().split(": ")[1];
//                    String putanja = bufferedReader.readLine().split(": ")[1];
//                    boolean zarazen = Boolean.parseBoolean(bufferedReader.readLine().split(": ")[1]);
//                    boolean potencijalan = Boolean.parseBoolean(bufferedReader.readLine().split(": ")[1]);
//                    boolean aktivan = Boolean.parseBoolean(bufferedReader.readLine().split(": ")[1]);
//                    boolean izlijecenNaPutuDoKuce = Boolean.parseBoolean(bufferedReader.readLine().split(": ")[1]);
//                    String poslednjeTemperature = bufferedReader.readLine().split(": ")[1];
//                    int lokacijaTrenutneAmbulante = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//                    int brojacNeuspjesnih = Integer.parseInt(bufferedReader.readLine().split(": ")[1]);
//
//                    ArrayList<Integer> put = new ArrayList<>();
//                    if (putanja.length() > 2)
//                    {
//                        putanja = putanja.substring(1, putanja.length() - 1);
//                        putanja = putanja.replace(" ", "");
//                        String[] pa = putanja.split(",");
//
//                        for (String value : pa)
//                            put.add(Integer.parseInt(value));
//                    }
//
//                    ArrayList<Double> temp = new ArrayList<>();
//                    if (poslednjeTemperature.length() > 2)
//                    {
//                        poslednjeTemperature = poslednjeTemperature.substring(1, poslednjeTemperature.length() - 1);
//                        poslednjeTemperature = poslednjeTemperature.replace(" ", "");
//                        String[] pa = poslednjeTemperature.split(",");
//
//                        for (String value : pa)
//                            temp.add(Double.parseDouble(value));
//                    }
//
//                    Ambulanta amb = null;
//                    for (Ambulanta a : Simulacija.kreirajSimulaciju().getAmbulante())
//                        if (a.getLokacija() == lokacijaTrenutneAmbulante)
//                            amb = a;
//
//                    s.setAll(ID, ime, prezime, godine, godinaRodjenja, pol,
//                            idKuce, lokacijaKuceX, lokacijaKuceY, temperatura,
//                            radijusKretanja, radijusGore, radijusDole, radijusLijevo,
//                            radijusDesno, lokacijaX, lokacijaY, kretanjeOmoguceno, smjerGUI,
//                            put, zarazen, potencijalan, aktivan, izlijecenNaPutuDoKuce,
//                            temp, amb, brojacNeuspjesnih);
//                }
//            }
//
//            for (Kuca k : Simulacija.kreirajSimulaciju().getKuce())
//                Simulacija.kreirajSimulaciju().getLokacijeObjekata().add(new OrderedPair(k.getLokacijaX(), k.getLokacijaY(), TipElementa.OBJEKAT));
//
//            for (Ambulanta a : Simulacija.kreirajSimulaciju().getAmbulante())
//                Simulacija.kreirajSimulaciju().getLokacijeObjekata().add(new OrderedPair(a.getLokacijaX(), a.getLokacijaY(), TipElementa.OBJEKAT));
//
//            for (KontrolniPunkt kp : Simulacija.kreirajSimulaciju().getPunktovi())
//                Simulacija.kreirajSimulaciju().getLokacijeObjekata().add(new OrderedPair(kp.getLokacijaX(), kp.getLokacijaY(), TipElementa.OBJEKAT));
//
//            bufferedReader.close();
//            Alarm.pokreni();
//            serialized = false;
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            MyLogger.log(Level.INFO, "Neuspjesna deserijalizacija.");
//        }
//    }
}
