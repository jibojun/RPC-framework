package com.rpc.serialization.hessian;

import com.caucho.hessian.io.HessianOutput;
import com.rpc.common.configuration.LogTipEnum;
import com.rpc.common.util.LogUtil;
import com.rpc.serialization.api.ObjectOutput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-09-14 16:54
 * @Description:
 */
public class HessianObjectOutput implements ObjectOutput {
    private final Object object;

    public HessianObjectOutput(Object object) {
        this.object = object;
    }

    @Override
    public void writeObject() throws IOException {

    }

    @Override
    public byte[] writeObjectAndReturn() throws IOException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            HessianOutput ho = new HessianOutput(os);
            ho.writeObject(object);
            return os.toByteArray();
        } catch (Exception e) {
            LogUtil.logError(HessianObjectOutput.class, LogTipEnum.SERIALIZATION_ERROR_TIP + e.getMessage());
        }
        return null;
    }
}
