/* 
 * Copyright 2014 nqcx.org All right reserved. This software is the 
 * confidential and proprietary information of nqcx.org ("Confidential 
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.commons.util.email;

import java.util.regex.Pattern;

/**
 * Email 工具
 * 
 * @author naqichuan 2014年8月14日 上午11:48:45
 */
public class EmailValidatorUtils {

    /**
     * Email address pattern
     */
    public static final String emailAddressPattern = "\\b(^['_A-Za-z0-9-]+(\\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";

    /**
     * validate email
     * 
     * @param value
     * @return
     */
    public static boolean validate(String value) {
        return validate(value, false, true);
    }

    /**
     * validate email
     * 
     * @param value
     * @param caseSensitive
     * @param trim
     * @return
     */
    public static boolean validate(String value, boolean caseSensitive, boolean trim) {

        if (value == null || value.trim().length() == 0)
            return false;

        Pattern pattern;
        if (caseSensitive)
            pattern = Pattern.compile(emailAddressPattern);
        else pattern = Pattern.compile(emailAddressPattern, Pattern.CASE_INSENSITIVE);

        if (trim)
            value = value.trim();

        return pattern.matcher(value).matches();
    }
}
