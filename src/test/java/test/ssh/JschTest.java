package test.ssh;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import test.ssh.jline2.MainJline2;
import test.ssh.jline3.MainJline3;

/**
 *
 * @author anand
 */
public class JschTest {

    @BeforeClass
    public static void beforeClass() throws IOException {
        MainJline3.startSshServer(8022);
        MainJline2.startSshServer(9022);
    }

    @AfterClass
    public static void afterClass() throws IOException {
        MainJline3.stopSshServer();
        MainJline2.stopSshServer();
    }

    @Test
    public void testJschJline3() throws JSchException, InterruptedException {
        JSch jsch = new JSch();
        Session session = jsch.getSession("admin", "localhost", 8022);
        session.setPassword("xxx");
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        ChannelShell channel = (ChannelShell) session.openChannel("shell");
        channel.setInputStream(new CharSequenceInputStream("exit\r", StandardCharsets.UTF_8));
        OutputStream os = new ByteArrayOutputStream();
        channel.setOutputStream(os);
        channel.connect();
        Thread.sleep(2000);
        System.out.println(os.toString());
        assertEquals("localhost>exit\nexit\nlocalhost>", os.toString());
        channel.disconnect();
        session.disconnect();
    }
    
    @Test
    public void testJschJline2() throws JSchException, InterruptedException {
        JSch jsch = new JSch();
        Session session = jsch.getSession("admin", "localhost", 9022);
        session.setPassword("xxx");
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        ChannelShell channel = (ChannelShell) session.openChannel("shell");
        channel.setInputStream(new CharSequenceInputStream("exit\r", StandardCharsets.UTF_8));
        OutputStream os = new ByteArrayOutputStream();
        channel.setOutputStream(os);
        channel.connect();
        Thread.sleep(2000);
        System.out.println(os.toString());
        assertEquals("localhost>exit\nexit\nlocalhost>", os.toString());
        channel.disconnect();
        session.disconnect();
    }
}
