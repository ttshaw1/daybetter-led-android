package com.karlquinsland.android.btleled;


import android.util.Log;

import java.io.ByteArrayOutputStream;

class Driver {

    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    Driver(){
        Log.d(TAG, "Driver class created");
    }


    private byte[] intArrayToByteArray(int[] ints){

        Log.i(TAG, "intArrayToByteArray>: ints: " + java.util.Arrays.toString(ints));

        ByteArrayOutputStream byo = new ByteArrayOutputStream();

        for (int integer : ints) {
            byo.write((byte) (integer));
        }

        return byo.toByteArray();

    }

    public int[] setOutputOnOff(boolean should_be_on){
        Log.i(TAG, "setOutputOnOff> should_be_on:" + should_be_on);

        int[] bytes = new int[6];


        if(should_be_on){

            bytes[0] = 160;
            bytes[1] = 17;
            bytes[2] = 4;
            bytes[3] = 1;
            bytes[4] = 177;
            bytes[5] = 33;

        } else {

            bytes[0] = 160;
            bytes[1] = 17;
            bytes[2] = 4;
            bytes[3] = 00;
            bytes[4] = 112;
            bytes[5] = 225;


        }

        return bytes;

    };

    public int[] setMacroByID(int macro_id){
        Log.i(TAG, "setMacroByID> macro_id:" + macro_id);

        int[] bytes = new int[9];

        //TODO: see below list of "valid" macros and validate macro_id

        /*
            appears to be this packet

                code[0] = 126;
                code[1] = 5;
                code[2] = 3;
                code[3] = model;
                code[4] = 3;
                code[5] = 255;
                code[6] = 255;
                code[8] = 239;

            where model is:
                <item>Static red,128</item>
                <item>Static blue,129</item>
                <item>Static green,130</item>
                <item>Static cyan,131</item>
                <item>Static yellow,132</item>
                <item>Static purple,133</item>
                <item>Static white,134</item>
                <item>Tricolor jump,135</item>
                <item>Seven-color jump,136</item>
                <item>Tricolor gradient,137</item>
                <item>Seven-color gradient,138</item>
                <item>Red gradient,139</item>
                <item>Green gradient,140</item>
                <item>Blue gradient,141</item>
                <item>Yellow gradient,142</item>
                <item>Cyan gradient,143</item>
                <item>Purple gradient,144</item>
                <item>White gradient,145</item>
                <item>Red-Green gradient,146</item>
                <item>Red-Blue gradient,147</item>
                <item>Green-Blue gradient,148</item>
                <item>Seven-color flash,149</item>
                <item>Red flash,150</item>
                <item>Green flash,151</item>
                <item>Blue flash,152</item>
                <item>Yellow flash,153</item>
                <item>Cyan flash,154</item>
                <item>Purple flash,155</item>
                <item>White flash,156</item>

            //TODO: fuzz this / see what "other" modes are "supported"
            // TODO: check if there's anythign special to "go back" to RGB mode.
             it might be as aimple as sending an RGB color packet to swtcih to the basic mode

         */



        bytes[0] = 126;
        bytes[1] = 5;
        bytes[2] = 3;
        bytes[3] = macro_id;
        bytes[4] = 3;
        bytes[5] = 255;
        bytes[6] = 255;
        //bytes[7] = NOT_SET;
        bytes[8] = 239;

        return bytes;

    };


