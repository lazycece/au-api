package com.lazycece.au.api.example.entity.dos;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lazycece
 */
@Getter
@Setter
public class ApiParams extends com.lazycece.au.api.params.ApiParams {
    private String deviceId;
    private String version;
    private Integer versionCode;

    @Override
    public boolean validate() {
        return super.validate()
                && StringUtils.isNotBlank(deviceId)
                && StringUtils.isNotBlank(version)
                && versionCode!=null;

    }
}
