package microservices.book.socialmultiplication.repository;

import microservices.book.socialmultiplication.domain.Multiplication;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * {@link Multiplication}을 저장하고 조회하기 위한 인터페이스
 */
public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {

    /**
     * @return factorA와 factorB 값이 똑같은 걸 가져오기
     */
    Optional<Multiplication> findByFactorAAndFactorB(int factorA, int factorB);
}
