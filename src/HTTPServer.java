import java.io.*;
import java.net.*;

public class HTTPServer {

    public static void main(String[] args) throws IOException {
        int port = 18080;

        System.out.println("Starting server on port " + port);
        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println("Waiting for a client...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected");

        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());

        String firstLine = reader.readLine();
        System.out.println("Request: " + firstLine);

        if (firstLine != null) {
            String[] requestParts = firstLine.split(" ");

            String method = requestParts[0];
            String path = requestParts[1];

            String headerLine;
            while ((headerLine = reader.readLine()) != null && !headerLine.equals("")) {
                System.out.println(headerLine);
            }

            String body;
            String statusLine;

            if (method.equals("GET")) {
                if (path.equals("/") || path.equals("/index.html")) {
                    statusLine = "HTTP/1.1 200 OK\r\n";
                    body = "<html><body><h1>Welcome</h1><p>This is my page.</p></body></html>";
                } else {
                    statusLine = "HTTP/1.1 400 Bad Request\r\n";
                    body = "<html><body><h1>400 Bad Request</h1><p>Page not found.</p></body></html>";
                }
            } else {
                statusLine = "HTTP/1.1 400 Bad Request\r\n";
                body = "<html><body><h1>400 Bad Request</h1><p>Only GET is supported.</p></body></html>";
            }

            writer.write(statusLine);
            writer.write("Content-Type: text/html\r\n");
            writer.write("Content-Length: " + body.length() + "\r\n");
            writer.write("\r\n");
            writer.write(body);
            writer.flush();
        }

        clientSocket.close();
        serverSocket.close();
    }
}