package nio.tcp.impl;

import nio.tcp.TCPProtocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TCPProtocolImpl implements TCPProtocol {

    private int bufferSize;

    public TCPProtocolImpl(int bufferSize){
        this.bufferSize = bufferSize;
    }

    /**
     * 将可连接 调整为 可读取
     * @param key
     * @throws IOException
     */
    public void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel clientChannel = (ServerSocketChannel)key.channel();
        clientChannel.accept()
                .configureBlocking(false)
                .register(key.selector(), SelectionKey.OP_READ | SelectionKey.OP_WRITE, ByteBuffer.allocate(bufferSize));
    }

    public void handleRead(SelectionKey key) throws IOException {

        //获得与客户端通信的通道
        SocketChannel clientChannel = (SocketChannel) key.channel();

        // 得到并清空缓冲区
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();

        // 读取信息获得读取的字节数
        int read = clientChannel.read(buffer);

        if(read == -1){
            // 没有读取到内容的情况
            clientChannel.close();
        } else{
            //将缓冲区准备为数据传出状态
            buffer.flip();

            //将字符串转化为UTF-16的字符串
            String receivedString = Charset.forName("UTF-16").newDecoder().decode(buffer).toString();

            // 控制台打印
            System.out.println("接受到来自[" + clientChannel.socket().getRemoteSocketAddress() + "]的信息->" + receivedString);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String f = format.format(new Date());

            // 准备发送文本
            String sendString = "你好，客户端.@" + f + ", 已经收到你的信息->" + receivedString;
            buffer = ByteBuffer.wrap(sendString.getBytes("UTF-16"));
            clientChannel.write(buffer);

            // 设置为下一次读取或是写入做准备
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        }


    }

    public void handleWrite(SelectionKey key) throws IOException {
        // do nothing
    }
}
