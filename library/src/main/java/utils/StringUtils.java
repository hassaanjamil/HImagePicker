package utils;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * Function returns the ellipsized string only if outLength is greater than text length.<br></br>
     * e.g. abcdefghijklmnop AS abcdefghij... IF outLength=1--stack0
     */
    public static String getEllipsizedSubstring(String text, int outLength) {
        if (text.length() < outLength)
            return text;
        else
            return text.substring(0, outLength - 1) + "...";
    }


    /**
     * Method is used for checking valid email format.
     *
     * @return boolean true for valid, false for invalid
     */
    @SuppressWarnings("unused")
    public static boolean isValidEmail(String email) {
        boolean isValid = false;

        //String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Method is used for checking valid email format.
     *
     * @return boolean true for valid, false for invalid
     */
    public static boolean isValidEmail2(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Method is used for checking web url format.
     *
     * @return boolean true for valid, false for invalid
     */
    public static boolean isValidWebURL(String text) {
        return isValidString(text) || Patterns.WEB_URL.matcher(text).matches()
                && (text.trim().startsWith("http://") || text.trim().startsWith("https://"));
    }

    /**
     * Method is used for checking valid String.
     *
     * @return boolean true for valid, false for invalid
     */
    public static boolean isValidString(String str) {
        return str != null && !(TextUtils.isEmpty(str.trim()) || str.trim().equals("") || str.trim().equals("null"));
    }

// --Commented out by Inspection START (2017-12-28 7:07 PM):
//    /**
//     * Function returns the occurrence of <b>findText</b> in <b>text</b>
//     */
//    @SuppressWarnings("SameParameterValue")
//    public static int getOccurrenceCountOfSubstring(String text, String findText) {
//        int lastIndex = 0;
//        int count = 0;
//
//        while (lastIndex != -1) {
//            lastIndex = text.indexOf(findText, lastIndex);
//            if (lastIndex != -1) {
//                count++;
//                lastIndex += findText.length();
//            }
//        }
//        return count;
//    }
// --Commented out by Inspection STOP (2017-12-28 7:07 PM)

// --Commented out by Inspection START (2017-12-28 7:07 PM):
//    /**
//     * Function will return the string from last occurrence of <b>findText</b> in
//     * <b>text</b> if found, otherwise, will return the string unchanged.
//     */
//    public static String getStringFromLastOccurrence(String text, String findText) {
//        int indexOfLastOccurrence = text.lastIndexOf(findText);
//        if (indexOfLastOccurrence != -1)
//            return text.substring(indexOfLastOccurrence);
//        else
//            return text;
//    }
// --Commented out by Inspection STOP (2017-12-28 7:07 PM)

    /**
     * Function will return concatenated strings by comma
     *
     * @param stringArray String array containing strings to be concatenated in sequence
     */
    public static String concatenateStrings(String[] stringArray) {
        StringBuilder address = new StringBuilder();
        if (stringArray != null && stringArray.length > 0) {
            for (int i = 0; i < stringArray.length; i++) {
                if (isValidString(stringArray[i])) {
                    if (i != 0 && isValidString(address.toString()))
                        address.append(", ");
                    address.append(stringArray[i]);
                }
            }
        }
        return address.toString();
    }

    /**
     * Function will return Initial letters of string
     *
     * @param userName User Name of Profile
     */
    public static String getTextDawableText(String userName) {
        StringBuilder text = new StringBuilder();
        if (!isValidString(userName)) {
            return text.toString();
        }
        userName = userName.trim();
        String[] arr = userName.split(" ");

        // Remove any emoty string
        ArrayList<String> chars = new ArrayList<>(Arrays.asList(arr));
        chars.removeAll(Collections.singleton(""));
        chars.removeAll(Collections.singleton(" "));

        arr = chars.toArray(arr);

        if (arr.length < 2) {
            text = new StringBuilder("" + userName.charAt(0));
        } else {
            for (int i = 0; i < 2; i++) {
                text.append(arr[i].charAt(0));
            }
        }
        return text.toString().toUpperCase();
    }

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    @SuppressWarnings("unused")
    public static int getRepeatedCharacterCountBasedIndex(String value, String character, int repeat) {
        int start = value.indexOf(character);

        for (int i = start; i < repeat - 1 && start != -1; i++) {
            //System.out.println("Found / at: " + start);
            start = value.indexOf(character, ++start);
        }

        return start;
    }

    private static final String TAG = StringUtils.class.getSimpleName();

    /**
     * Function will return the sub string of <b>dest</b> before <b>src</b> if exists. Otherwise,
     * It will return <b>dest</b>
     *
     * @param src  source string
     * @param dest destination string
     * @return String
     */
    public static String getSubStringBeforeSource(String src, String dest) {
        if (!isValidString(src) || !isValidString(dest)) {
            Log.e(TAG, "::getSubStringBeforeSource(): Arguments are not valid");
            return null;
        }

        if (!dest.contains(src))
            return dest;

        return dest.substring(0, dest.indexOf(src));
    }

    /**
     * Function will return the sub string from <b>dest</b> after <b>src</b> if exists. Otherwise,
     * It will return <b>dest</b>
     *
     * @param src  source string
     * @param dest destination string
     * @return String
     */
    public static String getSubStringAfterSource(String src, String dest) {
        if (!isValidString(src) || !isValidString(dest)) {
            Log.e(TAG, "::getSubStringAfterSource(): Arguments are not valid");
            return null;
        }

        if (!dest.contains(src))
            return dest;

        return dest.substring(src.length(), dest.length());
    }

    /**
     * Function will return the sub string from <b>dest</b> after <b>src</b> last occurrence if exists. Otherwise,
     * It will return <b>dest</b>
     *
     * @param src  source string
     * @param dest destination string
     * @return String
     */
    @SuppressWarnings("unused")
    public static String getSubStringAfterSourceLastOccurrence(String src, String dest) {
        if (!isValidString(src) || !isValidString(dest)) {
            Log.e(TAG, "::getSubStringAfterSource(): Arguments are not valid");
            return null;
        }

        if (!dest.contains(src))
            return dest;

        return dest.substring(dest.lastIndexOf(src) + 1, dest.length());
    }

    public static String removeAllWhiteSpacesAndHiddenCharacterForString(String text) {
        if (isValidString(text))
            return text.replaceAll("\\s+", "");
        else
            return null;
    }

// --Commented out by Inspection START (2017-12-28 7:07 PM):
//    public static String getDecimalLimitString(int decimalCount, double value) {
//        try {
//            return String.format("%." + decimalCount + "f", value);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
// --Commented out by Inspection STOP (2017-12-28 7:07 PM)

    public static boolean isValidPhoneNo(String phoneNo) {
        return Patterns.PHONE.matcher(phoneNo).matches() && lastIndexOfChar(phoneNo, '+') <= 0
                && phoneNo.length() >= 7;
    }

    private static int lastIndexOfChar(String fullSring, Character ch) {
        int index = -1;
        for (int i = 0; i < fullSring.length(); i++)
            if (fullSring.charAt(i) == ch)
                index = i;
        return index;
    }

    private static String getStringForCharset(String value, String charsetName) {

        if (value == null)
            return "";

        if (charsetName == null)
            return value;

        return new String(value.getBytes(Charset.forName(charsetName)));
    }

    public static String convertStringToArabicForCharset(String value, String charsetName) {
        if (value == null)
            return "";

        if (!value.contains("&#") || charsetName == null)
            return value;

        StringBuilder newString = new StringBuilder();
        value = value.replaceAll("&#", "");
        String[] characters = value.split(";");
        for (String character : characters) {
            if (character.startsWith(" "))
                newString.append(" ");

            if (character.trim().length() != 4)
                newString.append(character.trim());
            else
                newString.append((char) Integer.valueOf(character.trim()).intValue()).append("");
        }

        return getStringForCharset(newString.toString(), charsetName);
    }

    public static String getCurrentTimeStampInFormat(String format) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(now);
    }

}
