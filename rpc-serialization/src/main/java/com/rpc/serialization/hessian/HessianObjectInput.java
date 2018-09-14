package com.rpc.serialization.hessian;

import com.caucho.hessian.io.HessianInput;
import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.util.LogUtil;
import com.rpc.serialization.api.ObjectInput;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-09-14 16:54
 * @Description:
 */
public class HessianObjectInput implements ObjectInput {
    private final byte[] data;

    public HessianObjectInput(byte[] data) {
        this.data = data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            HessianInput hi = new HessianInput(is);
            return (T) hi.readObject(cls);
        } catch (Exception e) {
            LogUtil.logError(HessianObjectInput.class, LogTipEnum.DESERIALIZATION_ERROR_TIP + e.getMessage());
        }
        return null;
    }

    @Override
    public Object readObject(Object object) throws IOException, ClassNotFoundException {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            HessianInput hi = new HessianInput(is);
            return hi.readObject();
        } catch (Exception e) {
            LogUtil.logError(HessianObjectInput.class, LogTipEnum.DESERIALIZATION_ERROR_TIP + e.getMessage());
        }
        return null;
    }
}
