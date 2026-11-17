#!/bin/bash

set -eu

bazel build :all :Prob_deploy.jar

rm -f Prob_deploy.jar
cp bazel-bin/Prob_deploy.jar .

time bazel-bin/Prob 


