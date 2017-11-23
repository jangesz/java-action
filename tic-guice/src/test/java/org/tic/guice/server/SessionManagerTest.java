package org.tic.guice.server;

import com.google.inject.Guice;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.*;

public class SessionManagerTest {

    @Inject SessionManager sessionManager;
    @Inject List<String> supportedCurrencies;
    @Inject List<String> anotherCurrencies;

    @Before public void setUp() {
        Guice.createInjector(new ServerModule())
                .injectMembers(this);
    }

    @Test
    public void testGetSessionId() throws InterruptedException {
        Long sessionId1 = this.sessionManager.getSessionId();
        Thread.sleep(1000);
        Long sessionId2 = this.sessionManager.getSessionId();

        assertNotEquals(sessionId1.longValue(), sessionId2.longValue());
    }

    @Test
    public void testGetSupportedCurrencies() {
        throw new RuntimeException(supportedCurrencies.toString());
    }

}
