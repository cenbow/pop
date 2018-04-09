#!/bin/sh

for i in `ps -ef | grep cep_adapter-executable.jar | grep -v grep |awk '{print $2}'`
do
 echo "kill -9 $i"
 kill -9 $i;
done

echo "nohup java -jar cep_adapter-executable.jar stop"
nohup java -jar cep_adapter-executable.jar stop