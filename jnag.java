import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class wxnag {
  static Console cons = System.console();

  private static long parseTimeIn(String timestr) {
    long delay = 0;

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < timestr.length(); i++) {
      char c = timestr.charAt(i);
      if (Character.isDigit(c)) {
        sb.append(c);
      } else {
        int val = Integer.parseInt(sb.toString());
        sb = new StringBuilder();
        switch (c) {
          case 'h': delay += val * 3600;  break;
          case 'm': delay += val * 60;    break;
          case 's': delay += val;         break;
          default: break;
        }
      }
    }

    return delay * 1000;
  }

  private static long parseTimeAt(String timestr) {
    long delay = 0;

    String[] elems = timestr.split(":");
    int[] vals = {0,0,0};
    for (int i = 0; i < elems.length; i++) {
      vals[i] = Integer.parseInt(elems[i]);
    }

    Calendar cal = Calendar.getInstance();
    delay += (vals[0] - cal.get(Calendar.HOUR_OF_DAY)) % 24 * 3600;
    delay += (vals[1] - cal.get(Calendar.MINUTE)) % 60 * 60;
    delay += (vals[2] - cal.get(Calendar.SECOND)) % 60;

    return delay * 1000;
  }

  private static void die(String msg, int retcode) {
    cons.printf(msg + "\n");
    System.exit(retcode);
  }

  private static void printHelp() {
    die("Args: at|in|every timestr msgstr", 1);
  }

  public static void main(String[] args) throws InterruptedException {
    JFrame frame = new JFrame("WxNag");
    Object[] labels = {"Keep", "Close", "Again"};

    long delay = 0;
    boolean loop = false;
    String msg;

    if (args.length < 3) {
      printHelp();
    }

    switch (args[0]) {
      case "every":
        loop = true;
      case "in":
        delay = parseTimeIn(args[1]);
        break;
      case "at":
        delay = parseTimeAt(args[1]);
        break;
      default:
        printHelp();
        break;
    }

    msg = args[2];
    do {
      Thread.sleep(delay);
      int resp;
      do {
        resp = JOptionPane.showOptionDialog(frame, msg, "WxNag",
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
            null, labels, labels[2]);
      } while (resp == 2);
      if (resp != 0) loop = false;
    } while (loop);

    System.exit(0);
  }
}
