package net.twisteddna.switter.swit;

import lombok.*;

import java.util.Date;

/**
 * Class representing text message(swit) of a user.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Swit {
    public static final int LENGTH_LIMIT = 140;
    private String id;
    private String authorUsername;
    private String text;
    private Date posted;

}
