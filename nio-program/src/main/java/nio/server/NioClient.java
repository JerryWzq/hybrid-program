package nio.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioClient implements Runnable{

    public int id = 100001;
    public int bufferSize = 2048;

    @Override
    public void run() {
        init();
    }

    public void init(){
        try {
            System.out.println("nio Client init....");
            //创建一个socketchannel通道
            SocketChannel socketChannel = SocketChannel.open();
            //2. 创建一个selector选择器
            Selector selector = Selector.open();
            //3. 绑定地址
            InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 4700);
            socketChannel.bind(inetSocketAddress);
            //4. 设置通道为非阻塞，并绑定选择器
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Client started .... port: 4700");
            listener(selector);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void listener(Selector inSelector){

        try{

            while(true){
                Thread.sleep(1*1000);
                // 阻塞，知道有就绪事件为止
                Set<SelectionKey> readySelectionkey = inSelector.selectedKeys();
                Iterator<SelectionKey> iterator = readySelectionkey.iterator();

                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();

                    if(selectionKey.isAcceptable()){
                        System.out.println(selectionKey.attachment() + " - 接受请求事件");
                        //获取通道 接受连接
                        //设置非阻塞模式（必须）， 同时需要注册 读写数据的事件，这样有消息触发时才能捕获
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                        serverSocketChannel.accept()
                                .configureBlocking(false)
                                .register(inSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE)
                                .attach(id++);

                        System.out.println(selectionKey.attachment() + "　- 已连接");

                    }

                    //读数据
                    if(selectionKey.isReadable()){
                        System.out.println(selectionKey.attachment() + " - 读数据事件");
                        SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
                        clientChannel.read(buffer);
                        System.out.println(selectionKey.attachment() + " - 读数据： " + getString(buffer));
                    }

                    //写数据
                    if(selectionKey.isWritable()){
                        System.out.println(selectionKey.attachment() + " - 写数据事件");

                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer sendBuf = ByteBuffer.allocate(bufferSize);
                        String sendTxt = "client msg\n";
                        sendBuf.put(sendTxt.getBytes());
                        //写完数据，需要调用此方法，转到读模式，使position在头位置
                        sendBuf.flip();

                        channel.write(sendBuf);

                    }

                    if(selectionKey.isConnectable()){
                        System.out.println(selectionKey.attachment() + " - 连接事件");
                    }

                    //必须remove 否则会继续存在， 下一次循环还会进来
                    //注意remove的位置， 针对一个next（） remove一次
                    iterator.remove();

                }
            }

        } catch (Exception e){
            System.out.println("Error - " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static String getString(ByteBuffer buffer){
        String msg = "";
        try{
            for (int i=0; i < buffer.position(); i++){
                msg += (char)buffer.get(i);
            }
            return msg;
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
