package net.twisteddna.switter.user;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String username;
    private List<User> follows;

    public User(String username) {
        this.follows = new ArrayList<>();
        this.username = username;
    }

    public void addIdol(User idol){
        follows.add(idol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
