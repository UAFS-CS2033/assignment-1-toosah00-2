import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.File;
import java.io.FileReader;



public class Server{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int portNo;

    public Server(int portNo){
        this.portNo=portNo;
    }

    void requestFullfillment(String filePath , String contentType , PrintWriter out) throws IOException{
        File file = new File(filePath);
        int contentLength = (int) file.length();

        out.printf("HTTP/1.1 200 OK\n");
        out.printf("Content-Length: %d\n", contentLength);
        out.printf("Content-Type: %s\n\n", contentType);

        System.out.printf("HTTP/1.1 200 OK\n");
        System.out.printf("Content-Length: %d\n", contentLength);
        System.out.printf("Content-Type: %s\n\n", contentType);



        if(!(contentType.equals("image/png"))){
            BufferedReader br = new BufferedReader(new FileReader (file));
            String buffer = br.readLine();
            while(buffer != null){
                out.println(buffer);
                System.out.println(buffer);
                buffer = br.readLine();
            } 
            br.close();
        } else {
            // ?
            
        }
    }

    private void processConnection() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
    
        //*** Application Protocol *****
        String buffer = in.readLine();
        if(buffer.equals("GET /home.html HTTP/1.1")){
            requestFullfillment("docroot/home.html" , "text/html", out);
        } else if(buffer.equals("GET /scripts/style.css HTTP/1.1")){
            requestFullfillment("docroot/scripts/style.css" , "text/css", out);
        } else if (buffer.equals("GET /images/assign2-screen.png HTTP/1.1")){
            //requestFullfillment("docroot/images/assign2-screen.png" , "image/png" , out );
        }    
        
        in.close();
        out.close();

    }

    public void run() throws IOException{
        boolean running = true;
       
        serverSocket = new ServerSocket(portNo);
        System.out.printf("Listen on Port: %d\n",portNo);
        while(running){
            clientSocket = serverSocket.accept();
            //** Application Protocol
            processConnection();
            clientSocket.close();
        }
        serverSocket.close();
    }
    public static void main(String[] args0) throws IOException{
        Server server = new Server(8080);
        server.run();
    }
}
