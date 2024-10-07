//
// Created by user on 05-10-2024.
//
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/highgui.hpp>
#include<iostream>
#include <opencv2/core/utils/logger.hpp>
#include <opencv2/features2d.hpp>

using namespace cv;
using namespace std;

void myFlip(){
 // flip(src , src , 0);
  Mat a , b;
  a =  imread("1.png" , IMREAD_UNCHANGED);
  Scalar  intensity = a.at<uchar>(0,0);
  //Ptr<AKAZE> akaze = AKAZE::create();


  cout<< a.depth();
  cvtColor(a, b , COLOR_BGRA2GRAY);
  imshow("gray" , b);
  waitKey(0);
  return ;
}

void myFun(){

}