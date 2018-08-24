package net.twisteddna.switter.swit;

import net.twisteddna.switter.Storage;
import net.twisteddna.switter.swit.Swit;
import net.twisteddna.switter.user.User;
import org.springframework.stereotype.Component;


@Component
public class SwitService {

    private final Storage storage;

    public SwitService(Storage storage) {
        this.storage = storage;
    }

    public void postASwit(Swit swit) {
        String author = swit.getAuthorUsername();
        if (storage.findUserByUsername(author) == null) {
            storage.saveUser(new User(author));
        }
        storage.addSwit(swit);
    }
}
