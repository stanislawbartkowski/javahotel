/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.google.gwt.user.client.rpc.core.com.jythonui.shared;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.gwtmodel.table.common.ConvertTT;
import com.gwtmodel.table.common.TT;
import com.jythonui.shared.FieldValue;

public final class FieldValue_CustomFieldSerializer extends
        CustomFieldSerializer<FieldValue> {

    @Override
    public void deserializeInstance(SerializationStreamReader streamReader,
            FieldValue instance) throws SerializationException {
        FieldValue_CustomFieldSerializer.deserialize(streamReader, instance);

    }

    public static void deserialize(SerializationStreamReader streamReader,
            FieldValue instance) throws SerializationException {
        TT type = (TT) streamReader.readObject();
        int afterdot = streamReader.readInt();
        String s = streamReader.readString();
        Object o = ConvertTT.toO(type, s);
        instance.setValue(type, o, afterdot);
    }

    public static void serialize(SerializationStreamWriter streamWriter,
            FieldValue instance) throws SerializationException {
        String s = ConvertTT.toS(instance.getValue(), instance.getType(),
                instance.getAfterdot());
        streamWriter.writeObject(instance.getType());
        streamWriter.writeInt(instance.getAfterdot());
        streamWriter.writeString(s);
    }

    @Override
    public void serializeInstance(SerializationStreamWriter streamWriter,
            FieldValue instance) throws SerializationException {
        FieldValue_CustomFieldSerializer.serialize(streamWriter, instance);
    }

}
