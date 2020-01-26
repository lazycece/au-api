package com.lazycece.au.api.token.serialize;

import com.lazycece.au.api.token.Subject;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Default serializer for ${@link Subject}.
 * <p>
 * The serializer uses base64 in order to avoid a problem about
 * while use {@link ByteArrayOutputStream#toString()} directly.
 *
 * @author lazycece
 * @date 2019/11/09
 */
public class SubjectSerializer implements Serializer<Subject> {

    @Override
    public String serialize(Subject subject) throws Exception {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(subject);
            byte[] base64Encode = Base64.encodeBase64(bos.toByteArray());
            return new String(base64Encode, StandardCharsets.UTF_8);
        }
    }

    @Override
    public Subject deserialize(String str) throws Exception {
        byte[] base64Decode = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        try (ByteArrayInputStream bis = new ByteArrayInputStream(base64Decode);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Subject) ois.readObject();
        }
    }
}
