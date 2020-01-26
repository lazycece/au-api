package com.lazycece.au.api.params;

/**
 * Param validator
 *
 * @author lazycece
 * @date 2019/11/22
 */
public interface Validator {

    /**
     * validate params
     *
     * @return <code>true</code> that indicates checking passed,
     * otherwise <code>false</code>
     */
    boolean validate();
}
