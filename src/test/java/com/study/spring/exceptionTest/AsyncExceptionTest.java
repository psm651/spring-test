package com.study.spring.exceptionTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class AsyncExceptionTest {

    /**
     * 다른 스레드 내에서 발생한 예외를 그 스레드 내에서 try-catch로 잡아 처리해도 콘솔에는 아무런 흔적도 안 남을 수 있다.
     * 다른 스레드 내에서 발생한 예외는 바깥 스레드로 던져지지 않는다.
     * 따라서 다른 스레드를 사용하는 비동기 방식에서는
     * 예외 메시지 출력과 StackTrace 출력을 명시적으로 처리해줘야 하고,
     * 스레드 전용 예외 핸들러를 사용하는 것이 좋다.
     */
    @Test
    @DisplayName("쓰레드 예외 밖으로 나오는지 테스트")
    void threadTest() {
        log.info("Main Thread Name : " + Thread.currentThread().getName());

        try {
            Thread newThread = new Thread(
                    () -> {
                        throw new IllegalStateException();
                    }
            );
            newThread.setUncaughtExceptionHandler(
                    (thread, e) -> {
                        log.error(thread.getName() + "에서 예외처리");
                        throw new RuntimeException(thread.getName() + " 에서 예외잡아서 처리", e);
                    }
            );
            newThread.start();
        } catch (Exception e) {
            /**
             * 결론 : 여기로안온다
             */
            log.error("Main 에서 예외처리");
            throw new RuntimeException("MAIN에서 예외잡아 처리");
        }
    }
}
