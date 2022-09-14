package hello.thejavatest.study;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import hello.thejavatest.domain.Member;
import hello.thejavatest.domain.Study;
import hello.thejavatest.domain.StudyStatus;
import hello.thejavatest.member.MemberService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//@ExtendWith를 선언해줘야 Mock객체를 제대로 활용할 수 있다.
@ExtendWith(MockitoExtension.class)
class StudyServiceMockitoTest {

//	@Test
//	void createStudyService() {
//		MemberService memberService = mock(MemberService.class);
//		StudyRepository studyRepository = mock(StudyRepository.class);
//		StudyService studyService = new StudyService(memberService, studyRepository);
//		assertNotNull(studyService);
//	}

//	@Mock
//	MemberService memberService;
//	@Mock
//	StudyRepository studyRepository;
//
//	@Test
//	void createStudyService() {
//
//		StudyService studyService = new StudyService(memberService, studyRepository);
//		assertNotNull(studyService);
//	}

//	@Test
//	void createStudyService(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {
//
//		StudyService studyService = new StudyService(memberService, studyRepository);
//		assertNotNull(studyService);
//	}

	@Test
	void createNewStudy_1(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {

		StudyService studyService = new StudyService(memberService, studyRepository);
		assertNotNull(studyService);

		Member member = new Member();
		member.setId(1L);
		member.setEmail("jinwook@gmail.com");

		//mock객체를 가지고 스터빙을 한 것
		when(memberService.findById(1L)).thenReturn(Optional.of(member));

		Study study = new Study(10, "java");

		Optional<Member> findMember = memberService.findById(1L);
		assertEquals("jinwook@gmail.com", findMember.get().getEmail());

//		studyService.createNewStudy(1L, study);
	}

	@Test
	void createNewStudy_2(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {

		StudyService studyService = new StudyService(memberService, studyRepository);
		assertNotNull(studyService);

		Member member = new Member();
		member.setId(1L);
		member.setEmail("jinwook@gmail.com");

		//mock객체를 가지고 스터빙을 한 것(파라미터에 어떤 값이 들어오더라도 Optional<member>를 리턴함
		when(memberService.findById(any())).thenReturn(Optional.of(member));

		Study study = new Study(10, "java");

		Optional<Member> findMember1 = memberService.findById(1L);
		Optional<Member> findMember2 = memberService.findById(2L);
		assertEquals("jinwook@gmail.com", findMember1.get().getEmail());
		assertEquals("jinwook@gmail.com", findMember2.get().getEmail());
	}

	@Test
	void createNewStudy_3(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {

		StudyService studyService = new StudyService(memberService, studyRepository);
		assertNotNull(studyService);

		Member member = new Member();
		member.setId(1L);
		member.setEmail("jinwook@gmail.com");

		//mock객체를 가지고 스터빙을 한 것(파라미터에 어떤 값이 들어오더라도 Optional<member>를 리턴함)
		when(memberService.findById(any())).thenReturn(Optional.of(member));

		Study study = new Study(10, "java");

		Optional<Member> findMember1 = memberService.findById(1L);
		Optional<Member> findMember2 = memberService.findById(2L);
		assertEquals("jinwook@gmail.com", findMember1.get().getEmail());
		assertEquals("jinwook@gmail.com", findMember2.get().getEmail());

		doThrow(new IllegalArgumentException()).when(memberService).validate(1L);

		assertThrows(IllegalArgumentException.class, () -> memberService.validate(1L));
	}

	@Test
	void createNewStudy_4(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {

		StudyService studyService = new StudyService(memberService, studyRepository);
		assertNotNull(studyService);

		Member member = new Member();
		member.setId(1L);
		member.setEmail("jinwook@gmail.com");

		//mock객체를 가지고 스터빙을 한 것 Optional<member>를 리턴함
		//첫번째는 정상적인 Optioncal<Member> 반환
		//두번째는 RuntimeException 예외 발생
		//세번째는 Optional.empty 반환
		when(memberService.findById(any()))
				.thenReturn(Optional.of(member))
				.thenThrow(new RuntimeException())
				.thenReturn(Optional.empty());

		Optional<Member> findMember = memberService.findById(1L);
		assertEquals("jinwook@gmail.com", findMember.get().getEmail());

		assertThrows(RuntimeException.class, () -> memberService.findById(2L));

		assertEquals(Optional.empty(), memberService.findById(3L));
	}

	@Test
	void createNewStudy_5(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {

		//given
		StudyService studyService = new StudyService(memberService, studyRepository);
		assertNotNull(studyService);

		Member member = new Member();
		member.setId(1L);
		member.setEmail("jinwook@gmail.com");

		Study study = new Study(10, "테스트");

		//mock객체를 가지고 스터빙을 한 것 Optional<member>를 리턴함
		when(memberService.findById(any())).thenReturn(Optional.of(member));
		when(studyRepository.save(study)).thenReturn(study);

		//when
		studyService.createNewStudy(1L, study);

		//then
		assertEquals(member.getId(), study.getOwnerId());

		//verify 메서드를 통해
		//memberService에서 study라는 매개변수를 가지고 notify()라는 메서드가 1번 호출이 되었어야한다
		//를 판단할 수 있다.
		verify(memberService, times(1)).notify(study);

		//특정 시점 이후에 아무일도 일어나지 않을경우
		verifyNoMoreInteractions(memberService);
		verify(memberService, never()).validate(any());
	}

	//BDD 스타일
	@Test
	void createNewStudy_6(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {

		//given
		StudyService studyService = new StudyService(memberService, studyRepository);
		assertNotNull(studyService);

		Member member = new Member();
		member.setId(1L);
		member.setEmail("jinwook@gmail.com");

		Study study = new Study(10, "테스트");

		//mock객체를 가지고 스터빙을 한 것 Optional<member>를 리턴함
		given(memberService.findById(1L)).willReturn(Optional.of(member));
		given(studyRepository.save(study)).willReturn(study);

		//when
		studyService.createNewStudy(1L, study);

		//then
		assertEquals(member.getId(), study.getOwnerId());

		//verify 메서드를 통해
		//memberService에서 study라는 매개변수를 가지고 notify()라는 메서드가 1번 호출이 되었어야한다
		//를 판단할 수 있다.
		then(memberService).should(times(1)).notify(study);

		//특정 시점 이후에 아무일도 일어나지 않을경우
		then(memberService).shouldHaveNoMoreInteractions();
		then(memberService).should(never()).validate(any());
	}

	@Test
	void stubbingTest(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {

		Study study = new Study(10, "테스트");

		Member member = new Member();
		member.setId(1L);
		member.setEmail("jinwook@gmail.com");

		//TODO memberService 객체에 findById 메서드를 1L 값으로 호출하면 Optional.of(member 객체를 리턴하도록 Stubbing
		when(memberService.findById(1L)).thenReturn(Optional.of(member));

		//TODO studyRepository 객체에 save 메서드를 study 객체로 호출하면 study 객체 그대로 리턴하도록 Stubbing
		when(studyRepository.save(study)).thenReturn(study);

		StudyService studyService = new StudyService(memberService, studyRepository);
		studyService.createNewStudy(1L, study);

		assertNotNull(study);
		assertEquals(member.getId(), study.getOwnerId());
	}

	@Test
	void mockitoTest(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {

		//given
		StudyService studyService = new StudyService(memberService, studyRepository);
		Study study = new Study(10, "더 자바, 테스트");

		//TODO studyRepository Mcok 객체의 save 메서드를 호출 시 study를 리턴하도록 만들기
		given(studyRepository.save(any())).willReturn(study);

		//when
		studyService.openStudy(study);

		//then
		//TODO study의 status가 OPENED로 변경됐는지 확인
		assertEquals(StudyStatus.OPENED, study.getStatus());

		//TODO study의 openDateTime이 Null이 아닌지 확인
		assertNotNull(study.getOpenedDateTime());

		//TODO memberService notify(study)가 호출 됐는지 확인
		then(memberService).should(times(1)).notify(study);
	}
}
