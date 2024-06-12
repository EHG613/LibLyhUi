#!/bin/bash
# author:lijian
function init() {
  echo '>>>>>>>>请选择您要发布的版本 1.release(稳定版) 2.snapshot(快照):'
  echo '>>>>>>>>请输入 1 到 2 之间的数字:'
  read aNum
  case $aNum in
  1)
    echo '>>>>>>>>开始编译release(稳定版)'
    ./gradlew clean :lib_share:assembleRelease
    ./gradlew task :lib_share:publish -Denv=prd
    ;;
  2)
    echo '>>>>>>>>开始编译snapshot(快照)'
    #    ./gradlew assembleRelease -Denv=snapshot
    ./gradlew clean :lib_share:assembleRelease
    ./gradlew task :lib_share:publish -Denv=snapshot
    ;;
  *)
    echo '>>>>>>>>您没有输入 1 到 2 之间的数字，是否重新输入y/n'
    read ayn
    case $ayn in
    "y" | "Y")
      init
      ;;
    esac
    ;;
  esac
}
init
