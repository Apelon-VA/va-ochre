package org.ihtsdo.otf.uuidnidmap;

import java.util.UUID;

public class UuidUtil {

    public static long[] convert(UUID id) {
        long[] data = new long[2];
        data[0] = id.getMostSignificantBits();
        data[1] = id.getLeastSignificantBits();
        return data;
    }

    public static UUID convert(long[] data) {
        return new UUID(data[0], data[1]);
    }
}
