package acceptance.steps.givens;

import com.googlecode.propidle.authorisation.users.Username;
import static com.googlecode.propidle.server.sessions.Session.session;
import com.googlecode.propidle.server.sessions.SessionId;
import static com.googlecode.propidle.server.sessions.SessionId.sessionId;
import com.googlecode.propidle.server.sessions.Sessions;
import static com.googlecode.propidle.server.sessions.StartTime.startTime;
import acceptance.steps.WebClient;
import acceptance.steps.Given;
import acceptance.steps.Step;
import acceptance.Values;
import com.googlecode.propidle.util.Clock;

import java.util.concurrent.Callable;

public class CurrentUserIs implements Callable<SessionId> {
    private final Username username;
    private final Sessions sessions;
    private final Clock clock;
    private final WebClient webClient;

    public static Step<SessionId> currentUserIs(Values values){
        return new Given<SessionId>(CurrentUserIs.class, values);
    }

    public CurrentUserIs(Username username, Sessions sessions, Clock clock, WebClient webClient) {
        this.username = username;
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

}