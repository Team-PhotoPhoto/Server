package com.hjin.photophoto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest
class PhotophotoApplicationTests {

    @Test
    void contextLoads() {
    }

}
//테스트
//회귀테스트:
// api 를 눈으로 검증하면?
// 만약 코드를 수정하면 기존에 작동하던 코드가 작동하지 않을 수 있음(리팩토링 등) -> 이전에 검증된 100개의 것들을 다시 검증?
// 주기적으로 돌면서 기존에 되던 거 되는지 체크하는 거, 자동화

// 테스트 할 때마다 디비 연결하면 너무 느림, 테스트는 빨라야 함, 디비가 아니라 메소드를 검증하는 거임
