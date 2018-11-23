package com.example.juicekaaa.fireserver.util;

public class EncodingConversionTools {

    /**
     * 字符串转换成十六进制字符串（转GBK编码）
     * @param String str 待转换的ASCII字符串
     * @return String 如: [616C6B]
     */
    public static String str2HexStr(String str)
    {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = null;
        try {
            bs = str.getBytes("GBK");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * 生成16进制累加和校验码
     * @param data
     * 除去校验位的数据
     * @return
     */
    public static String makeChecksum(String data) {
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用65535求余最大是65534，即16进制的FFFF
         */
        int mod = total % 65535;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        // 如果不够校验位的长度，补0
        switch (len) {
            case 1:
                hex = "000" + hex;
                break;
            case 2:
                hex = "00" + hex;
                break;
            case 3:
                hex = "0" + hex;
                break;
            default:
                break;
        }
        return hex;
    }

    /**
     * 十六进制字符串转换成bytes
     * @param byte[] b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static byte[] HexString2Bytes(String src) {
        int lenth = src.length() / 2;
        byte[] ret = new byte[lenth];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < lenth; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static String getHexStr(String str) {
        String rtn = "";
        String hexStr = "0123456789ABCDEF";

        str = str.toUpperCase();
        for (int i = 0; i < str.length(); i++) {
            if (hexStr.indexOf(str.charAt(i)) != -1) {
                rtn = rtn + str.charAt(i);
            }
        }
        return rtn;
    }

    /**
     * bytes转换成十六进制字符串
     * @param byte[] b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }
}
