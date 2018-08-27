package net.twisteddna.switter.user;

import net.twisteddna.switter.Storage;
import net.twisteddna.switter.swit.Swit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    private static final String EXAMPLE_USERNAME = "twisteddna";
    private static final String IDOL_USERNAME = "sturbuxman";
    private static final String IDOL_2_USERNAME = "martinfowler";
    private static final String NOT_FOLLOWED_IDOL = "springjuergen";

    @InjectMocks
    private UserService userService;

    @Mock
    private Storage storage;

    @Test
    public void shouldReturnLatestFirstSwitsOnWallRequest() {
        Swit weekAgoSwit = generateSwit(EXAMPLE_USERNAME, Date.from(Instant.now().minus(Duration.ofDays(7))));
        Swit dayAgoSwit = generateSwit(EXAMPLE_USERNAME, Date.from(Instant.now().minus(Duration.ofDays(1))));
        Swit todaysSwit = generateSwit(EXAMPLE_USERNAME, Date.from(Instant.now()));
        when(storage.findAllSwitsForUsername(EXAMPLE_USERNAME)).thenReturn(Arrays.asList(dayAgoSwit, todaysSwit, weekAgoSwit));

        List<Swit> wall = userService.wall(EXAMPLE_USERNAME);
        assertEquals(todaysSwit, wall.get(0));
        assertEquals(dayAgoSwit, wall.get(1));
        assertEquals(weekAgoSwit, wall.get(2));
    }

    @Test
    public void shouldReturnEmptyListOnNonExistingUserTimelineRequest() {
        when(storage.findUserByUsername(EXAMPLE_USERNAME)).thenReturn(Optional.ofNullable(null));
        List<Swit> timeline = userService.timeline(EXAMPLE_USERNAME);
        assertEquals(0, timeline.size());
    }

    @Test
    public void shouldReturnLatestFirstSwitsOnFollowRequest() {
        Swit earlySwit = generateSwit(IDOL_USERNAME, Date.from(Instant.now().minus(Duration.ofDays(7))));
        Swit laterSwit = generateSwit(IDOL_2_USERNAME, Date.from(Instant.now().minus(Duration.ofDays(1))));
        Swit notFollowedSwit = generateSwit(NOT_FOLLOWED_IDOL, Date.from(Instant.now()));

        when(storage.findAllSwitsForUsername(IDOL_USERNAME)).thenReturn(Collections.singletonList(earlySwit));
        when(storage.findAllSwitsForUsername(IDOL_2_USERNAME)).thenReturn(Collections.singletonList(laterSwit));
        when(storage.findAllSwitsForUsername(NOT_FOLLOWED_IDOL)).thenReturn(Collections.singletonList(notFollowedSwit));

        User exampleUser = buildUserFollowingIdols();

        when(storage.findUserByUsername(EXAMPLE_USERNAME)).thenReturn(Optional.of(exampleUser));

        List<Swit> timeline = userService.timeline(EXAMPLE_USERNAME);

        assertEquals(2, timeline.size());
        assertEquals(laterSwit, timeline.get(0));
        assertEquals(earlySwit, timeline.get(1));
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionOnNonExistingFollower() throws UserNotFoundException {
        userService.follow(null, null);
    }
    @Test(expected = UserNotFoundException.class)
    public void shouldReturnNotSuccessfulResultOnIdolDoesNotExist() throws UserNotFoundException {
        userService.follow(EXAMPLE_USERNAME, null);
    }
    @Test
    public void shouldReturnSuccessfulResultOnFollowCall() throws UserNotFoundException {
        User follower = User.builder().username(EXAMPLE_USERNAME).follows(new ArrayList<>()).build();
        User idol = User.builder().username(EXAMPLE_USERNAME).follows(new ArrayList<>()).build();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        when(storage.findUserByUsername(EXAMPLE_USERNAME)).thenReturn(Optional.of(follower));
        when(storage.findUserByUsername(IDOL_USERNAME)).thenReturn(Optional.of(idol));

        userService.follow(EXAMPLE_USERNAME, IDOL_USERNAME);

        verify(storage).update(captor.capture());
        assertEquals(idol, captor.getValue().getFollows().get(0));
    }

    private User buildUserFollowingIdols() {
        return User.builder().username(EXAMPLE_USERNAME)
                .follows(Arrays.asList(
                        User.builder().username(IDOL_USERNAME).build(),
                        User.builder().username(IDOL_2_USERNAME).build()
                )).build();
    }

    private Swit generateSwit(String username, Date posted) {
        return Swit.builder().authorUsername(username).posted(posted).text("Java experience++").build();
    }

}