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
                if (fromClient.equals("get_speed_boost"))
                {
                    odpowiedz = Integer.toString(parametry.getSpeed_boost());
                }
                else if (fromClient.equals("get_collisions_available"))
                {
                    odpowiedz = Integer.toString(parametry.getCollisions_available());
                }
                else if (fromClient.equals("get_speed_parameter"))
                {
                    odpowiedz = Double.toString(parametry.getSpeed_parameter());
                }
                else if (fromClient.equals("get_number_of_levels"))
                {
                    odpowiedz = Integer.toString(parametry.getNumber_of_levels());
                }
                else if (fromClient.equals("get_difficulty_change"))
                {
                    odpowiedz = Double.toString(parametry.getDifficulty_change());
                }
                else if (fromClient.equals("get_starting_object_size"))
                {
                    odpowiedz = Double.toString(parametry.getStarting_object_size());
                }
                else if (fromClient.equals("get_starting_number_of_enemies"))
                {
                    odpowiedz = Integer.toString(parametry.getNumber_of_enemies());
                }/////
                else if (fromClient.contains("newScore")) {
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

