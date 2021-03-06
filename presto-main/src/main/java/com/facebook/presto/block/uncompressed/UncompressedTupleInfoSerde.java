/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.block.uncompressed;

import com.facebook.presto.tuple.TupleInfo;
import com.google.common.collect.ImmutableList;
import io.airlift.slice.SliceInput;
import io.airlift.slice.SliceOutput;

import static com.google.common.base.Preconditions.checkNotNull;

// TODO: give this a real implementation, this is just a hack for now
public class UncompressedTupleInfoSerde
{
    /**
     * @param tupleInfo
     * @param sliceOutput
     * @return number of bytes written
     */
    public static int serialize(TupleInfo tupleInfo, SliceOutput sliceOutput)
    {
        checkNotNull(tupleInfo, "tupleInfo is null");
        checkNotNull(sliceOutput, "sliceOutput is null");

        sliceOutput.writeByte(tupleInfo.getFieldCount());
        int bytes = 1;

        for (TupleInfo.Type type : tupleInfo.getTypes()) {
            sliceOutput.writeByte(type.ordinal());
            bytes++;
        }
        return bytes;
    }

    public static TupleInfo deserialize(SliceInput sliceInput)
    {
        checkNotNull(sliceInput, "slice is null");

        int fieldCount = sliceInput.readUnsignedByte();
        ImmutableList.Builder<TupleInfo.Type> builder = ImmutableList.builder();
        for (int i = 0; i < fieldCount; i++) {
            builder.add(TupleInfo.Type.values()[sliceInput.readUnsignedByte()]);
        }
        return new TupleInfo(builder.build());
    }
}
