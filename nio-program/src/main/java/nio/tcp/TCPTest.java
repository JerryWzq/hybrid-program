package nio.tcp;

public class TCPTest {

    public void tcpServer(){

        Thread t = new Thread(new TCPServer());
        t.start();

        while(true){
            try {
                Thread.sleep(3*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void tcpClient(){

    }
}
