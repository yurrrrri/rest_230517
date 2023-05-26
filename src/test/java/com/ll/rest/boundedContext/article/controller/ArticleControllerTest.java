package com.ll.rest.boundedContext.article.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class ArticleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void t001() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/api/v1/articles"))
                .andDo(print());

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.articles[0].id").exists());
    }

    @Test
    void t002() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/api/v1/articles/1"))
                .andDo(print());

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.article.id").value(1));
    }

    @Test
    @WithUserDetails("user1")
    void t003() throws Exception {
        ResultActions resultActions = mvc
                .perform(post("/api/v1/articles")
                        .content("""
                                {
                                "subject": "제목 new",
                                "content": "내용 new"
                                }
                                """)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                ).andDo(print());

        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.article").exists());
    }

}
