package net.twisteddna.switter;

import net.twisteddna.switter.swit.Swit;
import net.twisteddna.switter.user.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class Storage {
    private final Set<User> users;
    private final Set<Swit> swits;

    public Storage() {
        this.users = new HashSet<>();
        this.swits = new HashSet<>();
    }

    public void addSwit(Swit swit) {
        swit.setId(UUID.randomUUID().toString());
        swits.add(swit);
    }

    public List<Swit> findAllSwitsForUsername(String authorUsername) {
        return swits.stream().filter(swit -> swit.getAuthorUsername().equals(authorUsername)).collect(Collectors.toList());
    }

    public void saveUser(User user) {
        users.remove(user);
        users.add(user);
    }

    public User findUserByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }
}
