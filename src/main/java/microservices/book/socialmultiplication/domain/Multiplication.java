package microservices.book.socialmultiplication.domain;

/*
* 애플리케이션에서 곱셈을 나타내는 클래스
* */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class Multiplication {

    @Id
    @GeneratedValue
    @Column(name = "MULTIPLICATION_ID")
    private Long id;

    //두 인수
    private final int factorA;
    private final int factorB;

    protected Multiplication() {
        this(0,0);
    }
}

//--구 버전--
//public class Multiplication {
//
//    //인수
//    private int factorA;
//    private int factorB;
//
//    // A * B의 결과
//    private int result;
//
//    public Multiplication(int factorA, int factorB) {
//        this.factorA = factorA;
//        this.factorB = factorB;
//        this.result = factorA * factorB;
//    }
//
//    public int getFactorA() {
//        return factorA;
//    }
//
//    public int getFactorB() {
//        return factorB;
//    }
//
//    public int getResult() {
//        return result;
//    }
//
//    @Override
//    public String toString() {
//        return "Multiplication{" +
//                "factorA=" + factorA +
//                ", factorB=" + factorB +
//                ", result(A*B)=" + result +
//                "}";
//    }
//}






















