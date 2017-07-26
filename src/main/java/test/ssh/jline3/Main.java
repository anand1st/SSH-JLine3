package test.ssh.jline3;

import java.io.IOException;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author anand
 */
public class Main {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final SshServer SSH_SERVER = SshServer.setUpDefaultServer();
    public static void main(String[] args) throws IOException, InterruptedException {
        startSshServer();
        Thread.sleep(Long.MAX_VALUE);
    }
    
    static void startSshServer() throws IOException {
        SSH_SERVER.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        SSH_SERVER.setPasswordAuthenticator((username, password, session) -> true);
        SSH_SERVER.setHost("127.0.0.1");
        SSH_SERVER.setPort(8022);
        SSH_SERVER.setShellFactory(() -> new EchoSshSessionInstance());
        SSH_SERVER.start();
        LOG.info("Server started at port " + SSH_SERVER.getPort());
    }
    
    static void stopSshServer() throws IOException {
        SSH_SERVER.stop();
    }
}
