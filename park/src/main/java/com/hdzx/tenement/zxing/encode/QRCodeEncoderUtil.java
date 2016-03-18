package com.hdzx.tenement.zxing.encode;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by anchendong on 15/7/28.
 */
public class QRCodeEncoderUtil {


    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private static BarcodeFormat format = BarcodeFormat.QR_CODE;

    /**
     * 字符串->二维码
     * @param str
     * @param QR_WIDTH
     * @param QR_HEIGHT
     * @return
     */
    public static final Bitmap create2DCoderBitmap(String str, int QR_WIDTH, int QR_HEIGHT) {
        int smallerDimension = QR_WIDTH < QR_HEIGHT ? QR_WIDTH : QR_HEIGHT;
        smallerDimension = smallerDimension * 7/ 8;

        Bitmap bitmap = null;
        try {


            String contentsToEncode = str;
            if (contentsToEncode == null) {
                return null;
            }
            Map<EncodeHintType, Object> hints = null;
            String encoding = guessAppropriateEncoding(contentsToEncode);
            if (encoding != null) {
                hints = new EnumMap<>(EncodeHintType.class);
                hints.put(EncodeHintType.CHARACTER_SET, encoding);
            }
            BitMatrix result;

            result = new MultiFormatWriter().encode(contentsToEncode, format, smallerDimension, smallerDimension, hints);

            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
}
