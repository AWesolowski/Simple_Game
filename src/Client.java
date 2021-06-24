import java.net.*;
import java.io.*;


/**
 * Klient do realizacji protokolu sieciowego
 *
 * @author SJ RK
 * @version 1.0.0  26 maja 2019 21:30
 */
public class Client {

    Socket socket = null;
    OutputStream os = null;
    PrintWriter pw = null;
    InputStream is = null;
    BufferedReader br = null;


    public Client()
    {
    }

    public boolean otworzPolaczenie(String _host, int _port)
    {
        try {
            /// otworz polaczenie
            socket = new Socket(_host,_port);
            return true;
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            return false;
        }
    }

    public String wyslijPolecenie(String polecenie)
    {
        String odpowiedz = "";
        try {
            /// strumienie wyjscia
            os = socket.getOutputStream();
            pw = new PrintWriter(os, true);
            /// wyslij polecenie do serwera
            pw.println(polecenie);
            pw.flush();
            ///poczekaj na odpowiedz
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            /// odbierz informacje zwrotna, Wypisz w oknie terminala wartosc odebrana z serwera
            odpowiedz = br.readLine();
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
        }
        if(odpowiedz!=null){System.out.println("Otrzymano wynik: " + polecenie+ " -> " + odpowiedz);}
        return odpowiedz;

    }

    public void zamknij(){
        try{
            System.out.println("Zakonczenie polaczenia");
            is.close();
            os.close();
            br.close();
            pw.close();
            socket.close();
        }catch (Exception e) {
            System.err.println("Client exception: " + e);
        }
    }

}
