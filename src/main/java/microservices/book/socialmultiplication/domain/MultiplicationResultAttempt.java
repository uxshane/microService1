package microservices.book.socialmultiplication.domain;

/*
* {@link User}가 {@link Multiplication}을 계산한 답안을 정의한 클래스
* */

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class MultiplicationResultAttempt {
    private final User user;
    private final Multiplication multiplication;
    private final int resultAttmpt;

    //JSON (역)직렬화를 위한 빈 생성자
    MultiplicationResultAttempt() {
        user = null;
        multiplication = null;
        resultAttmpt = -1;
    }
}
