package microservices.book.socialmultiplication.service;


import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.User;
import microservices.book.socialmultiplication.repository.MultiplicationRepository;
import microservices.book.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.socialmultiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class MultiplicationServiceImplTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultiplicationRepository multiplicationRepository;

    @BeforeEach
    public void setUp() {
        //목 객체를 초기화합니다.
        MockitoAnnotations.openMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService,
                                                                    attemptRepository,
                                                                    userRepository,
                                                                    multiplicationRepository);
    }

    @Test
    public void createRandomMultiplicationTest() {
        //given(목 객체가 처음에 50, 나중에 30을 반환하도록 설정)
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        //when
        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        //assert
        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    public void checkCorrectAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("John_Doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user,
                multiplication, 3000, false);
        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(
                user, multiplication, 3000, true);

        //given
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //assert
        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("John_Doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user,
                multiplication, 3010, false);

        //given
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //assert
        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
    }

    @Test
    public void retrieveStatsTest() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("John_Doe");
        MultiplicationResultAttempt attempt1 =
                new MultiplicationResultAttempt(user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 =
                new MultiplicationResultAttempt(user, multiplication, 3051, false);

        List<MultiplicationResultAttempt> latestAttempts =
                Lists.newArrayList(attempt1, attempt2);

        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("john_doe"))
                .willReturn(latestAttempts);

        //when
        List<MultiplicationResultAttempt> latestAttemptsResult =
                multiplicationServiceImpl.getStatsForUser("john_doe");

        //then
        assertThat(latestAttemptsResult).isEqualTo(latestAttempts);
    }

}










