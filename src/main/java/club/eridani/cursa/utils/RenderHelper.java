package club.eridani.cursa.utils;

import club.eridani.cursa.utils.math.Vec2I;
import net.minecraft.client.gui.ScaledResolution;

public class RenderHelper {

    public static Vec2I getStart(ScaledResolution scaledResolution, String caseIn) {
        switch (caseIn) {
            case "RightDown": {
                return new Vec2I(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
            }
            case "LeftTop": {
                return new Vec2I(0, 0);
            }
            case "LeftDown": {
                return new Vec2I(0, scaledResolution.getScaledHeight());
            }
            default: {
                return new Vec2I(scaledResolution.getScaledWidth(), 0);
            }
        }
    }

    public static Vec2I getStart(ScaledResolution scaledResolution, StartPos caseIn) {
        switch (caseIn) {
            case RightDown: {
                return new Vec2I(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
            }
            case LeftTop: {
                return new Vec2I(0, 0);
            }
            case LeftDown: {
                return new Vec2I(0, scaledResolution.getScaledHeight());
            }
            default: {
                return new Vec2I(scaledResolution.getScaledWidth(), 0);
            }
        }
    }

    enum StartPos {
        RightDown,
        RightTop,
        LeftDown,
        LeftTop
    }
}
