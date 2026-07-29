package com.fgupioc.api_novasys.helpers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 29/07/2026
 */
public final class BarcodeUtil {

    public static String code128Base64(String contenido, int w, int h) {
        try {
            BitMatrix matrix = new Code128Writer().encode(
                    contenido, BarcodeFormat.CODE_128, w, h,
                    Map.of(EncodeHintType.MARGIN, 0));
            var out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar el código de barras", e);
        }
    }
}