    public int[] setBrightness(int brightness) {

        Log.i(TAG, "setBrightness> brightness:" + brightness);


        int[] bytes = new int[6];

        bytes[0] = 160;
        bytes[1] = 19;
        bytes[2] = 4;

        bytes[3] = 1;
        bytes[4] = 16;
        bytes[5] = 225;

        int[] first_brightness_bytes = {1, 12, 20, 22, 27, 30, 34, 41, 42, 50, 61, 73, 78, 88, 90, 100};
        int[] second_brightness_bytes = {16, 209, 209, 80, 145, 81, 81, 16, 80, 80, 16, 16, 81, 208, 81, 208};
        int[] third_brightness_bytes = {225, 36, 46, 239, 42, 41, 56, 255, 254, 244, 240, 215, 21, 219, 26, 202};


        int[] thresholds = {1, 11, 19, 21, 26, 29, 34, 40, 42, 50, 61, 73, 78, 88, 90, 100};

        for (int i = 0; i < thresholds.length; i++) {
            if (brightness <= thresholds[i]) {
                bytes[3] = first_brightness_bytes[i];
                bytes[4] = second_brightness_bytes[i];
                bytes[5] = third_brightness_bytes[i];

                return bytes; //seems like brightness doesn't work unless I return here
            }
        }

        bytes[3] = 1;
        bytes[4] = 16;
        bytes[5] = 225;

        return bytes;
    }

    public int[] setRGB(int red, int green, int blue) {
        Log.i(TAG, "setRGB> red: " + red + " green:" + green + " blue:" + blue);

        int[] bytes = new int[8];

        //default to white
        bytes[0] = 160;
        bytes[1] = 21;
        bytes[2] = 6;
        bytes[3] = 247;
        bytes[4] = 237;
        bytes[5] = 255;
        bytes[6] = 169;
        bytes[7] = 18;

        if (red > 50 && green < 50 && blue < 50) {
            bytes[3] = 255;
            bytes[4] = 0;
            bytes[5] = 0;
            bytes[6] = 37;
            bytes[7] = 192; //red
        }
        if (red < 50 && green > 50 && blue < 50) {
            bytes[3] = 0;
            bytes[4] = 255;
            bytes[5] = 0;
            bytes[6] = 84;
            bytes[7] = 0; //green
        }

        if (red < 50 && green < 50 && blue > 50) {
            bytes[3] = 0;
            bytes[4] = 0;
            bytes[5] = 255;
            bytes[6] = 85;
            bytes[7] = 176; //blue
        }

        if (red < 50 && green > 50 && blue > 50) {
            bytes[3] = 0;
            bytes[4] = 255;
            bytes[5] = 255;
            bytes[6] = 20;
            bytes[7] = 64; //cyan
        }

        if (red > 50 && green > 50 && blue < 50)
        {
            bytes[3] = 255;
            bytes[4] = 255;
            bytes[5] = 0;
            bytes[6] = 100;
            bytes[7] = 48; //yellow
        }

        if (red > 50 && green < 50 && blue > 128)
        {
            bytes[3] = 128;
            bytes[4] = 0;
            bytes[5] = 128;
            bytes[6] = 21;
            bytes[7] = 184; //purple
        }

        if (red == 143 && green == 112 && blue == 119)
        {
            int[] bytes_short = new int[7];

            bytes_short[0] = 160;
            bytes_short[1] = 18;
            bytes_short[2] = 5;
            bytes_short[3] = 192;
            bytes_short[4] = 0;
            bytes_short[5] = 225;
            bytes_short[6] = 96;

            return bytes_short;
        }

        if (red == 129 && green == 126 && blue == 126)
        {
            int[] bytes_short = new int[7];

            bytes_short[0] = 160;
            bytes_short[1] = 18;
            bytes_short[2] = 5;
            bytes_short[3] = 193;
            bytes_short[4] = 0;
            bytes_short[5] = 224;
            bytes_short[6] = 240;

            return bytes_short;
        }

        return bytes;
    }


    // TODO: should use ENUM?
    public int[] setRGBOrder(int rgb_order){

        Log.i(TAG, "setRGB> rgb_order " + rgb_order);

        /*
            <array name="rgb_model">
                <item>RGB,1</item>
                <item>RBG,2</item>
                <item>GRB,3</item>
                <item>GBR,4</item>
                <item>BRG,5</item>
                <item>BGR,6</item>
            </array>

         */
        int[] bytes = new int[9];

        bytes[0] = 126;
        bytes[1] = 4;
        bytes[2] = 8;
        bytes[3] = rgb_order;
        bytes[4] = 255;
        bytes[5] = 255;
        bytes[6] = 255;
        bytes[8] = 239;



        return bytes;
    };

}
