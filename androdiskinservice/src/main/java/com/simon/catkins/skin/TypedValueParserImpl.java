package com.simon.catkins.skin;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * TypedValue is used internally to store dynamic typed data inflate from XML.
 *
 * @see TypedValue
 */
public class TypedValueParserImpl implements TypedValueParser {
    private static final String TAG = "TypedValueParserImpl";
    private static final boolean DEBUG = Loot.DEBUG;

    /**
     * Color literal
     *
     * @param color the literal color string
     * @return null if parse error occurred
     */
    @Override
    public TypedValue parseColor(String color) {
        if (color.startsWith("#")) {
            String substring = color.substring(1, color.length());
            TypedValue value = new TypedValue();
            try {
                value.data = (int) Long.parseLong(substring, 16);
                if (DEBUG) Loot.logParse("parseColor : " + Integer.toHexString(value.data));
            } catch (NumberFormatException e) {
                if (DEBUG) Loot.logParse("Parse color failed: " + color);
                return null;
            }
            final int length = color.length() - 1;
            if (length == 6) {
                value.type = TypedValue.TYPE_INT_COLOR_RGB8;
                return value;
            } else if (length == 3) {
                value.type = TypedValue.TYPE_INT_COLOR_RGB4;
            } else if (length == 8) {
                value.type = TypedValue.TYPE_INT_COLOR_ARGB8;
            } else if (length == 4) {
                value.type = TypedValue.TYPE_INT_COLOR_ARGB4;
            } else {
                if (DEBUG) Loot.logParse("Parse color failed: [" + color + "] : wrong TypedValue Type");
                return null;
            }
            return value;
        }
        if (DEBUG) Loot.logParse("Parse color failed: [" + color + "] : not a color");
        return null;
    }

    /**
     * Float literal
     *
     * Store float as int bits into TypedValue.data
     *
     * @param f the float in String
     * @return null if parse error occurred
     */
    @Override
    public TypedValue parseFloat(String f) {
        try {
            final int data = Float.floatToIntBits(Float.parseFloat(f));
            TypedValue value = new TypedValue();
            value.type = TypedValue.TYPE_FLOAT;
            value.data = data;
            return value;
        } catch (NumberFormatException e) {
            if (DEBUG) Loot.logParse("Parse float failed: [" + f + "]");
            return null;
        }
    }

    /**
     * dimension literal
     *
     * @param dimension the dimension
     * @return null if parse error occurred
     */
    @Override
    public TypedValue parseDimension(String dimension, DisplayMetrics dm) {

        String unitStr = dimension.substring(dimension.length() - 2, dimension.length() - 1);
        String dimenStr = dimension.substring(0, dimension.length() - 3);

        int unit;
        if (unitStr.equals("dp")) {
            unit = TypedValue.COMPLEX_UNIT_DIP;
        } else if (unitStr.equals("sp")) {
            unit = TypedValue.COMPLEX_UNIT_SP;
        } else if (unitStr.equals("in")) {
            unit = TypedValue.COMPLEX_UNIT_IN;
        } else if (unitStr.equals("mm")) {
            unit = TypedValue.COMPLEX_UNIT_MM;
        } else if (unitStr.equals("pt")) {
            unit = TypedValue.COMPLEX_UNIT_PT;
        } else if (unitStr.equals("px")) {
            unit = TypedValue.COMPLEX_UNIT_PX;
        } else {
            if (DEBUG) {
                Loot.logParse("Parse dimension failed: [" + dimension + "] : wrong unit type");
            }
            return null;
        }

        final int data;
        try {
            final float value = Float.valueOf(dimenStr);
            data = Float.floatToIntBits(TypedValue.applyDimension(unit, value, dm));
        } catch (NumberFormatException e) {
            if (DEBUG) {
                Loot.logParse("Parse dimension failed: [" + dimension + "]");
            }
            return null;
        }

        TypedValue tv = new TypedValue();
        tv.type = TypedValue.TYPE_DIMENSION;
        tv.density = dm.densityDpi;
        tv.data = data;

        return tv;
    }

    /**
     * String literal
     * @param string a string
     * @return the TypedValue
     */
    @Override
    public TypedValue parseString(String string) {
        TypedValue tv = new TypedValue();
        tv.type = TypedValue.TYPE_STRING;
        tv.string = string;
        return tv;
    }

    /**
     * Boolean literal
     * @param value a boolean
     * @return the TypedValue
     */
    @Override
    public TypedValue parseBoolean(String value) {
        final int b;
        if ("true".equals(value)) {
            b = 1;
        } else if ("false".equals(value)) {
            b = 0;
        } else {
            if (DEBUG) Loot.logParse("Parse boolean failed: not true/false");
            return null;
        }
        TypedValue tv = new TypedValue();
        tv.type = TypedValue.TYPE_INT_BOOLEAN;
        tv.data = b;
        return tv;
    }

    /**
     * Integer literal
     *
     * @param integer a integer
     * @return the TypedValue
     */
    @Override
    public TypedValue parseInt(String integer) {
        if (integer.startsWith("0x")) {
            final int i;
            try {
                i = Integer.parseInt(integer, 16);
            } catch (NumberFormatException e) {
                if (DEBUG) Loot.logParse("Parse integer failed: [" + integer + "]");
                return null;
            }
            TypedValue tv = new TypedValue();
            tv.type = TypedValue.TYPE_INT_HEX;
            tv.data = i;
            return tv;
        } else {
            final int i;
            try {
                i = Integer.parseInt(integer);
            } catch (NumberFormatException e) {
                if (DEBUG) Loot.logParse("Parse integer failed: [" + integer + "]");
                return null;
            }
            TypedValue tv = new TypedValue();
            tv.type = TypedValue.TYPE_INT_DEC;
            tv.data = i;
            return tv;
        }
    }

    /**
     * Parse the reference id
     */
    @Override
    public TypedValue parseReference(String ref, Resources res, String pkg) {
        if (ref.startsWith("@")) {
            ref = ref.substring(1);
            String[] typeValue;
            if (ref.contains("/")) {
                typeValue = ref.split("/");
            } else {
                if (DEBUG) Loot.logParse("Parse reference failed: [" + ref + "] : no type information");
                return null;
            }
            final int id = res.getIdentifier(typeValue[1], typeValue[0], pkg);
            if (id == 0) {
                if (DEBUG) Loot.logParse("Parse reference failed: [" + ref + "] : resource not found");
                return null;
            }
            TypedValue tv = new TypedValue();
            tv.type = TypedValue.TYPE_REFERENCE;
            tv.resourceId = id;
            tv.data = id;
            tv.string = ref;
            if (DEBUG) Loot.logParse("Parse reference id: 0x" + Integer.toHexString(id));
            return tv;
        } else {
            if (DEBUG) Loot.logParse("Parse reference failed: [" + ref + "] : not start with @");
            return null;
        }
    }

}
