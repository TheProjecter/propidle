package acceptance.steps.givens;

import com.googlecode.propidle.authorisation.users.Username;
import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.propidle.authentication.Session.session;
import com.googlecode.propidle.authentication.SessionId;
import com.googlecode.propidle.authentication.Sessions;
import static com.googlecode.propidle.authentication.SessionId.sessionId;
import static com.googlecode.propidle.authentication.StartTime.startTime;
import acceptance.steps.WebClient;
import com.googlecode.propidle.util.time.Clock;

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

    public CurrentUser is(String username) {
        this.username = username(username);
        return this;
    }
}