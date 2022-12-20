package f3f.dev1.domain.member.dao;

import f3f.dev1.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUserNameAndPhoneNumber(String username, String phoneNumber);

    Optional<Member> findByUserNameAndPhoneNumberAndEmail(String username, String phoneNumber, String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByIdAndPassword(Long id, String password);

    boolean existsByNickname(String nickname);
}
