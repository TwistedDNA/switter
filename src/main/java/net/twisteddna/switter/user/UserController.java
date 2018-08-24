package net.twisteddna.switter.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}/wall")
    public ResponseEntity wall(@PathVariable String username) {
        if (!userService.userExists(username)) {
            return ResponseEntity.badRequest().body(String.format("User %s does not exist.", username));
        }
        return ResponseEntity.ok().body(userService.wall(username));
    }

    @GetMapping("/{username}/timeline")
    public ResponseEntity timeline(@PathVariable String username) {
        if (!userService.userExists(username)) {
            return ResponseEntity.badRequest().body(String.format("User %s does not exist.", username));
        }
        return ResponseEntity.ok().body(userService.timeline(username));
    }

    @PostMapping("/{follower}/follow/{idol}")
    public ResponseEntity follow(@PathVariable String follower, @PathVariable String idol) {
        FollowResult result = userService.follow(follower, idol);
        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result.getErrorMessage());
        }
        return ResponseEntity.ok().build();
    }
}
