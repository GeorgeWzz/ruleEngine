package com.wuzizhuo.util;



import java.util.ArrayList;
import java.util.List;

/**
 * @author zoucaihui
 * @created 2018/7/9
 * @updated
 * @description:字符串工具类
 **/
public class StringUtil {

    public static final String EMPTY_STRING = "";

    public static boolean isEmpty(String s) {
        return (s == null || "".equals(s));
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }


    public static boolean equals(String l, String r) {
        if (l == null) {
            return r == null;
        } else {
            return l.equals(r);
        }
    }

    public static boolean startsWith(String sourceStr, String str) {
        if (sourceStr == null || str == null) {
            return false;
        }

        return sourceStr.startsWith(str);
    }

    public static boolean notEquals(String l, String r) {
        return !equals(l, r);
    }

//    public static void main(String[] args) {
////        List<ApplyCardReqDTO> list = Lists.newArrayList();
////        ApplyCardReqDTO a = new ApplyCardReqDTO();
////        a.setCardNo("a");
////        ApplyCardReqDTO b = new ApplyCardReqDTO();
////        b.setCardNo("b");
////        ApplyCardReqDTO c = new ApplyCardReqDTO();
////        c.setCardNo("c");
////        list.add(a);
////        list.add(b);
////        list.add(c);
//
////       String result =  StringUtil.compose(",",list,ApplyCardReqDTO::getCardNo);
//        String result = StringUtil.compose(",", Arrays.asList("a", "b", "c"), Function.identity());
//        System.out.println(result);
//    }

    public static List<String> split(String input, char splitChar) {
        List<String> wordList = new ArrayList<String>();
        if (StringUtil.isEmpty(input)) {
            return wordList;
        }
        int frontIndex = 0;
        char[] inputCharArray = input.toCharArray();
        for (int currentIndex = 0; currentIndex < input.length(); currentIndex++) {
            if (inputCharArray[currentIndex] == splitChar) {
                wordList.add(input.substring(frontIndex, currentIndex));
                frontIndex = currentIndex + 1;
            }
        }
        wordList.add(input.substring(frontIndex));

        return wordList;
    }
}
