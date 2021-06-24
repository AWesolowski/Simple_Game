import java.net.*;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.*;

/**
 * Klasa odpowiedzialna za przejscia zadania obslugi klienta w ramach serwera - dzieki temu z serwerem moze komunikowac sie kilka klientow
 *
 * @author KH AW
 */

public class ServerThread extends Thread {

    /// gniazdo klienta
    protected Socket socket;

    /// bufory wejscia, wyjscia tekstowe
    String fromClient = "";
    String odpowiedz = "";
    BufferedReader br = null;
    PrintWriter pw = null;
    Server_Parser parametry;

    /**
     * Konstruktor klasy ServerThread
     *
     * @param clientSocket gniazdo klienta
     * @param _parametry   lista parametrow programu
     */
    public ServerThread(Socket clientSocket, Server_Parser _parametry) {
        this.socket = clientSocket;
        this.parametry = _parametry;
    }

    /**
     * Metoda uruchamiajaca watek
     */
    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream());

        } catch (IOException e) {
            System.out.println("Wystapil blad polaczenia");
            return;
        }

        try {
            /// wysluchanie polecenia od klienta
            fromClient = br.readLine();

            /// wysluchiwanie nadchodzacych polecen od klienta dopoki nie zakonczy polaczenia przez quit
            while (fromClient.compareTo("quit") != 0) {
                /// reakcja na zdanie protokolu
                if (fromClient.equals("getScores"))
                {
                    odpowiedz = Integer.toString(parametry.geTsco());
                }
                else if (fromClient.equals("getlvlscore"))
                {
                    odpowiedz = Integer.toString(parametry.geTsco());
                }
                else if (fromClient.equals("getScores"))
                {
                    odpowiedz = Integer.toString(parametry.geTsco());
                }
                else if (fromClient.equals("getScores"))
                {
                    odpowiedz = Integer.toString(parametry.geTsco());
                }
                else if (fromClient.equals("getScores"))
                {
                    odpowiedz = Integer.toString(parametry.geTsco());
                }


                if (fromClient.equals("getLiczbaPoziomow")) {
                    odpowiedz = Integer.toString(parametry.getLiczbaPoziomow());
                } else if (fromClient.equals("getStartNum")) {
                    odpowiedz = Integer.toString(parametry.getNumeracjaPoziomowZaczynaSieOd());
                } else if (fromClient.equals("getLiczbaStopniTrudnosci")) {
                    odpowiedz = Integer.toString(parametry.getLiczbaStopniTrudnosci());
                } else if (fromClient.equals("getFigura")) {
                    odpowiedz = parametry.getFiguraObiektuGry();
                } else if (fromClient.equals("getPlikTla")) {
                    odpowiedz = parametry.getPlikTla();
                } else if (fromClient.equals("getPlikObiektu")) {
                    odpowiedz = parametry.getPlikObiektu();
                } else if (fromClient.equals("jakaZmianaStopniaTrudnosci")) {
                    odpowiedz = Integer.toString(parametry.getZmianaStopniaTrudnosci());
                } else if (fromClient.equals("getSzerokosc")) {
                    odpowiedz = Integer.toString(parametry.getPoczatkowaSzerokoscPlanszy());
                } else if (fromClient.equals("getProcent")) {
                    odpowiedz = Double.toString(parametry.getPoczatkowaSzerokoscObiektuGryJakoProcentPoczatkowejSzerokosciPlanszy());
                } else if (fromClient.equals("getNazwa")) {
                    odpowiedz = parametry.getNazwaGry();
                } else if (fromClient.equals("getNazwaOpisu")) {
                    odpowiedz = parametry.getNazwaBazowaPlikuZOpisemPoziomu();
                } else if (fromClient.contains("newScore")) {
                    try {///Zapisz do pliku (ranking) - dodaj nowa linie zwierajaca ostatni wynik
                        Files.write(Paths.get("./Highscores"), (fromClient.substring(14) + "=" + fromClient.substring(9, 13) + "\n").getBytes(), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        //wyjatek - obsluga
                    }
                    odpowiedz = "Zapisano pomyslnie.";
                } else if (fromClient.equals("ileRekordow")) {
                    int rekordy = 0;
                    String line = "";
                    try {
                        /// otworz plik z lista wynikow
                        BufferedReader r = new BufferedReader(new FileReader("./Highscores"));
                        /// zlicz wszystkie linie

                        while ((line = r.readLine()) != null) {
                            rekordy++;
                        }
                        /// obsluz wyjatki
                    } catch (FileNotFoundException fnfe) {
                        System.out.println("Nie znaleziono pliku");
                    } catch (IOException ioe) {
                        System.out.println("WystĂ„â€¦pil bÄąâ€šĂ„â€¦d odczytu pliku");
                    }
                    odpowiedz = Integer.toString(rekordy);
                } else if (fromClient.contains("wczytajWynik")) {
                    String line = "";
                    try {
                        /// otworz plik z lista wynikow
                        BufferedReader r = new BufferedReader(new FileReader("Highscores"));

                        int i = 0;
                        while (i < Integer.parseInt(fromClient.substring(13))) {
                            line = r.readLine();
                            i++;
                        }
                        /// obsloz wyjatki
                    } catch (FileNotFoundException fnfe) {
                        System.out.println("Nie znaleziono pliku");
                    } catch (IOException ioe) {
                        System.out.println("Wystapil blad odczytu pliku");
                    }
                    odpowiedz = line;
                }
                /// wyslij odpowiedz do klienta
                pw.println(odpowiedz);
                pw.flush();
                if (odpowiedz != null) {
                    System.out.println("Wyslano odpowiedz: " + odpowiedz + " do klienta " + socket.getRemoteSocketAddress().toString());
                }
                fromClient = br.readLine();
            }///przechwyt wyjatkow
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            try {
                /// czynnosci koncowe przed zamknieciem socketa
                System.out.println("Zamykanie polaczenia...");
                if (br != null) {
                    br.close();
                    System.out.println("Bufor wejsciowy zamkniety");
                }
                if (pw != null) {
                    pw.close();
                    System.out.println("Bufor wyjsciowy zamkniety");
                }
                if (socket != null) {
                    br.close();
                    System.out.println("Socket zamkniety");
                }
            } catch (IOException ie) {
                System.out.println("Wystapil blad podczas zamykania gniazda");
            }
        }
    }

}

