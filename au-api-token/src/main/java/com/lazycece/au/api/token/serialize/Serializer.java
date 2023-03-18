package com.lazycece.au.api.token.serialize;

import com.lazycece.au.api.token.Subject;


/**
 * Serializer for ${@link Subject}
 *
 * @author lazycece
 * @date 2019/11/09
 */
public interface Serializer {

    String serialize(Subject subject) throws Exception;

    Subject deserialize(String str) throws Exception;
}
