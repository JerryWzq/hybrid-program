package nio.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ClientReadThread implements Runnable{

    private Selector selector;

    public ClientReadThread(Selector selector){
        this.selector = selector;
        new Thread(this).start();
    }

    public void run() {

        try {
            int sel = selector.select();
            while(sel > 0){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keys = selectionKeys.iterator();

                while(keys.hasNext()){
                    SelectionKey key = keys.next();

                    if(key.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buf = ByteBuffer.allocate(1024);

                        socketChannel.read(buf);
                        buf.flip();

                        String msg = null;
                        while(buf.hasRemaining()){
                            msg += buf.get();
                        }

                        System.err.println("from server-->" + msg);
                    }

                    keys.remove();

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
