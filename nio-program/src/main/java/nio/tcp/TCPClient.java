package nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class TCPClient {

    /** 信道选择器 */
    private Selector selector;

    /** 与服务器通信的通道 */
    SocketChannel socketChannel;

    /** 要连接的远程服务器Ip地址 */
    private String hostIp;

    /** 要连接的远程服务器在监听的端口 */
    private int hostListenningPort;

    /**
     *
     * @param hostIp
     * @param hostListenningPort
     * @throws IOException
     */
    public TCPClient(String hostIp, int hostListenningPort) throws IOException {
        this.hostIp = hostIp;
        this.hostListenningPort = hostListenningPort;

        initialize();
    }

    /**
     * 初始化
     * @throws IOException
     */
    private void initialize() throws IOException {

        //打开监听信道并设置为非阻塞模式
        socketChannel = SocketChannel.open(new InetSocketAddress(hostIp, hostListenningPort));
        socketChannel.configureBlocking(false);

        //打开并注册选择器到信道
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 启动读取线程
        new TCPClientReadThread(selector);
    }

    /**
     * 发送字符串到服务器
     * @param message
     * @throws IOException
     */
    public void sendMsg(String message) throws IOException {

        ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-16"));

        int r = socketChannel.write(writeBuffer);
        System.out.println("write msg return : " + r);

        //socketChannel
    }

    public static void main(String[] args) throws IOException {
        TCPClient client = new TCPClient("10.118.48.68", 1994);
//        for(int i=0; i<10; i++){
        client.sendMsg("Nio-");
//        }
    }

}
