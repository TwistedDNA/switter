package net.twisteddna.switter.user;

import net.twisteddna.switter.swit.Swit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserControllerTest {


    private static final String EXAMPLE_USERNAME = "twisteddna";
    private static final String ERROR_MESSAGE = "Error message";
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void wallRequestShouldReturnBadRequestOnNonExistingUser() {
        when(userService.userExists(EXAMPLE_USERNAME)).thenReturn(false);
        ResponseEntity result = userController.wall(EXAMPLE_USERNAME);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(String.format("User %s does not exist.", EXAMPLE_USERNAME), result.getBody().toString());
    }

    @Test
    public void wallRequestShouldReturnDataOnCorrectUser() {
        when(userService.userExists(EXAMPLE_USERNAME)).thenReturn(true);
        when(userService.wall(EXAMPLE_USERNAME)).thenReturn(Arrays.asList(generateSwit(), generateSwit()));

        ResponseEntity result = userController.wall(EXAMPLE_USERNAME);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        List<Swit> swits = (List<Swit>) result.getBody();
        assertEquals(2, swits.size());
    }

    @Test
    public void timelineRequestShouldReturnBadRequestOnNonExistingUser() {
        when(userService.userExists(EXAMPLE_USERNAME)).thenReturn(false);
        ResponseEntity result = userController.timeline(EXAMPLE_USERNAME);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(String.format("User %s does not exist.", EXAMPLE_USERNAME), result.getBody().toString());
    }

    @Test
    public void timelineRequestShouldReturnDataOnCorrectUser() {
        when(userService.userExists(EXAMPLE_USERNAME)).thenReturn(true);
        when(userService.timeline(EXAMPLE_USERNAME)).thenReturn(Arrays.asList(generateSwit(), generateSwit()));

        ResponseEntity result = userController.timeline(EXAMPLE_USERNAME);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        List<Swit> swits = (List<Swit>) result.getBody();
        assertEquals(2, swits.size());
    }

    @Test
    public void followRequestShouldReturnBadRequestOnNonExistingUser() throws UserNotFoundException {
        doThrow(new UserNotFoundException(ERROR_MESSAGE)).when(userService).follow(EXAMPLE_USERNAME, EXAMPLE_USERNAME);
        ResponseEntity result = userController.follow(EXAMPLE_USERNAME, EXAMPLE_USERNAME);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody().toString());

    }

    @Test
    public void followRequestShouldReturnOkOnCorrectUser() throws UserNotFoundException {
        doNothing().when(userService).follow(EXAMPLE_USERNAME, EXAMPLE_USERNAME);
        ResponseEntity result = userController.follow(EXAMPLE_USERNAME, EXAMPLE_USERNAME);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    private Swit generateSwit() {
        return Swit.builder().text("datagram").posted(Date.from(Instant.now())).authorUsername(EXAMPLE_USERNAME).build();
    }
}