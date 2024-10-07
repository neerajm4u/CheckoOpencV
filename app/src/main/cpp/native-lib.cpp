#include <jni.h>
#include <string>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/highgui.hpp>
#include<iostream>
#include <opencv2/core/utils/logger.hpp>
using namespace cv;
using namespace std;

void gray(){
  setLogLevel(cv::utils::logging::LOG_LEVEL_VERBOSE);
  // flip(src , src , 0);
  Mat a , b;
  a =  imread("1.png");
  cout<< a.depth();
  cvtColor(a, b , COLOR_BGRA2GRAY);
  imshow("gray" , b);
  waitKey(0);
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
                                                         jobject /* this */) {
  std::string hello = "Hello from C++";
//  myFlip();
//  myFun();

}
