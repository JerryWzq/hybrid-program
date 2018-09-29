package nio.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class HandleImpl implements Handle {

    private int bufferSize;

    public HandleImpl(int bufferSize){
        this.bufferSize = bufferSize;
    }

    public void accept(SelectionKey selectionKey) throws IOException {

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        serverSocketChannel.accept()
                .configureBlocking(false)
                .register(selectionKey.selector(), SelectionKey.OP_READ | SelectionKey.OP_WRITE);

    }

    public void read(SelectionKey selectionKey) throws IOException {

        // 1. 获取channel信道
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        // 2. 创建一个buffer用来读取数据
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        socketChannel.read(buffer);
        buffer.flip();

        String msg = "";
        while(buffer.hasRemaining()){
            msg += (char)buffer.get();
        }

        System.out.println(msg);
    }

    public void write(SelectionKey selectionKey) throws IOException {

        // 1. 获取channel信道
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        // 2. 创建一个buffer用来写数据
//        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
//        String msg = "server msg...";
//        buffer.put(msg.getBytes());
//        buffer.flip();

        socketChannel.write(ByteBuffer.wrap("this is server....".getBytes()));
    }

    public void connect(SelectionKey selectionKey) {
        //do nothing
    }
}
