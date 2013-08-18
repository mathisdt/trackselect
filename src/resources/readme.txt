
What's the purpose of TrackSelect?
==================================

This little front-end for some linux console tools makes it very easy
and convenient to extract tracks and/or chapters from a video medium,
e.g. a DVD or a Blu-Ray disc, and to name the extracted files meaningfully.


Is there anything I have to do before I can use the program?
============================================================

Yes!

1. You should check that the tools mentioned in commands.properties are present
   on your system. If not, consult the manual or the internet on how to install
   them. Also feel free to use the tools of your choice, you just have to put
   the commands inside the mentioned file. All commands are executed using "sh -c".
   The long-running commands used to extract the actual data are executed using
   the prefix configured in "external.process" (in addition to "sh -c").

2. You should also check that any paths in commands.properties are fitted for
   your system.


What effect do the controls on the UI have?
===========================================

The "Read Disc" button does exactly what it says: it reads basic disc data and
then displays it. You can use the "Global Prefix" to not repeat yourself when
typing the target file names: anything inserted as prefix will be put before
the names in the list.
If you want to pre-check if a title or chapter is what you think it is, you can
use "Play" to review it. Then, when you are sure, give it a name to the right,
and you're ready to go. Use "Queue Extraction" to start the process and watch
the progress below.


How can I debug my custom-made commands?
========================================

You should start TrackSelect from a console window and watch the output it
produces. On every command, it prints out what it actually does (with
variables already replaced).
