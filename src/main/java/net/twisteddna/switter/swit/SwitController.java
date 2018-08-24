package net.twisteddna.switter.swit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/swits")
public class SwitController {

    private final SwitService switService;

    public SwitController(SwitService switService) {
        this.switService = switService;
    }

    @PostMapping("/post")
    public ResponseEntity post(@RequestBody Swit swit) {
        ValidatedSwit validatedSwit = new ValidatedSwit(swit);
        if (!validatedSwit.isValid()) {
            return ResponseEntity.badRequest().body(validatedSwit.getErrorMessage());
        } else {
            switService.postASwit(swit);
            return ResponseEntity.ok().build();
        }
    }
}
