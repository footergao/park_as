package com.hdzx.tenement.utils;

import java.util.Date;

/**
 * User: hope
 * Date: 2014/7/28
 * Description:
 */
public class IdCardEntity {
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 年份.
     */
    private int year;

    /**
     * 月份.
     */
    private int month;

    /**
     * 日期.
     */
    private int day;

    /**
     * 性别.
     */
    private String gender;

    /**
     * 出生日期.
     */
    private Date birthday;

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "年份:" + year + "月份:" + month + "" + "日期:" + day + "性别：" + gender
                + ",出生日期：" + birthday;
    }

    /**
     * Gets the 年份.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the 年份.
     *
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the 月份.
     *
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Sets the 月份.
     *
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Gets the 日期.
     *
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets the 日期.
     *
     * @param day the day to set
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Gets the 性别.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the 性别.
     *
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the 出生日期.
     *
     * @return the birthday
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * Sets the 出生日期.
     *
     * @param birthday the birthday to set
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

}

