package com.developers.dmaker.service;

import com.developers.dmaker.dto.CreateDeveloper;
import com.developers.dmaker.dto.DeveloperDetailDto;
import com.developers.dmaker.entity.Developer;
import com.developers.dmaker.exception.DmakerErrorCode;
import com.developers.dmaker.exception.DmakerException;
import com.developers.dmaker.repository.DeveloperRepository;
import com.developers.dmaker.repository.RetiredDeveloperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.developers.dmaker.code.StatusCode.EMPLOYED;
import static com.developers.dmaker.type.DeveloperLevel.JUNIOR;
import static com.developers.dmaker.type.DeveloperLevel.SENIOR;
import static com.developers.dmaker.type.DeveloperSkillType.BACK_END;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DmakerServiceTest {
    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @InjectMocks
    private DmakerService dmakerService;

    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(SENIOR)
            .developerSkillType(BACK_END)
            .experienceYears(12)
            .statusCode(EMPLOYED)
            .name("punch")
            .age(20)
            .build();

    private final CreateDeveloper.Request defaultCreateRequest =
            CreateDeveloper.Request.builder()
                    .developerLevel(JUNIOR)
                    .developerSkillType(BACK_END)
                    .experienceYears(2)
                    .memberId("Rakku")
                    .name("punch")
                    .age(20)
                    .build();

    @Test
    public void testSomething() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        DeveloperDetailDto developerDetailDto = dmakerService.getDeveloperDetail("Rakku");

        //then
        assertEquals(SENIOR, developerDetailDto.getDeveloperLevel());
        assertEquals(BACK_END, developerDetailDto.getDeveloperSkillType());
        assertEquals(12, developerDetailDto.getExperienceYears());
    }

    @Test
    void createDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        // 데이터 캡쳐를 위해
        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        //when
        CreateDeveloper.Response developer = dmakerService.createDeveloper(defaultCreateRequest);

        //then
        // 특정 mock이 몇 번 호출됐다는 것을 검증
        verify(developerRepository, times(1))
                .save(captor.capture());
        Developer savedDeveloper = captor.getValue();
        assertEquals(JUNIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(BACK_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(2, savedDeveloper.getExperienceYears());
    }

    @Test
    void createDeveloperTest_failed_with_duplicated() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        //then
        // assertThrows는 첫번째로 예상되는 exception.class를 받고 두번째로 해당 exception을 던질 동작을 받음
        DmakerException dmakerException = assertThrows(DmakerException.class,
                () -> dmakerService.createDeveloper(defaultCreateRequest)
        );

        assertEquals(DmakerErrorCode.DUPLICATED_MEMBER_ID, dmakerException.getDmakerErrorCode());
    }
}