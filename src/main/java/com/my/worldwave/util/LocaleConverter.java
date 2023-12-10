package com.my.worldwave.util;

import java.util.Locale;

public class LocaleConverter {

    public static String convert(String locale) {
        if (locale == null) return "";

        String[] part = locale.split("[-_]");
        // ko인 경우 KR로 수동 변환 필요
        if (part[0].equalsIgnoreCase("ko")) {
            // 수동 변환 국가가 3개 넘어가면 다른 방법 사용하기 (zh -> CN 등)
            return Locale.KOREA.getCountry();
        } else if (part.length == 1) {
            return getCountryCode(part[0]).toUpperCase();
        } else if (part.length == 2) {
            // ko_KR인 경우 2번째 덩어리를 국가 코드로 사용
            return getCountryCode(part[1]).toUpperCase();
        } else {
            return "";
        }
    }

    private static String getCountryCode(String language) {
        try {
            return new Locale(language).getCountry();
        } catch (Exception e) {
            // 변환에 실패한 경우 빈 문자열 처리 후 기타 국가로 표시
            return "";
        }
    }

}
