package net.twisteddna.switter.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowResult {
    public static final FollowResult SUCCESS = new FollowResult("");
    private String errorMessage;
    public boolean isSuccess(){
        return errorMessage.isEmpty();
    }
}
