package com.wy.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.wy.lang.StrTool;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:08:36
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class CommonUtils {
	public static String toAmount(long amount) {
        return (new BigDecimal(amount)).divide(new BigDecimal(100)).toString();
    }

    public static String toDate(Date date) {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
    }

    public static boolean isEmpty(Object object) {
        if (object instanceof String) {
			return StrTool.isEmpty((String)object);
        } else {
            return object == null;
        }
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static <T> boolean isListNotEmpty(List<T> list) {
        return list != null && list.size() > 0;
    }

    public static <T> boolean isListEmpty(List<T> list) {
        return !isListNotEmpty(list);
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }
    }
}