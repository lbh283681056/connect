package com.base.connect.http;


import com.base.connect.http.bean.BaseResultWrapper;

import io.reactivex.functions.Function;

public class ApiResultVerifyFactory {

    //坑爹的是我们这边数据接口并不是固定 打死思传
    //返回值验证
    public static class ApiResultVerify implements Function<BaseResultWrapper, BaseResultWrapper> {
        @Override
        public BaseResultWrapper apply(BaseResultWrapper baseResultWrapper) throws Exception {
            if (baseResultWrapper == null || baseResultWrapper.code == null) {
                throw new ApiExection(ApiExection.ERROR_CODE);
            }
            return baseResultWrapper;
        }
    }
}
