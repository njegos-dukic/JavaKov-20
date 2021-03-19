package utils;

public final class LogDetails
{
    private LogDetails() { }
    public final static String ALARM_RING_EXCEPTION = "Greška pri oglašavanju alarma.";
    public final static String INFO_FILE_EXCEPTION = "IO greška pri upisivanju u info fajl.";
    public final static String TEMPERATURE_UPDATE_EXCEPTION = "Greška pri ažuriranju temperature.";
    public final static String CAR_MOVE_EXCEPTION = "Greška tokom kretanja ambulantnih kola. Kretanje nastavljeno.";
    public final static String NEW_AMBULANCE_WHILE_DES = "Neuspješno kreiranje ambulante. Deserijalizujte simulaciju prije dodavanja nove ambulante.";
    public final static String TOO_MANY_AMBULANCES = "Dodavanje ambulante neuspjesno: Postoje bar 3 slobodne ambulante.";
    public final static String ERROR_NEW_AMBULANCE = "Izuzetak pri dodavanje ambulante. Dodavanje neuspješno.";
    public final static String START_WHILE_DES = "Deserijalizujte prije pokretanja.";
    public final static String VEHICLE_WHILE_DES = "Deserijalizujte prije slanja vozila.";
    public final static String INVALID_DATA = "Neispravno uneseni podaci.";
    public final static String INS_PEOPLE = "Nedovoljno stanovnika uneseno.";
    public final static String INS_HOUSES = "Nedovoljno kuća uneseno.";
    public final static String INS_VEHICLES = "Nedovoljno vozila uneseno.";
    public final static String INS_CONTROL = "Nedovoljno punktova uneseno.";
    public final static String OVF_BUILDINGS = "Uneseno vise od 50 objekata i/ili stanovnika.";
    public final static String CSV_EXCEPTION = "Izuzetak pri upisivanju u CSV fajl.";
    public final static String FW_EXCEPTION = "Izuzetak pri radu File Watchera.";
    public final static String AMBULANCE_INFO_EXC = "Greška pri prikupljanju informacija o ambulantama.";
    public final static String CONTROL_INFO_EXC = "Greška pri prikupljanju informacija o punktovima.";
    public final static String HOUSE_INFO_EXC = "Greška pri prikupljanju informacija o kućama.";
    public final static String PEOPLE_INFO_EXC = "Greška pri prikupljanju informacija o stanovništvu.";
    public final static String SIM_INFO_EXC = "Greška pri prikupljanju informacija o simulaciji.";
    public final static String VEHICLE_INFO_EXC = "Greška pri prikupljanju informacija o kolima.";
    public final static String RACE_CONDITION_CELL = "Pokušaj oslobađanja istog polja od više niti.";
    public final static String NO_VEHICLES = "Razrješavanje alarma pauzirano: Nema slobodnih vozila.";
    public final static String NO_AMBULANCES = "Razrješavanje alarma pauzirano: Nema slobodnih ambulanti.";
    public final static String GUI_EXCEPTION = "Izuzetak pri ažuriranju GUI-ja. Ažuriranje nastavljeno.";
    public final static String ALREADY_SERIALIZED = "Serijalizacija neuspješna: Simulacija trenutno serijalizovana.";
    public final static String SER_EXCEPTION = "Serijalizacija neuspješna: IO Izuzetak pri serijalizaciji.";
    public final static String NOT_SERIALIZED = "Deserijalizacija neuspješna: Simulacija trenutno nije serijalizovana.";
    public final static String DES_EXCEPTION_IO = "Deserijalizacija neuspješna: IO Izuzetak pri deserijalizaciji.";
    public final static String DES_EXCEPTION_CNFE = "Deserijalizacija neuspješna: ClassNotFound izuzetak.";
}
