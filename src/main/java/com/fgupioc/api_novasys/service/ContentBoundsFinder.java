package com.fgupioc.api_novasys.service;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 29/07/2026
 */
public class ContentBoundsFinder extends PDFGraphicsStreamEngine {

    private final Rectangle2D.Double box = new Rectangle2D.Double();
    private boolean any = false;
    private Point2D current = new Point2D.Double(0, 0);

    public ContentBoundsFinder(PDPage page) { super(page); }

    public Rectangle2D find() throws IOException {
        processPage(getPage());
        return any ? box : null;
    }

    private void add(double x, double y) {
        if (!any) { box.setRect(x, y, 0, 0); any = true; }
        else box.add(x, y);
    }

    @Override public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
        add(p0.getX(), p0.getY()); add(p1.getX(), p1.getY());
        add(p2.getX(), p2.getY()); add(p3.getX(), p3.getY());
        current = p3;
    }

    @Override public void drawImage(PDImage img) {
        Matrix m = getGraphicsState().getCurrentTransformationMatrix();
        add(m.getTranslateX(), m.getTranslateY());
        add(m.getTranslateX() + m.getScalingFactorX(),
                m.getTranslateY() + m.getScalingFactorY());
    }

    @Override public void moveTo(float x, float y) { current = new Point2D.Double(x, y); add(x, y); }
    @Override public void lineTo(float x, float y) { current = new Point2D.Double(x, y); add(x, y); }
    @Override public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        add(x1, y1); add(x2, y2); add(x3, y3);
        current = new Point2D.Double(x3, y3);
    }

    @Override public Point2D getCurrentPoint() { return current; }
    @Override public void clip(int windingRule) { }
    @Override public void closePath() { }
    @Override public void endPath() { }
    @Override public void strokePath() { }
    @Override public void fillPath(int windingRule) { }
    @Override public void fillAndStrokePath(int windingRule) { }
    @Override public void shadingFill(COSName name) { }

    @Override
    protected void showGlyph(Matrix trm, PDFont font, int code, Vector disp) throws IOException {
        super.showGlyph(trm, font, code, disp);
        float size = trm.getScalingFactorY();
        double x = trm.getTranslateX();
        double y = trm.getTranslateY();
        add(x, y - size * 0.25);                        // descendente
        add(x + disp.getX() * size, y + size * 0.85);   // ascendente
    }
}