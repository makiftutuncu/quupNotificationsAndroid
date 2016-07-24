package com.mehmetakiftutuncu.quupnotifications.utilities;

import java.util.Collection;
import java.util.Iterator;

public class StringUtils {
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String makeString(Collection collection, String prefix, String separator, String suffix) {
        if (collection == null) return "";

        StringBuilder stringBuilder = new StringBuilder(prefix);

        int size = collection.size();

        if (size > 0) {
            Iterator iterator = collection.iterator();

            for (int i = 0; iterator.hasNext(); i++) {
                stringBuilder.append(iterator.next());

                if (i < size - 1) {
                    stringBuilder.append(separator);
                }
            }
        }

        stringBuilder.append(suffix);

        return stringBuilder.toString();
    }

    public static String makeString(Collection collection, String separator) {
        return makeString(collection, "", separator, "");
    }
}
