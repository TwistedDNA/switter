package net.twisteddna.switter.user;

import net.twisteddna.switter.Storage;
import net.twisteddna.switter.swit.Swit;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserService {

    private final Storage storage;

    public UserService(Storage storage) {
        this.storage = storage;
    }

    public List<Swit> wall(String username) {
        return storage
                .findAllSwitsForUsername(username)
                .stream()
                .sorted(Comparator.comparing(Swit::getPosted).reversed())
                .collect(Collectors.toList());
    }


    public List<Swit> timeline(String username) {
        User user = storage.findUserByUsername(username);
        if (user == null) {
            return Collections.EMPTY_LIST;
        }
        return user.getFollows().stream()
                .flatMap(idol -> storage.findAllSwitsForUsername(idol.getUsername()).stream())
                .sorted(Comparator.comparing(Swit::getPosted).reversed())
                .collect(Collectors.toList());
    }

    public FollowResult follow(String follower, String idol) {
        User followerUser = storage.findUserByUsername(follower);
        if (followerUser == null) {
            return new FollowResult("Follower user does not exist.");
        }
        User idolUser = storage.findUserByUsername(idol);
        if (idolUser == null) {
            return new FollowResult("Idol user does not exist.");
        }
        followerUser.addIdol(idolUser);
        storage.saveUser(followerUser);
        return FollowResult.SUCCESS;
    }

    public boolean userExists(String username) {
        return storage.findUserByUsername(username) != null;
    }
}
