package net.twisteddna.switter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.twisteddna.switter.swit.Swit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void returningUserWallInLatestFirstOrder() throws Exception {
        Swit earlier = Swit
                .builder()
                .authorUsername("josh_long")
                .text("Reactive streams hype")
                .posted(Date.from(Instant.now().minus(Duration.ofDays(365))))
                .build();
        Swit later = Swit
                .builder()
                .authorUsername("josh_long")
                .text("Cloud native hype.")
                .posted(Date.from(Instant.now()))
                .build();

        postASwit(earlier);
        postASwit(later);

        MvcResult result = mockMvc.perform(get("/users/josh_long/wall"))
                .andExpect(status().isOk())
                .andReturn();
        List<Swit> received = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Swit>>(){});
        assertTrue(received.get(0).getText().contains("Cloud native hype."));
        assertTrue(received.get(1).getText().contains("Reactive streams hype"));
    }
    @Test
    public void returningUserTimelineInLatestFirstOrder() throws Exception {
        Swit earlier = Swit
                .builder()
                .authorUsername("juergen_hoeller")
                .text("Our applications need context.")
                .posted(Date.from(Instant.now().minus(Duration.ofDays(3000))))
                .build();
        Swit someTimeAgo = Swit
                .builder()
                .authorUsername("martin_fowler")
                .text("Refactoring has become a core skill for software developers.")
                .posted(Date.from(Instant.now().minus(Duration.ofDays(1200))))
                .build();
        Swit later = Swit
                .builder()
                .authorUsername("uber_developer")
                .text("Spring suxx.")
                .posted(Date.from(Instant.now()))
                .build();

        postASwit(earlier);
        postASwit(someTimeAgo);
        postASwit(later);

        mockMvc.perform(post("/users/uber_developer/follow/juergen_hoeller")).andReturn();
        mockMvc.perform(post("/users/uber_developer/follow/martin_fowler")).andReturn();

        MvcResult result = mockMvc.perform(get("/users/uber_developer/timeline"))
                .andExpect(status().isOk())
                .andReturn();
        List<Swit> received = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Swit>>(){});
        assertEquals("Refactoring has become a core skill for software developers.",received.get(0).getText());
        assertTrue(received.get(0).getAuthorUsername().contains("martin_fowler"));
        assertEquals("Our applications need context.",received.get(1).getText());
        assertTrue(received.get(1).getAuthorUsername().contains("juergen_hoeller"));
    }

    @Test
    public void badRequestOnNonExistingUserWallRequest() throws Exception {
        mockMvc.perform(get("/users/twisteddna/wall"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User twisteddna does not exist.")));
    }

    @Test
    public void badRequestOnNonExistingUserTimelineRequest() throws Exception {
        mockMvc.perform(get("/users/twisteddna/timeline"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User twisteddna does not exist.")));
    }

    private void postASwit(Swit swit) throws Exception {
        mockMvc.perform(post("/swits/post").content(objectMapper.writeValueAsString(swit)).contentType(MediaType.APPLICATION_JSON)).andReturn();
    }
}
