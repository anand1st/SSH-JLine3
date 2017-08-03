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
public class MainJline3 {
    
    private static final Logger LOG = LoggerFactory.getLogger(MainJline3.class);
    private static final SshServer SSH_SERVER = SshServer.setUpDefaultServer();
    public static void main(String[] args) throws IOException, InterruptedException {
        startSshServer(8022);
        Thread.sleep(Long.MAX_VALUE);
    }
    
    public static void startSshServer(int port) throws IOException {
        SSH_SERVER.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        SSH_SERVER.setPasswordAuthenticator((username, password, session) -> true);
        SSH_SERVER.setHost("127.0.0.1");
        SSH_SERVER.setPort(port);
        SSH_SERVER.setShellFactory(() -> new EchoSshSessionJline3Instance());
        SSH_SERVER.start();
        LOG.info("Server started at port " + SSH_SERVER.getPort());
    }
    
    public static void stopSshServer() throws IOException {
        SSH_SERVER.stop();
    }
}
