package net.twisteddna.switter;

import net.twisteddna.switter.swit.Swit;
import net.twisteddna.switter.user.User;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Storage {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Swit> swits = new HashMap<>();

    public void addSwit(Swit swit) {
        swit.setId(UUID.randomUUID().toString());
        swits.put(swit.getId(), swit);
    }

    public List<Swit> findAllSwitsForUsername(String authorUsername) {
        return swits.values()
                .stream()
                .filter(swit -> swit.getAuthorUsername().equals(authorUsername))
                .collect(Collectors.toList());
    }

    public void update(User user) {
        users.put(user.getUsername(), user);
    }

    public Optional<User> findUserByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public void createUser(String author) {
        users.put(author,User.builder().username(author).follows(new ArrayList<>()).build());
    }
}
