package nio.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer implements Runnable{

    private static final int PROT = 4450;

    @Override
    public void run() {
        System.out.println("Server start run.....");

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PROT);

            Socket socket = serverSocket.accept();

            System.err.println("Bio Server start.....");

            //读
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = br.readLine();
            System.out.println("from client msg : " + line);

            //写
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println("server send this msg!");
            pw.flush();

//            pw.close();
            br.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
