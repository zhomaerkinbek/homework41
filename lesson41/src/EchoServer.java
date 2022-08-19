import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {

    private final int port;

    private EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        try (var server = new ServerSocket(port)) {
            // обработка подключения
            System.out.println("Сервер запущен!");
            try(var clientSocket = server.accept()) {
                handle(clientSocket);
            }
        } catch (IOException e) {
            System.out.printf("Вероятнее всего порт %s занят.%n", port);
            e.printStackTrace();
        }
    }

    private void handle(Socket clientsocket) throws IOException {
        // логика обработки


        try(Scanner in = new Scanner(new InputStreamReader(clientsocket.getInputStream(), "UTF-8"));
            PrintWriter out = new PrintWriter(clientsocket.getOutputStream())){
            while (true) {
                String message = in.nextLine().strip();
                System.out.printf("Got: %s%n", message);

                if ("bye".equalsIgnoreCase(message)) {
                    out.write("Bye bye!");
                    out.write(System.lineSeparator());

                    out.flush();
                    return;
                }
                out.write(reverseString(message));
                out.write(System.lineSeparator());

                out.flush();


            }
        } catch (NoSuchElementException e){
            System.out.println("Client dropped the connection!");
        }
    }

    public static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }
}
