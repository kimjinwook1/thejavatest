package hello.thejavatest.study;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import hello.thejavatest.domain.Member;
import hello.thejavatest.domain.Study;
import hello.thejavatest.domain.StudyStatus;
import hello.thejavatest.member.MemberService;
import hello.thejavatest.study.StudyServiceContainerTest.ContainerPropertyInitializer;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
@Slf4j
@ContextConfiguration(initializers = ContainerPropertyInitializer.class)
class StudyServiceContainerTest {

    @Mock
    MemberService memberService;

    @Autowired StudyRepository studyRepository;

    @Autowired
    Environment environment;

    @Value("${container.port}") int port;

    @Container
    static GenericContainer mySQLContainer = new GenericContainer("mysql")
            .withExposedPorts(13307)
            .withEnv("MYSQL_DB", "studytest")
            .waitingFor(Wait.forListeningPort());

    @BeforeAll
    static void beforeAll() {
        //컨테이너 안에 찍히는 로그를 같이 볼수 있음
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
        mySQLContainer.followOutput(logConsumer);
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("=================");
        //내 포트와 연결된 testContainer 포트를 확인하는 방법
        System.out.println(environment.getProperty("container.port"));
        System.out.println(port);

        //모든 로그를 볼 수 있음
//        System.out.println(mySQLContainer.getLogs());
        studyRepository.deleteAll();
    }

    @Test
    void createNewStudy() {

        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("jinwook@email.com");

        Study study = new Study(10, "테스트");

        given(memberService.findById(1L)).willReturn(Optional.of(member));

        // When
        studyService.createNewStudy(1L, study);

        // Then
        assertEquals(1L, study.getOwnerId());
        then(memberService).should(times(1)).notify(study);
        then(memberService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    @Test
    void openStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "더 자바, 테스트");
        assertNull(study.getOpenedDateTime());

        // When
        studyService.openStudy(study);

        // Then
        assertEquals(StudyStatus.OPENED, study.getStatus());
        assertNotNull(study.getOpenedDateTime());
        then(memberService).should().notify(study);
    }

    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of("container.port=" + mySQLContainer.getMappedPort(13307))
                    .applyTo(context.getEnvironment());
        }
    }

}
