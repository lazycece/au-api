package com.lazycece.au.api.token;

import com.alibaba.fastjson.JSON;
import com.lazycece.au.api.token.serialize.Serializer;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author lazycece
 * @date 2019/11/14
 */
public class TokenHolderTest {

    private static final String SECRET = "e0e81f5a856d42ea9f834dc7dbc1609e";
    private TestSubject testSubject;

    @Before
    public void init() {
        testSubject = new TestSubject();
        testSubject.setUserId("1");
    }

    @Test
    public void test() throws Exception {
        TokenHolder tokenHolder = TokenHolder.build(SECRET);
        String token = tokenHolder.generateToken(testSubject);

        boolean valid = tokenHolder.verification(token);
        assertThat(valid).isEqualTo(true);

        Subject subject = tokenHolder.parseToken(token);
        assertThat(subject).isInstanceOf(TestSubject.class);
    }

    @Test
    public void testAdvance() throws Exception {
        TokenHolder tokenHolder = TokenHolder.build(SECRET)
                .issuer("AU-TEST-ISSUER")
                .expire(10 * 60 * 1000)
                .tokenHeader("AU-TEST-HEADER")
                .serializer(new TestSerializer());
        String token = tokenHolder.generateToken(testSubject);

        boolean valid = tokenHolder.verification(token);
        assertThat(valid).isEqualTo(true);

        Subject subject = tokenHolder.parseToken(token);
        assertThat(subject).isInstanceOf(TestSubject.class);
    }


    private static class TestSubject implements Subject {

        private String userId;

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    private static class TestSerializer implements Serializer<TestSubject> {

        @Override
        public String serialize(TestSubject testSubject) throws Exception {
            return JSON.toJSONString(testSubject);
        }

        @Override
        public TestSubject deserialize(String str) throws Exception {
            return JSON.parseObject(str, TestSubject.class);
        }
    }
}
