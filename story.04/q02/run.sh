#!/bin/bash

set -eu

bazel build :all :Prob_deploy.jar

time bazel-bin/Prob 


