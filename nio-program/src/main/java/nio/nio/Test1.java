package nio.nio;

import org.junit.Test;

public class Test1 {

    @Test
    public void nIOServer(){

        Thread t = new Thread(new Server());
        t.start();
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void nIOClient(){
        Client client = new Client("10.118.48.68", 1900);
//        try {
//            Thread.sleep(2000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
