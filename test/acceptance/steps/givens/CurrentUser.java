package acceptance.steps.givens;

import com.googlecode.propidle.authorisation.users.Username;
import static com.googlecode.propidle.server.sessions.Session.session;
import com.googlecode.propidle.server.sessions.SessionId;
import static com.googlecode.propidle.server.sessions.SessionId.sessionId;
import com.googlecode.propidle.server.sessions.Sessions;
import static com.googlecode.propidle.server.sessions.StartTime.startTime;
import acceptance.steps.WebClient;
import acceptance.Values;
import com.googlecode.propidle.util.Clock;

import java.util.concurrent.Callable;

public class CurrentUser implements Callable<SessionId> {
    private final Sessions sessions;
    private final Clock clock;
    private final WebClient webClient;
    private Username username;

    public CurrentUser(Sessions sessions, Clock clock, WebClient webClient) {
        this.sessions = sessions;
        this.clock = clock;
        this.webClient = webClient;
    }

    public SessionId call() throws Exception {
        SessionId id = sessionId();
        sessions.put(session(
                id,
                username,
                startTime(clock)));
        webClient.currentSession(id);
        return id;
    }

    public CurrentUser is(Username username) {
        this.username = username;
        return this;
    }
}