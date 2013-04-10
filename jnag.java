/*
 * jnag/jnag.java - The annoying nagger, yours truly
 *
 * © Copyright Piotr S. Staszewski 2013
 * Visit http://www.drbig.one.pl for contact information.
 *
 */

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class jnag {
  static Console cons = System.console();

  private static long parseTimeIn(String timestr) {
    long delay = 0;

    StringBuilder sb = new StringBuilder(2);
    for (int i = 0; i < timestr.length(); i++) {
      char c = timestr.charAt(i);
      if (Character.isDigit(c))
        sb.append(c);
      else {
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
    for (int i = 0; i < elems.length; i++)
      vals[i] = Integer.parseInt(elems[i]);

    Calendar cal = Calendar.getInstance();
    delay += (vals[0] - cal.get(Calendar.HOUR_OF_DAY)) % 24 * 3600;
    delay += (vals[1] - cal.get(Calendar.MINUTE)) % 60 * 60;
    delay += (vals[2] - cal.get(Calendar.SECOND)) % 60;

    if (delay < 0) delay = 24*3600 + delay;

    return delay * 1000;
  }

  private static void die(String msg, int retcode) {
    cons.printf(msg + "\n");
    System.exit(retcode);
  }

  private static void printHelp() {
    die("Args: at|in|every timestr msgstr\n"
      + " e.g. in 45m laundry's done\n"
      + "      every 2h15m do yourself a break\n"
      + "      every 5s nag nag nag nag\n"
      + "      at 17 it's five o'clock!\n"
      + "      at 6:12 sunrise, go to sleep!\n"
      + "      at 3:13:37 leet time", 1);
  }

  public static void main(String[] args) throws InterruptedException {
    JFrame frame = new JFrame("JNag");
    Object[] labels = {"Keep", "Close", "Again"};
    StringBuilder msg = new StringBuilder(128);

    long delay = 0;
    boolean loop = false;
    int resp;

    if (args.length < 3) printHelp();

    switch (args[0]) {
      case "every":
        loop = true;
      case "in":
        delay = parseTimeIn(args[1]);
        break;
      case "at":
        delay = parseTimeAt(args[1]);
        break;
      default: printHelp();
    }

    for (int i = 2; i < args.length; i++) {
      msg.append(args[i]);
      if (i != (args.length - 1)) msg.append(" ");
    }

    do {
      Thread.sleep(delay);
      do {
        resp = JOptionPane.showOptionDialog(frame, msg.toString(), "JNag",
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
            null, labels, labels[2]);
      } while (resp == 2);
      if (resp != 0) loop = false;
    } while (loop);

    System.exit(0);
  }
}
