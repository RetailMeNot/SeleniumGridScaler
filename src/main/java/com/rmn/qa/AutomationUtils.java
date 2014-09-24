/*
 * Copyright (C) 2014 RetailMeNot, Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 */

package com.rmn.qa;

import java.util.Calendar;
import java.util.Date;

/**
 * Util methods
 * @author mhardin
 */
public final class AutomationUtils {

    /**
     * Modifies the specified date
     * @param dateToModify Date to modify
     * @param unitsToModify Number of units to modify (e.g. 6 for 6 seconds)
     * @param unitType Measurement type (e.g. Calendar.SECONDS)
     * @return Modified date
     */
    public static Date modifyDate(Date dateToModify,int unitsToModify,int unitType) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateToModify);
        // Add 60 seconds so we're as close to the hour as we can be instead of adding 55 again
        c.add(unitType,unitsToModify);
        return c.getTime();
    }

    /**
     * Returns true if the current time is after the specified date, false otherwise
     * @param dateToCheck Date to check against the current time
     * @param unitsToCheckWith Number of units to add/subtract from dateToCheck
     * @param unitType Unit type (e.g. Calendar.MINUTES)
     * @return
     */
    public static boolean isCurrentTimeAfterDate(Date dateToCheck,int unitsToCheckWith,int unitType) {
        Date targetDate = AutomationUtils.modifyDate(dateToCheck,unitsToCheckWith,unitType);
        return new Date().after(targetDate);
    }

    /**
     * Returns true if the strings are lower case equal
     * @param string1 First string to compare
     * @param string2 Second string to compare
     * @return
     */
    public static boolean lowerCaseMatch(String string1, String string2) {
        string2 = string2.toLowerCase().replace(" ","");
        return string2.equals(string1.toLowerCase().replace(" ", ""));
    }

}
