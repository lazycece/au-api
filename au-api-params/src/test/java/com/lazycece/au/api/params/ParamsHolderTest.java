package com.lazycece.au.api.params;

import com.lazycece.au.api.params.crypt.AESCrypto;
import com.lazycece.au.api.params.utils.SaltUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author lazycece
 * @date 2019/11/23
 */
public class ParamsHolderTest {

    private static final String SECRET = "e0e81f5a856d42ea9f834dc7dbc1609e";

    @Test
    public void test() throws Exception {
        ParamsHolder paramsHolder = ParamsHolder.build(SECRET).dataCrypto(new AESCrypto());
        String data = "encode and decode test!";
        String salt = SaltUtils.generateSalt();
        assertThat(data).isEqualTo(paramsHolder.decode(salt, paramsHolder.encode(salt, data)));
    }
}
