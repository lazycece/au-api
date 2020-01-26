package com.lazycece.au.api.token;

import java.io.Serializable;

/**
 * Token's subject
 * <p>
 * You need implement this interface for token context, and you can
 * get subject information by ${@link SubjectContext#getContext()},
 *
 * @author lazycece
 */
public interface Subject extends Serializable {
}
