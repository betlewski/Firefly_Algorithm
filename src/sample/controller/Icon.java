package sample.controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Icon {

    private static final String DIR_PATH = "../icons/";
    public static final ImageView PLAY_ICON = createIcon("play.png", 15);
    public static final ImageView PAUSE_ICON = createIcon("pause.png", 14);
    public static final ImageView STOP_ICON = createIcon("stop.png", 13);

    public static ImageView createIcon(String filename, double size) {
        String filepath = DIR_PATH + filename;
        Image image = new Image(Icon.class.getResourceAsStream(filepath),
                size, size, true, false);
        return new ImageView(image);
    }

}
