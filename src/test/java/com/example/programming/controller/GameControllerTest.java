package com.example.programming.controller;
import com.example.programming.GameMenuApplication;
import com.example.programming.model.GameState;
import com.example.programming.model.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(GameController.class)
@AutoConfigureMockMvc
public class GameControllerTest  {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @MockBean
    private GameController gameController;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetReset() throws Exception {
       this.mvc.perform(get("http://localhost:8080/play/api/v1/reset")
               .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
                .andExpect(status().isOk())
               ;
    }

    @Test
    public void testPostStartGame() throws Exception {
       this.mvc.perform(post("http://localhost:8080/play/api/v1/start?name=a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
               ;
    }

    @Test
    public void testPostAddMoveToBoard() throws Exception {
       this.mvc.perform(post("http://localhost:8080/play/api/v1/move?name=a&column=6")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                ;
    }
}

