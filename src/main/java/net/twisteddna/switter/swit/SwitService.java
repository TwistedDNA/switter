package net.twisteddna.switter.swit;

import net.twisteddna.switter.Storage;
import org.springframework.stereotype.Component;


@Component
public class SwitService {

    private final Storage storage;

    public SwitService(Storage storage) {
        this.storage = storage;
    }

    public void postASwit(Swit swit) {
        String author = swit.getAuthorUsername();
        if (!storage.findUserByUsername(author).isPresent()) {
            storage.createUser(author);
        }
        storage.addSwit(swit);
    }
}
