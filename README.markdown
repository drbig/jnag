# jnag

A minimal nagger designed to be easy to run from the terminal. If you find yourself forgetting about all those small, random events throughout a day this should help you.

This version is written in Java and should work everywhere. As making a Java program detach from terminal and run in background is rather non-trivial there is a simple wrapper sh script. I guess on Windows you can just run it with javaw.exe, but I haven't tested that.

```
drbig@swordfish:pts/5 ~/P/jnag> java jnag in 10s hello there!
```

After 10 long seconds...

![Jay Nagger in action](https://raw.github.com/drbig/jnag/master/jnag.png)

## Usage

Arguments read like an ordinary English sentence and do what you mean.

```
drbig@swordfish:pts/5 ~/P/jnag> java jnag
Args: (exactly) at|in|every timestr msgstr
 e.g. in 45m check laundry
      every 2h15m do yourself a break
      exactly every 5s nag nag nag nag
      at 17 tea time
      at 6:12 sunrise, go to sleep
      at 3:13:37 leet time
```

Notes:

- For in/every the order of h, m, s is irrelevant.
- For at the order is fixed: h, m, s.
- The exactly modifier is relevant only for a repetitive timer.
- All remaining arguments are concatenated into the msgstr.

Two behaviours for repetitive timers:

1. The default behaviour for every is that the next delay will be counted after you click "Keep" in the currently displayed nagger. Example usage: 'every 2h do yourself a break', and when you see the popup you take a break, then when you come back you click "Keep" and will have another 2h session of work. Obviously there will never be more than one popup visible at once.

2. With the exactly modifier the next delay will be counted immediately after displaying the current nagger. This means that the popups may pile-up in case you were away from the monitor. Important: you'll need to click "Keep" in _every_ instance of the popup to have it running (clicking "Close" on any of them closes the whole program). This is intentional.

## Popups outside of current application context

Popups are great to get your attention, however if they don't originate from the currently focused application there is one 'problem' with them: they will steal keyboard focus and happily interpret your keystrokes. With default two-button popups this would mean that very often you'll close randomly the dialog without even noticing it, because you were typing at that time.
The simplest solution to the above I came up with is to have a third button, labeled "Again", that is selected by default, and when clicked will just immediately show the same nagger again. Therefore unless you happen to be typing TAB followed by SPACE _a_lot_ you should not happen to close the dialog accidentally.

## Warning

There is no error handling whatsoever, as it didn't seem to be worth writing. Anything can happen, though you'll most probably just get an exception. Also please note that the accuracy should be +/- 1 second, but there is no guarantee - in fact there is no guarantee this will work at all, for whatever purpose. You have been warned.
