package com.hdzx.tenement.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

/**
 * User: hope
 * Date: 2014/7/28
 * Description:
 */
public class IdCardUtil {

    /**
     * 中国公民身份证号码最小长度
     */
    public static final int CHINA_ID_MIN_LENGTH = 15;
    /**
     * 中国公民身份证号码最大长度
     */
    public static final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 提取身份证信息.
     *
     * @param idCard 身份证号码
     * @return 提取身份证信息
     */
    public static IdCardEntity getIdCardEntity(String idCard) {
        // 长度
        int card_length = idCard.length();
        if (card_length == CHINA_ID_MIN_LENGTH || card_length == CHINA_ID_MAX_LENGTH) {
            if (card_length == CHINA_ID_MIN_LENGTH) {
                // 15位转成18位
                idCard = convert15IdCarTo18(idCard);
            }
            // ****************************性别（0男1女）**********************************
            // 获取性别
            String id17 = idCard.substring(16, 17);
            String gender = "1";
            if (Integer.parseInt(id17) % 2 != 0) {
                gender = "0";
            }
            // ******************************出生日期***********************************
            // 出生日期
            String birthday = idCard.substring(6, 14);
            Date birthdate = DateUtil.stringToDate(birthday, "yyyyMMdd");
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(birthdate);
            // ***********************************************************************************
            IdCardEntity idCardEntity = new IdCardEntity();
            idCardEntity.setBirthday(birthdate);
            idCardEntity.setYear(gregorianCalendar.get(Calendar.YEAR));
            idCardEntity.setMonth(gregorianCalendar.get(Calendar.MONTH) + 1);
            idCardEntity.setDay(gregorianCalendar.get(Calendar.DAY_OF_MONTH));
            idCardEntity.setGender(gender);
            return idCardEntity;
        }
        return null;
    }

    /**
     * 15位和18位身份证号码的基本数字和位数验校.
     *
     * @param idCard the id card
     * @return true, if is id card
     */
    public static boolean isIdCard(String idCard) {
        return !(idCard == null || "".equals(idCard)) && Pattern.matches("(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)", idCard);
    }

    /**
     * 将15位的身份证转成18位身份证.
     *
     * @param idCard 身份证号码
     * @return 将15位的身份证转成18位身份证
     */
    public static String convert15IdCarTo18(String idCard) {
        String idCard17;
        // 非15位身份证
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (NumberUtils.isNumber(idCard)) {
            // 获取出生年月日
            String birthday = idCard.substring(6, 12);
            Date birthdate = DateUtil.stringToDate(birthday, "yyMMdd");
            Calendar cday = Calendar.getInstance();
            cday.setTime(birthdate);
            String year = String.valueOf(cday.get(Calendar.YEAR));
            idCard17 = idCard.substring(0, 6) + year + idCard.substring(8);
            char[] c = idCard17.toCharArray();
            String checkCode;
            // 将字符数组转为整型数组
            int[] bit = converCharsToInts(c);
            int sum17 = getPowerSum(bit);
            // 获取和值与11取模得到余数进行校验码
            checkCode = getCheckCodeBySum(sum17);
            // 获取不到校验位
            if (null == checkCode) {
                return null;
            }
            // 将前17位与第18位校验码拼接
            idCard17 += checkCode;
        } else { // 身份证包含数字
            return null;
        }
        return idCard17;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值.
     *
     * @param bit the bit
     * @return 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     */
    private static int getPowerSum(int[] bit) {
        int sum = 0;
        if (power.length != bit.length) {
            return sum;
        }
        for (int i = 0; i < bit.length; ++i) {
            for (int j = 0; j < power.length; ++j) {
                if (i == j) {
                    sum = sum + bit[i] * power[j];
                }
            }
        }
        return sum;
    }

    /**
     * 将和值与11取模得到余数进行校验码判断.
     *
     * @param sum17 the sum17
     * @return 将和值与11取模得到余数进行校验码判断
     */
    private static String getCheckCodeBySum(int sum17) {
        String checkCode = null;
        switch (sum17 % 11) {
            case 10:
                checkCode = "2";
                break;
            case 9:
                checkCode = "3";
                break;
            case 8:
                checkCode = "4";
                break;
            case 7:
                checkCode = "5";
                break;
            case 6:
                checkCode = "6";
                break;
            case 5:
                checkCode = "7";
                break;
            case 4:
                checkCode = "8";
                break;
            case 3:
                checkCode = "9";
                break;
            case 2:
                checkCode = "x";
                break;
            case 1:
                checkCode = "0";
                break;
            case 0:
                checkCode = "1";
                break;
        }
        return checkCode;
    }

    /**
     * 将字符数组转为整型数组.
     *
     * @param chars 字符数组
     * @return 将字符数组转为整型数组
     */
    private static int[] converCharsToInts(char[] chars) {
        int[] ints = new int[chars.length];
        int k = 0;
        for (char char_temp : chars) {
            ints[k++] = Integer.parseInt(String.valueOf(char_temp));
        }
        return ints;
    }

    // 每位加权因子
    private static int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
}
