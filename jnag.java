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
import javax.swing.JFrame;
import javax.swing.JOptionPane;

class JNagger {
  JFrame frame = new JFrame("JNagger");
  Object[] labels = {"Keep", "Close", "Again"};

  Console cons = System.console();
  StringBuilder msg = new StringBuilder(128);
  int counter = 0;

  private void die(String msg, int retcode) {
    cons.printf(msg + "\n");
    System.exit(retcode);
  }

  private void printHelp() {
    die("Args: (exactly) at|in|every timestr msgstr\n"
        + " e.g. in 45m check laundry\n"
        + "      every 2h15m do yourself a break\n"
        + "      exactly every 5s nag nag nag nag\n"
        + "      at 17 tea time\n"
        + "      at 6:12 sunrise, go to sleep\n"
        + "      at 3:13:37 leet time", 1);
  }

  private long parseTimeIn(String timestr) {
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

  private void dialogLoop() {
    int resp;

    do {
      resp = JOptionPane.showOptionDialog(frame, msg.toString(), 
          String.format("Jay Nagger #%d", counter),
          JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
          null, labels, labels[2]);
    } while (resp == 2);
    if (resp != 0) System.exit(0);
  }

  private long parseTimeAt(String timestr) {
    long delay = 0;

    String[] elems = timestr.split(":");
    int[] vals = {0,0,0};
    for (int i = 0; i < elems.length; i++)
      vals[i] = Integer.parseInt(elems[i]);

    Calendar cal = Calendar.getInstance();
    delay += (vals[0] - cal.get(Calendar.HOUR_OF_DAY)) * 3600;
    delay += (vals[1] - cal.get(Calendar.MINUTE)) * 60;
    delay += (vals[2] - cal.get(Calendar.SECOND));

    if (delay < 0) delay = 24*3600 + delay;

    return delay * 1000;
  }

  class PopupTask extends TimerTask {
    class PopupThread extends Thread {
      public void run() {
        dialogLoop();
      }
    }

    public void run() {
      counter++;
      PopupThread p = new PopupThread();
      p.start();
    }
  }

  public void run(String[] args) throws InterruptedException {
    Timer timer;
    long delay = 0;
    boolean loop = false;
    int offset = 0;

    if (args.length < 3) printHelp();
    if ("exactly".equals(args[0])) offset = 1;

    switch (args[offset]) {
      case "every":
        loop = true;
      case "in":
        delay = parseTimeIn(args[offset+1]);
        break;
      case "at":
        delay = parseTimeAt(args[offset+1]);
        break;
      default: printHelp();
    }

    for (int i = offset + 2; i < args.length; i++) {
      msg.append(args[i]);
      if (i != (args.length - 1)) msg.append(" ");
    }

    if (loop && (offset == 1)) {
      timer = new Timer();
      timer.schedule(new PopupTask(), delay, delay);
    } else {
      do {
        counter++;
        Thread.sleep(delay);
        dialogLoop();
      } while (loop);

      System.exit(0);
    }
  }
}

public class jnag {
  public static void main(String[] args) throws InterruptedException {
    JNagger jn = new JNagger();
    jn.run(args);
  }
}
