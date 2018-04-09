#!/bin/sh
echo "nohup java -Xmx1024M -Xms1024M -Xmn1024M -XX:PermSize=64M -XX:MaxPermSize=128m -XX:+CMSPermGenSweepingEnabled -XX:+CMSClassUnloadingEnabled -jar pop_adapter-executable.jar  >adapter.out &"
nohup java -Xmx1024M -Xms1024M -Xmn1024M -XX:PermSize=64M -XX:MaxPermSize=128m -XX:+CMSPermGenSweepingEnabled -XX:+CMSClassUnloadingEnabled -jar pop-adapter-executable.jar start > adapter.out &