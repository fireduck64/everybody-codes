#!/bin/bash

set -eu

bazel build :all :Prob_deploy.jar

#time bazel-bin/Prob 

time java -jar bazel-bin/Prob_deploy.jar -Xss=1g 

