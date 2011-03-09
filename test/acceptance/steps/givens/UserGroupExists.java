package acceptance.steps.givens;

import com.googlecode.propidle.authorisation.groups.GroupName;
import com.googlecode.propidle.authorisation.groups.Group;
import com.googlecode.propidle.authorisation.groups.Groups;
import static com.googlecode.propidle.authorisation.groups.Group.group;
import com.googlecode.yatspec.state.givenwhenthen.TestLogger;
import com.googlecode.totallylazy.Option;

import java.util.concurrent.Callable;

public class UserGroupExists implements Callable<Group>{
    private final Groups groups;
    private final TestLogger logger;
    private GroupName groupName;

    public UserGroupExists(Groups groups, TestLogger logger) {
        this.groups = groups;
        this.logger = logger;
    }

    public UserGroupExists with(GroupName groupName) {
        this.groupName = groupName;
        return this;
    }

    public Group call() throws Exception {
        return groups.get(groupName).getOrElse(createGroup());
    }

    private Callable<Group> createGroup() {
        return new Callable<Group>() {
            public Group call() throws Exception {
                Group group = groups.add(group(groupName));
                logger.log("group", group);
                return group;
            }
        };
    }
}
