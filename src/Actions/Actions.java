
package Actions;

import Models.User;
import java.util.ArrayList;

public interface Actions {
    int create(User user);
    int update(User user);
    int delete(User user);
    ArrayList<User>displayUsers();
}
