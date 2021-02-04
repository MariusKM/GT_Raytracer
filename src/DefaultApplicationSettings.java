import java.awt.*;

public class DefaultApplicationSettings extends ApplicationSettings {

    public DefaultApplicationSettings() {
        this.resX = 1024;
        this.resY = 1024;
        this.usePerspective = true;
        this.animationLength = 300;
        this.BG_Color = new Color(0.125f, 0.115f, 0.125f);
    }
}
