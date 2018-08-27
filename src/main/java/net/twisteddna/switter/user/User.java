package net.twisteddna.switter.user;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private String username;
    private List<User> follows;

    public void addIdol(User idol){
        follows.add(idol);
    }

}
