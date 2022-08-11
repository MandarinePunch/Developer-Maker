package com.developers.dmaker.controller;

import com.developers.dmaker.dto.DeveloperDto;
import com.developers.dmaker.service.DmakerService;
import com.developers.dmaker.type.DeveloperLevel;
import com.developers.dmaker.type.DeveloperSkillType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.developers.dmaker.type.DeveloperLevel.*;
import static com.developers.dmaker.type.DeveloperSkillType.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// controller 테스트시 어노테이션
@WebMvcTest(DmakerController.class)
class DmakerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DmakerService dmakerService;

    protected MediaType contentType
            = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    @Test
    void getAllDevelopers() throws Exception {
        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(BACK_END)
                .developerLevel(JUNIOR)
                .memberId("Rakku1")
                .build();
        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(FRONT_END)
                .developerLevel(SENIOR)
                .memberId("Rakku2")
                .build();

        given(dmakerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(juniorDeveloperDto, seniorDeveloperDto));

        // get으로 developers를 호출하고 json 타입으로 주고받음
        mockMvc.perform(get("/developers").contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(
                        jsonPath("$.[0].developerSkillType",
                                is(BACK_END.name()))
                ).andExpect(
                        jsonPath("$.[0].developerLevel",
                                is(JUNIOR.name()))
                ).andExpect(
                        jsonPath("$.[1].developerSkillType",
                                is(FRONT_END.name()))
                ).andExpect(
                        jsonPath("$.[1].developerLevel",
                                is(SENIOR.name()))
                );
    }

}