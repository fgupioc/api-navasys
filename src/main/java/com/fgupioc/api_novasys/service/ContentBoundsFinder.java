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
import java.util.ArrayList;
import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 29/07/2026
 */

public class ContentBoundsFinder extends PDFGraphicsStreamEngine {

    private final Rectangle2D.Double box = new Rectangle2D.Double();
    private final List<Point2D> pendiente = new ArrayList<>();
    private boolean any = false;
    private Point2D current = new Point2D.Double(0, 0);

    public ContentBoundsFinder(PDPage page) { super(page); }

    public Rectangle2D find() throws IOException {
        processPage(getPage());
        return any ? box : null;
    }

    /** Confirma un punto en el bounding box. */
    private void add(double x, double y) {
        if (!any) { box.setRect(x, y, 0, 0); any = true; }
        else box.add(x, y);
    }

    /** Confirma el path pendiente (solo si se pinta de verdad). */
    private void commit() {
        for (Point2D p : pendiente) add(p.getX(), p.getY());
        pendiente.clear();
    }

    // ---- construcción del path: NO confirma todavía ----

    @Override public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
        pendiente.add(p0); pendiente.add(p1);
        pendiente.add(p2); pendiente.add(p3);
        current = p3;
    }

    @Override public void moveTo(float x, float y) {
        current = new Point2D.Double(x, y);
        pendiente.add(current);
    }

    @Override public void lineTo(float x, float y) {
        current = new Point2D.Double(x, y);
        pendiente.add(current);
    }

    @Override public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        pendiente.add(new Point2D.Double(x1, y1));
        pendiente.add(new Point2D.Double(x2, y2));
        current = new Point2D.Double(x3, y3);
        pendiente.add(current);
    }

    // ---- el path se pinta: SÍ confirma ----

    @Override public void strokePath()                  { commit(); }
    @Override public void fillPath(int windingRule)     { commit(); }
    @Override public void fillAndStrokePath(int rule)   { commit(); }

    // ---- el path era clip o se descarta: NO confirma ----

    @Override public void clip(int windingRule) { pendiente.clear(); }
    @Override public void endPath()             { pendiente.clear(); }
    @Override public void closePath()           { }
    @Override public void shadingFill(COSName name) { }

    // ---- texto e imágenes: siempre confirman ----

    @Override
    protected void showGlyph(Matrix trm, PDFont font, int code, Vector disp) throws IOException {
        super.showGlyph(trm, font, code, disp);
        float size = trm.getScalingFactorY();
        double x = trm.getTranslateX();
        double y = trm.getTranslateY();
        add(x, y - size * 0.25);
        add(x + disp.getX() * size, y + size * 0.85);
    }

    @Override public void drawImage(PDImage img) {
        Matrix m = getGraphicsState().getCurrentTransformationMatrix();
        add(m.getTranslateX(), m.getTranslateY());
        add(m.getTranslateX() + m.getScalingFactorX(),
                m.getTranslateY() + m.getScalingFactorY());
    }

    @Override public Point2D getCurrentPoint() { return current; }
}