package com.rpc.serialzation.test;

import com.rpc.serialization.api.ObjectInput;
import com.rpc.serialization.api.ObjectOutput;
import com.rpc.serialization.protostuff.ProtoStuffObjectInput;
import com.rpc.serialization.protostuff.ProtoStuffObjectOutput;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * @Author: Bojun Ji
 * @Description:
 * @Date: 2018/7/21_6:15 PM
 */
public class ProtoStuffTest {

    @Test
    public void testSerializationAndDeserialization() throws IOException, ClassNotFoundException {
        TestObject obj = new TestObject(1, "testProtoStuff");
        ObjectOutput objectOutput = new ProtoStuffObjectOutput(obj);
        byte[] data = objectOutput.writeObjectAndReturn();
        assertThat(data, not(nullValue()));
        System.out.println("serialization result:" + data);
        System.out.println("serialization result size:" + data.length);
        ObjectInput objectInput = new ProtoStuffObjectInput(TestObject.class, data);
        TestObject resultObj = objectInput.readObject(TestObject.class);
        assertThat(resultObj, not(nullValue()));
        assertThat(resultObj.getId(), is(1));
        assertThat(resultObj.getName(), is("testProtoStuff"));
    }
}
