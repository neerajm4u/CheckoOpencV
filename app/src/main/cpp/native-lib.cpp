


#include <android/asset_manager.h>
#include <iostream>
#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/core/utils/logger.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/imgproc.hpp>
#include <string>
#include <android/log.h>

using namespace cv;
using namespace std;


AAssetManager* assetManager;



std::string jstringToStdString(JNIEnv* env, jstring jstr) {
  const char* cstr = env->GetStringUTFChars(jstr, nullptr);
  std::string str(cstr);
  env->ReleaseStringUTFChars(jstr, cstr);
  return str;
}

std::string getAssetFilePath2(const std::string& filename) {
  // Files in the assets folder cannot be accessed via a traditional file path.
  // Instead, use the AAssetManager to read the file.
  AAsset* asset = AAssetManager_open(assetManager, filename.c_str(), AASSET_MODE_UNKNOWN);
  if (asset != nullptr) {
    // Successfully opened the asset
    AAsset_close(asset); // Always close the asset when done
    __android_log_print(ANDROID_LOG_INFO, "MyTag", "The value of x is %s", filename.c_str());
    return filename; // Indicate success
  } else {
  __android_log_print(ANDROID_LOG_INFO, "MyTag", "The value of x is %s", filename.c_str());
    return  filename; // Indicate failure
  }

}

void myFlip( std::string name){
  // flip(src , src , 0);
  __android_log_print(ANDROID_LOG_INFO, "Neeraj", "The value of x is %s", name.c_str());

  Mat a , b;
  a =  imread(name );
  __android_log_print(ANDROID_LOG_INFO, "Neeraj", "The value of x is " );

  Scalar  intensity = a.at<uchar>(0,0);
  //Ptr<AKAZE> akaze = AKAZE::create();
  __android_log_print(ANDROID_LOG_INFO, "Neeraj", "The value of x is %i , %i", a.size().width , a.size().height );

  cout<< a.depth();
  __android_log_print(ANDROID_LOG_INFO, "Neeraj", "The value of x is %i  , %s", a.channels()  , a.empty() ? "true" : "false");

  cvtColor(a, a , COLOR_BGR2GRAY);

 // imshow("gray" , b);
 // waitKey(0);
  return ;
}


void gray(){
  setLogLevel(cv::utils::logging::LOG_LEVEL_VERBOSE);
  // flip(src , src , 0);
  Mat a , b;
  a =  imread("1.png");
  cout<< a.depth();
  cvtColor(a, b , COLOR_BGRA2GRAY);
  //imshow("gray" , b);
 // waitKey(0);
  return ;


}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_checkoopencv_MainActivity_stringFromJNI(JNIEnv *env,
                                                         jobject /* this */) {
  std::string hello = "Hello from C++";
  return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_checkoopencv_MainActivity_flip(JNIEnv *env,
                                                         jobject /* this */,
                                                jstring name
                                                ) {
  std::string hello = "Hello from C++";
  __android_log_print(ANDROID_LOG_INFO, "Neeraj", "The value of x is %s", hello.c_str());

  myFlip(jstringToStdString(env , name));
//  myFun();
  //gray();

}
