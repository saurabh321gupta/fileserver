package fileserver.helpers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SSLManagerTest {

    @Test
    public void shouldSetSSLProperties() throws Exception {
        System.clearProperty("javax.net.ssl.trustStore");
        System.clearProperty("javax.net.ssl.trustStorePassword");
        SSLManager.setSslProperties();
        String trustStore = System.getProperty("javax.net.ssl.trustStore");
        String trustStorePassword = System.clearProperty("javax.net.ssl.trustStorePassword");

        Assert.assertTrue(trustStore.matches("/tmp/sys-connect-via-ssl-test-cacerts\\d{19}.jks"));
        Assert.assertEquals("password", trustStorePassword);
    }

    @Test
    public void shouldClearSSLProperties() throws Exception {
        SSLManager.setSslProperties();
        SSLManager.clearSslProperties();

        String trustStore = System.getProperty("javax.net.ssl.trustStore");
        String trustStorePassword = System.clearProperty("javax.net.ssl.trustStorePassword");

        Assert.assertEquals(null, trustStore);
        Assert.assertEquals(null, trustStorePassword);
    }

}