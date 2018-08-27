package net.twisteddna.switter.user;

import net.twisteddna.switter.Storage;
import net.twisteddna.switter.swit.Swit;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<Swit> swits = new ArrayList<>();
        storage.findUserByUsername(username).ifPresent(
                user -> user.getFollows().stream()
                        .flatMap(findAllIdolsSwits())
                        .sorted(Comparator.comparing(Swit::getPosted).reversed())
                        .collect(Collectors.toCollection(() -> swits)));


        return swits;
    }

    private Function<User, Stream<? extends Swit>> findAllIdolsSwits() {
        return idol -> storage.findAllSwitsForUsername(idol.getUsername()).stream();
    }


    public void follow(String follower, String idol) throws UserNotFoundException {
        User followerUser = storage.findUserByUsername(follower).orElseThrow(() -> new UserNotFoundException("Follower not found"));
        User idolUser = storage.findUserByUsername(idol).orElseThrow(() -> new UserNotFoundException("Idol not found"));
        followerUser.addIdol(idolUser);
        storage.update(followerUser);
    }

    public boolean userExists(String username) {
        return storage.findUserByUsername(username).isPresent();
    }
}
