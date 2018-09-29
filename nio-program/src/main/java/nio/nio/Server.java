package nio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server implements Runnable{

    private static final int PORT = 1900;

    private static final int BUFFERSIZE = 1024;

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    public Server(){

    }

    @Override
    public void run() {
        init();
    }

    private void init(){
        try{
            //1. 初始化selector
            selector = Selector.open();

            //2. 初始化channel
            serverSocketChannel = ServerSocketChannel.open();

            //3. 将channel注册到selector中
            //3.1 绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            //3.2 设置为非阻塞的
            serverSocketChannel.configureBlocking(false);
            //3.3 注册
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //4. 创建一个处理事件
            Handle handle = new HandleImpl(BUFFERSIZE);

            while(true){

                int sel = selector.select(2000);
                if(sel == 0){
                    System.out.println("孤独等待中.........");
                    continue;
                }

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keys = selectionKeys.iterator();

                while(keys.hasNext()){

                    // 获取注册事件
                    SelectionKey key = keys.next();

                    try{
                        if(key.isAcceptable()){
                            System.out.println("Server端accept.......");
                            handle.accept(key);
                        }

                        if(key.isReadable()){
                            System.out.println("Server端read........");
                            handle.read(key);
                        }

                        if(key.isWritable()){
//                            System.out.println("Server端write.......");
                            handle.write(key);
                        }
                    } catch (Exception e){
                        keys.remove();
                        e.printStackTrace();
                        continue;
                    }

                    // 移除（必须）
                    keys.remove();
                }


            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
