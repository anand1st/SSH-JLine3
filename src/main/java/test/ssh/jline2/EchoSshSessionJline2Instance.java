package test.ssh.jline2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import jline.console.ConsoleReader;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.jline.reader.UserInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author anand
 */
public class EchoSshSessionJline2Instance implements Command, Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(EchoSshSessionJline2Instance.class);
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
        try (ConsoleReader reader = new ConsoleReader(in, out)) {
            reader.setPrompt("localhost>");
            PrintWriter pw = new PrintWriter(reader.getOutput());
            String line;
            while((line = reader.readLine()) != null) {
                pw.println(line);
                pw.flush();
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
