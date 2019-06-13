package ftc.vision.Glyph;

import org.opencv.core.Scalar;

import ftc.vision.ImageUtil;

public class GlyphResult {

    private final GlyphColor glyphColor;

    private final int xPos, yPos;

    public GlyphResult(GlyphColor glyphColor, int xPos, int yPos) {
        this.glyphColor = glyphColor;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getxPos() {return xPos;}
    public int getyPos() {return yPos;}

    public GlyphColor getGlyphColor() {return glyphColor;}

    public enum GlyphColor {
        BROWN (ImageUtil.BROWN),
        GRAY (ImageUtil.GRAY);

        public final Scalar color;

        GlyphColor(Scalar color) {this.color = color;}
    }
}
