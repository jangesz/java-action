package org.tic.guice.server;

import com.google.inject.Provider;

import javax.inject.Inject;
import javax.inject.Named;

public class SessionManager {

    // 使用这个方式注入，当Guice第一次提供sessionId后，这个sessionId就不会变化了
//    private final Long sessionId;

    // 而使用Provider的好处就是，每次都会获取到Provider提供的值，这个提供的值可以是一个复杂的计算过程，两次结果的值可以完全不一样
    // 这样就比原先的更加灵活，可控。
    private final Provider<Long> sessionIdProvider;

    @Inject public SessionManager(@SessionId Provider<Long> sessionIdProvider) {
        this.sessionIdProvider = sessionIdProvider;
    }

    public Long getSessionId() {
        return this.sessionIdProvider.get();
    }

}
