package test.ssh.jline3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author anand
 */
class EchoSshSessionInstance implements Command, Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(EchoSshSessionInstance.class);
    private InputStream in;
    private OutputStream out;
    private ExitCallback callback;
    private Thread sshThread;

    @Override
    public void setInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public void setOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void setErrorStream(OutputStream err) {
    }

    @Override
    public void setExitCallback(ExitCallback callback) {
        this.callback = callback;
    }

    @Override
    public void start(Environment env) throws IOException {
        (sshThread = new Thread(this)).start();
    }

    @Override
    public void destroy() throws Exception {
        sshThread.interrupt();
    }

    @Override
    public void run() {
        try (Terminal terminal = TerminalBuilder.builder().system(false).streams(in, out).build()) {
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
            terminal.writer().println("Press CTRL-C to exit");
            String line;
            while((line = reader.readLine("localhost>")) != null) {
                terminal.writer().println(line + '\r');
                terminal.flush();
            }
        } catch (IOException ex) {
            LOG.error("IO Exception", ex);
        } catch (UserInterruptException ex) {
            LOG.info(ex.getMessage());
        } finally {
            callback.onExit(0);
        }
    }
}
