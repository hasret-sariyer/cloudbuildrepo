
touch $ANDROID_HOME/hasret-test.txt
ls $ANDROID_HOME/platforms
echo "Pre-build custom script1..."

ANDROID_NDK=$(pwd)/android-ndk
curl -o "${ANDROID_NDK}.zip" https://dl.google.com/android/repository/android-ndk-r16b-linux-x86_64.zip

cd $ANDROID_HOME
ls
echo "======= ANDROID_HOME size ======="
du -sh $ANDROID_HOME/*

# rm -rf $ANDROID_HOME
echo "====================================="
echo "Current working directory: "
echo $(pwd)
echo "====================================="

exit -1
