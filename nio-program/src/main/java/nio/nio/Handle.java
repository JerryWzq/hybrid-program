package nio.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface Handle {

    void accept(SelectionKey selectionKey) throws IOException;

    void read(SelectionKey selectionKey) throws IOException;

    void write(SelectionKey selectionKey) throws IOException;

    void connect(SelectionKey selectionKey);

}
