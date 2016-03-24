package com.framework.core.app.comm.utils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
/**
 * @author chirs@zhoujin.com (Chirs Chou)
 */
public class EncodeUtils{
    private static final String DEFAULT_URL_ENCODING="UTF-8";

    public static String hexEncode(byte[] input){
        return Hex.encodeHexString(input);
    }

    public static byte[] hexDecode(String input){
        try{
            return Hex.decodeHex(input.toCharArray());
        }catch(DecoderException e){
            throw new IllegalStateException("Hex Decoder exception",e);
        }
    }

    public static String base64Encode(byte[] input){
        return new String(Base64.encodeBase64(input));
    }

    public static String base64UrlSafeEncode(byte[] input){
        return Base64.encodeBase64URLSafeString(input);
    }

    public static byte[] base64Decode(String input){
        return Base64.decodeBase64(input);
    }

    public static String urlEncode(String input){
        return urlEncode(input,"UTF-8");
    }

    public static String urlEncode(String input,String encoding){
        try{
            return URLEncoder.encode(input,encoding);
        }catch(UnsupportedEncodingException e){
            throw new IllegalArgumentException("Unsupported Encoding Exception",e);
        }
    }

    public static String urlDecode(String input){
        return urlDecode(input,"UTF-8");
    }

    public static String urlDecode(String input,String encoding){
        try{
            return URLDecoder.decode(input,encoding);
        }catch(UnsupportedEncodingException e){
            throw new IllegalArgumentException("Unsupported Encoding Exception",e);
        }
    }
}
