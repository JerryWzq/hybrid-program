package nio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Client {

    private int port;

    private String host;

    private SocketChannel socketChannel;

    private Selector selector;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    private void init() {
        try {
            //1.selector初始化
            selector = Selector.open();
            //2.channel初始化
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            //3.注册
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);


//            new TCPClientReadThread(selector);
//            int sel = selector.select(1000);
//
//            while (sel > 0) {
//                Set<SelectionKey> selectionKeys = selector.selectedKeys();
//                Iterator<SelectionKey> keys = selectionKeys.iterator();
//                while (keys.hasNext()) {
//
//                    SelectionKey key = keys.next();
//
//                    if (key.isReadable()) {
//                        SocketChannel socketChannel = (SocketChannel) key.channel();
//                        ByteBuffer buf = ByteBuffer.allocate(1024);
//                        socketChannel.read(buf);
//                        buf.flip();
//
//                        String msg = null;
//                        while (buf.hasRemaining()) {
//                            msg += (char) buf.get();
//                        }
//
//                        System.out.println("来自Server端的消息-->>" + msg);
//                    }
//                    keys.remove();
//                }
//            }

            int sel = selector.select();
            System.out.println("Client select->" + sel);
            while (sel > 0) {
                // 遍历每个有可用IO操作Channel对应的SelectionKey
                for (SelectionKey sk : selector.selectedKeys()) {

                    // 如果该SelectionKey对应的Channel中有可读的数据
                    if (sk.isReadable()) {
                        // 使用NIO读取Channel中的数据
                        SocketChannel sc = (SocketChannel) sk.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        buffer.flip();

                            String receivedString = "";
                            while(buffer.hasRemaining()){
                                receivedString += (char)buffer.get();
                        }

                        // 控制台打印出来
                        System.out.println("接收到来自服务器[" + sc.socket().getRemoteSocketAddress() + "]的信息:" + receivedString);

                        String msg = "This is client....";
                        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
                        // 为下一次读取作准备
                        sk.interestOps(SelectionKey.OP_READ);
                    }

                    // 删除正在处理的SelectionKey
                    selector.selectedKeys().remove(sk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) throws IOException {
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        buffer.put(msg.getBytes())
//                .flip();
        System.err.println("send msg to server....");
        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
    }

}
