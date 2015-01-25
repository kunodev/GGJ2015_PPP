package de.panda.tiled;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class TextureHelper {
    private TextureHelper() {
    }

    public static TextureRegion[] cast(List<TextureRegion> walkList) {
        TextureRegion[] result = new TextureRegion[walkList.size()];
        walkList.toArray(result);
        return result;
    }

    public static List<TextureRegion> extractListOfRegions(Texture walk, int xParts) {
        List<TextureRegion> walkList = new ArrayList<TextureRegion>();
        int heightOfOne = walk.getHeight();
        int widthOfOne = walk.getWidth() / xParts;
        for (int i = 0; i < xParts; i++) {
            TextureRegion nextOne = new TextureRegion(walk, widthOfOne * i, 0, widthOfOne, heightOfOne);
            walkList.add(nextOne);
        }
        return walkList;
    }

    public static TextureRegion[] doTextureMagic(Texture t, int xParts) {
        return cast(extractListOfRegions(t, xParts));
    }
}
