
touch $ANDROID_HOME/hasret-test.txt
ls $ANDROID_HOME/platforms
echo "Pre-build custom script1..."

cd $ANDROID_HOME
ls
echo "======= ANDROID_HOME size ======="
du -sh $ANDROID_HOME

# rm -rf $ANDROID_HOME
echo "====================================="
echo "Current working directory: "
echo $(pwd)
echo "====================================="
