package hello.thejavatest.member;

import hello.thejavatest.domain.Member;
import hello.thejavatest.domain.Study;
import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId);

    void validate(Long memberId);

    void notify(Study newstudy);

    void notify(Member member);
}
