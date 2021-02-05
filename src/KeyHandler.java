import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener {

    @Override

    public void keyPressed(KeyEvent event) {

        printEventInfo("Key Pressed", event);

        if (event.getKeyCode() == KeyEvent.VK_ENTER){
            RayTracerApplication.setExit(true);
            GUI.getFrame().dispose();
        }

    }

    @Override

    public void keyReleased(KeyEvent event) {

        printEventInfo("Key Released", event);

    }

    @Override

    public void keyTyped(KeyEvent event) {

        printEventInfo("Key Typed", event);

    }


    private void printEventInfo(String str, KeyEvent e) {

        System.out.println(str);

        int code = e.getKeyCode();

        System.out.println("   Code: " + KeyEvent.getKeyText(code));

        System.out.println("   Char: " + e.getKeyChar());

        int mods = e.getModifiersEx();

        System.out.println("    Mods: "

                + KeyEvent.getModifiersExText(mods));

        System.out.println("    Location: "

                + keyboardLocation(e.getKeyLocation()));

        System.out.println("    Action? " + e.isActionKey());

    }


    private String keyboardLocation(int keybrd) {

        switch (keybrd) {

            case KeyEvent.KEY_LOCATION_RIGHT:

                return "Right";

            case KeyEvent.VK_ENTER:

                return "Enter";

            case KeyEvent.KEY_LOCATION_LEFT:

                return "Left";

            case KeyEvent.KEY_LOCATION_NUMPAD:

                return "NumPad";

            case KeyEvent.KEY_LOCATION_STANDARD:

                return "Standard";

            case KeyEvent.KEY_LOCATION_UNKNOWN:

            default:

                return "Unknown";

        }

    }

}