package microservices.book.socialmultiplication.service;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.User;
import microservices.book.socialmultiplication.repository.MultiplicationRepository;
import microservices.book.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.socialmultiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService{

    private final RandomGeneratorService randomGeneratorService;
    private final MultiplicationResultAttemptRepository attemptRepository;
    private final UserRepository userRepository;
    private final MultiplicationRepository multiplicationRepository;

    @Autowired
    public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
                                     final MultiplicationResultAttemptRepository attemptRepository,
                                     final UserRepository userRepository,
                                     final MultiplicationRepository multiplicationRepository) {
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.multiplicationRepository = multiplicationRepository;
    } // MultiplicationServiceImpl 생성자

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    } // createRandomMultiplication 메서드

    @Transactional
    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt attempt) {

        //해당 닉네임의 사용자가 존재하는지 확인
        Optional<User> existingUser = userRepository.findByAlias(attempt.getUser().getAlias());
        User user = existingUser.orElseGet(() -> userRepository.save(attempt.getUser()));

        //곱셈 문제 존재 여부 확인
        Optional<Multiplication> existingMultiplication = multiplicationRepository
                                                .findByFactorAAndFactorB(attempt.getMultiplication().getFactorA(),
                                                                         attempt.getMultiplication().getFactorB());
        Multiplication multiplication = existingMultiplication.orElseGet(() ->
                                                    multiplicationRepository.save(attempt.getMultiplication()));

        //조작된 답안을 방지
        Assert.isTrue(!attempt.isCorrect(), "채점한 채로 보낼 수 없습니다!!");

        boolean correct = attempt.getResultAttempt() ==
                attempt.getMultiplication().getFactorA() * attempt.getMultiplication().getFactorB();

        //조작된 답안을 방지
        Assert.isTrue(!attempt.isCorrect(), "채점한 상태로 보낼 수 없습니다!");

        //복사본을 만들고 correct 필드를 상황에 맞게 설정
        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
                user,
                multiplication,
                attempt.getResultAttempt(),
                correct);

        attemptRepository.save(checkedAttempt);

        return correct;
    } // checkAttempt 메서드

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }

}
