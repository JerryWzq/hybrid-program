package nio.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest {

    public void bioServer(){
        Thread t = new Thread(new BioServer());
        t.start();
        while(true){
            try {
                Thread.sleep(3*1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void bioClient(){
        try {
            Socket socket = new Socket("10.118.48.68", 4450);
            System.err.println("连接成功。。。");

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println("client send this msg!!!");
            pw.flush();

//            while(true){
//                String line = br.readLine();
//                System.out.println("This is client, msg from server : " + line);
//                try {
//                    Thread.sleep(2*1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

            //
            byte[] bytes = new byte[1024];

            while(true){
                int read = socket.getInputStream().read(bytes);
                System.out.println("data : " + new String(bytes) + " length: " + read);
                try {
                    Thread.sleep(2*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nioServer(){
        Thread t = new Thread(new Reactor());
        t.start();
        while (true){
            try {
                Thread.sleep(1*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void nioClient(){
        Thread t = new Thread(new NioClient());
        t.start();
    }


}
