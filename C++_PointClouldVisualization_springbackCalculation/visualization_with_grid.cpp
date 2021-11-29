#include <iostream>
#include <cstring>
#include <vector>
// #include <opencv2/opencv.hpp>
#include <pcl/io/pcd_io.h>
#include <pcl/point_types.h>
#include <pcl/common/common_headers.h>
#include <pcl/visualization/pcl_visualizer.h>
#include <pcl/visualization/cloud_viewer.h>
#include <pcl/filters/voxel_grid.h>
#include <pcl/sample_consensus/method_types.h>
#include <pcl/sample_consensus/model_types.h>
#include <pcl/segmentation/sac_segmentation.h>
 

// convert to cloud

void toCloud_spc(
    ifstream& in_file, 
    pcl::PointCloud<pcl::PointXYZ>::Ptr cloud) {
  // read and convert
  std::cout << "reading txt file..." << std::endl;
  std::string line_str;
  std::vector<pcl::PointXYZ> point_vec;
  while(getline(in_file, line_str)) {
    std::vector<std::string> str_vec;
    std::string s;
    std::stringstream ss(line_str);
    while (getline(ss, s, ' ')) {
      str_vec.push_back(s);
    }
    if (str_vec.size() < 3) {
      std::cout << "error coordinate!" << endl;
      continue;
    }
    pcl::PointXYZ p;
    p.x = atof((str_vec[0]).c_str());
    p.y = atof((str_vec[1]).c_str());
    p.z = atof((str_vec[2]).c_str());

    point_vec.push_back(p);
  }

  // construct cloud
  std::cout << "constructing cloud..." << std::endl;
  cloud->width = (int)point_vec.size();
  cloud->height = 1;
  cloud->is_dense = false;
  cloud->points.resize(cloud->width * cloud->height);
  for (int i = 0; i < point_vec.size(); i++) {
    cloud->points[i].x = point_vec[i].x;
    cloud->points[i].y = point_vec[i].y;
    cloud->points[i].z = point_vec[i].z;
  }
  std::cout << "cloud size: " << cloud->points.size() << std::endl;
}


// get the intersection point of a line and a plane 
Eigen::Vector3f CalPlaneLineIntersectPoint(
    Eigen::Vector3f plane_normal,  // plane normal vector gout的(A,B,C)
    Eigen::Vector3f plane_point,   // a point on the plane gout的质心
    Eigen::Vector3f line_normal,   // line direction vector gin的(A,B,C)
    Eigen::Vector3f line_point) {  // a point on the line gin的质心
  Eigen::Vector3f intersect_point;
  float pn_x, pn_y, pn_z;
  pn_x = plane_normal[0];
  pn_y = plane_normal[1];
  pn_z = plane_normal[2];
  float pp_x, pp_y, pp_z;
  pp_x = plane_point[0];
  pp_y = plane_point[1];
  pp_z = plane_point[2];
  float ln_x, ln_y, ln_z;
  ln_x = line_normal[0];
  ln_y = line_normal[1];
  ln_z = line_normal[2];
  float lp_x, lp_y, lp_z;
  lp_x = line_point[0];
  lp_y = line_point[1];
  lp_z = line_point[2];
  
  float dp, t;
  dp = ln_x * pn_x + ln_y * pn_y + ln_z * pn_z;
  // whether the line is parallel to the plane 
  if (dp == 0) {
    intersect_point = Eigen::Vector3f(NULL, NULL, NULL);
  }
  else {
    t = ((pp_x - lp_x) * pn_x + (pp_y - lp_y) * pn_y + (pp_z - lp_z) * pn_z) / dp;
    intersect_point[0] = lp_x + ln_x * t;
    intersect_point[1] = lp_y + ln_y * t;
    intersect_point[2] = lp_z + ln_z * t;
  }
  // std::cout << "test" << std:: endl;
  return intersect_point;
}


int main() {
  // load file1 Gin
  ifstream in_file;
  in_file.open("/Users/yangbingqian/Desktop/SAIF/visualization/notab_model_v3.txt");
  assert(in_file.is_open());
  pcl::PointCloud<pcl::PointXYZ>::Ptr cloud(new pcl::PointCloud<pcl::PointXYZ>);
  toCloud_spc(in_file, cloud);
  

  // load file2 Gout
  ifstream in_file2;
  in_file2.open("/Users/yangbingqian/Desktop/SAIF/visualization/D4_1_completo.txt");
  assert(in_file2.is_open());
  pcl::PointCloud<pcl::PointXYZ>::Ptr cloud2(new pcl::PointCloud<pcl::PointXYZ>);
  toCloud_spc(in_file2, cloud2);

  // // f1 to 2D
  // pcl::PointCloud<pcl::PointXYZ>::Ptr cloud_2d(new pcl::PointCloud<pcl::PointXYZ>);
  // cloud_2d->points.resize(cloud->points.size());
  // for (int i = 0; i < cloud->points.size(); i++) {
  //   pcl::PointXYZ pt = cloud->points[i];
  //  std::cout << "file1 point " << pt.z << std::endl;
  // }

  // // f2 to 2D
  // pcl::PointCloud<pcl::PointXYZ>::Ptr cloud_2d_2(new pcl::PointCloud<pcl::PointXYZ>);
  // cloud_2d_2->points.resize(cloud->points.size());
  // for (int i = 0; i < cloud2->points.size(); i++) {
  //   pcl::PointXYZ pt = cloud2->points[i];
  //   std::cout << "file2 point " << pt.z << std::endl;
  // }

  // voxel grids
  pcl::PointCloud<pcl::PointXYZ>::Ptr cloud_filtered(new pcl::PointCloud<pcl::PointXYZ>);
  pcl::PointCloud<pcl::PointXYZ>::Ptr cloud_filtered2(new pcl::PointCloud<pcl::PointXYZ>);
  pcl::VoxelGrid<pcl::PointXYZ> sor;
  pcl::VoxelGrid<pcl::PointXYZ> sor2;

  //file1 filtered cloud_filtered
  sor.setInputCloud (cloud);
  sor.setLeafSize (2.0f, 2.0f, 0.1f);  // set the size of grid, the unit is meters
  sor.setSaveLeafLayout(true);
  sor.filter (*cloud_filtered);
  std::cout << "cloud_filtered size: " << cloud_filtered->points.size() << std::endl;
  // for (int i = 0; i < cloud_filtered->points.size(); i++) {
  //   pcl::PointXYZ pt = cloud_filtered->points[i];
  //   std::cout << "file1 point ： " << pt.z << std::endl;
  // }

  //file2 filtered cloud_filtered2
  sor2.setInputCloud (cloud2);
  sor2.setLeafSize (2.0f, 2.0f, 0.1f);  // set the size of grid, the unit is meters
  sor2.setSaveLeafLayout(true);
  sor2.filter (*cloud_filtered2);
  std::cout << "cloud_filtered2 size: " << cloud_filtered2->points.size() << std::endl;

  // get each grid
  //file1 grids
  // std::vector<pcl::PointCloud<pcl::PointXYZ>::Ptr> grids(cloud_filtered->points.size());
  std::vector<pcl::PointCloud<pcl::PointXYZ>::Ptr> grids;
  for (int i = 0; i < cloud_filtered->points.size(); i++) {
    pcl::PointCloud<pcl::PointXYZ>::Ptr cloud_grid(new pcl::PointCloud<pcl::PointXYZ>);
    grids.push_back(cloud_grid);
  }

  //file2 grids2这里能用同一个grid吗，size和区域一样吗
  std::vector<pcl::PointCloud<pcl::PointXYZ>::Ptr> grids2;
  for (int i = 0; i < cloud_filtered2->points.size(); i++) {
    pcl::PointCloud<pcl::PointXYZ>::Ptr cloud_grid_2(new pcl::PointCloud<pcl::PointXYZ>);
    grids2.push_back(cloud_grid_2);
  }

  //file1 grid centroid
  for (int i = 0; i < cloud->points.size(); i++) {
    Eigen::Vector3i pt_3i = sor.getGridCoordinates(
        cloud->points[i].x, cloud->points[i].y, cloud->points[i].z);
    int idx = sor.getCentroidIndexAt(pt_3i);
    grids[idx]->points.push_back(cloud->points[i]);
  }

  //file2 grid centroid
  for (int i = 0; i < cloud2->points.size(); i++) {
    Eigen::Vector3i pt_3i_2 = sor2.getGridCoordinates(
        cloud2->points[i].x, cloud2->points[i].y, cloud2->points[i].z);
    int idx2 = sor2.getCentroidIndexAt(pt_3i_2);
    grids2[idx2]->points.push_back(cloud2->points[i]);
  }

  // get normal vector
  //file 1's normal vector
  std::vector<pcl::ModelCoefficients::Ptr> coefficient_vec;
  std::vector<pcl::PointIndices::Ptr> inliers_vec;
  std::vector<pcl::SACSegmentation<pcl::PointXYZ> > seg_vec(grids.size());
  std::vector<float> d_vec(grids.size());

  std::vector<pcl::ModelCoefficients::Ptr> coefficient_vec2;
  std::vector<pcl::PointIndices::Ptr> inliers_vec2;
  std::vector<pcl::SACSegmentation<pcl::PointXYZ> > seg_vec2(grids2.size());
  std::vector<float> d_vec2(grids2.size());

  for (int i = 0; i < grids.size(); i++) {
    pcl::ModelCoefficients::Ptr coefficient_grid(new pcl::ModelCoefficients);
    coefficient_vec.push_back(coefficient_grid);
    pcl::PointIndices::Ptr inliers_grid(new pcl::PointIndices);
    inliers_vec.push_back(inliers_grid);

    pcl::ModelCoefficients::Ptr coefficient_grid2(new pcl::ModelCoefficients);
    coefficient_vec2.push_back(coefficient_grid2);
    pcl::PointIndices::Ptr inliers_grid2(new pcl::PointIndices);
    inliers_vec2.push_back(inliers_grid2);
  }
  
  // 最后一个attri是： <<"grid_location"
  ofstream out;
  out.open("/Users/yangbingqian/Desktop/SAIF/visualization/error.txt",ios::trunc);
  out<<"distance," << "centroid_point_x,"<<"centroid_point_y,"<<"centroid_point_z,"<<'\n';

  for (int i = 0; i < grids.size(); i++) {
    if (grids[i]-> points.size() < 5 || grids2[i]-> points.size() < 5){
      continue;
    }
    // coefficient_vec[i] = boost::make_shared <pcl::ModelCoefficients>();
    // inliers_vec[i] = boost::make_shared <pcl::PointIndices>();
    seg_vec[i].setOptimizeCoefficients(true);
    seg_vec[i].setModelType(pcl::SACMODEL_PLANE);
    seg_vec[i].setMethodType(pcl::SAC_RANSAC);
    seg_vec[i].setDistanceThreshold(0.01);
    seg_vec[i].setInputCloud (grids[i]);
    seg_vec[i].segment (*inliers_vec[i], *coefficient_vec[i]);
    d_vec[i] = -1 * (coefficient_vec[i]->values[0] * cloud_filtered->points[i].x + 
        coefficient_vec[i]->values[1] * cloud_filtered->points[i].y +
        coefficient_vec[i]->values[2] * cloud_filtered->points[i].z);

    
    // coefficient_vec[i] = boost::make_shared <pcl::ModelCoefficients>();
    // inliers_vec[i] = boost::make_shared <pcl::PointIndices>();
    seg_vec2[i].setOptimizeCoefficients(true);
    seg_vec2[i].setModelType(pcl::SACMODEL_PLANE);
    seg_vec2[i].setMethodType(pcl::SAC_RANSAC);
    seg_vec2[i].setDistanceThreshold(0.01);
    seg_vec2[i].setInputCloud (grids2[i]);
    seg_vec2[i].segment (*inliers_vec2[i], *coefficient_vec2[i]);
    d_vec2[i] = -1 * (coefficient_vec2[i]->values[0] * cloud_filtered2->points[i].x + 
        coefficient_vec2[i]->values[1] * cloud_filtered2->points[i].y +
        coefficient_vec2[i]->values[2] * cloud_filtered2->points[i].z);
    
    Eigen::Vector3f palnein_normal;
    palnein_normal[0] = coefficient_vec[i]->values[0];
    palnein_normal[1] = coefficient_vec[i]->values[1];
    palnein_normal[2] = coefficient_vec[i]->values[2];

    Eigen::Vector3f palneout_normal;
    palneout_normal[0] = coefficient_vec2[i]->values[0];
    palneout_normal[1] = coefficient_vec2[i]->values[1];
    palneout_normal[2] = coefficient_vec2[i]->values[2];

    Eigen::Vector3f palnein_point;
    palnein_point[0] = cloud_filtered->points[i].x;
    palnein_point[1] = cloud_filtered->points[i].y;
    palnein_point[2] = cloud_filtered->points[i].z;

    Eigen::Vector3f palneout_point;
    palneout_point[0] = cloud_filtered2->points[i].x;
    palneout_point[1] = cloud_filtered2->points[i].y;
    palneout_point[2] = cloud_filtered2->points[i].z;


    // std::cout << "grid coordinate: " << Eigen::Vector3f pcl::VoxelGrid< PointT >::getGridCoordinates(palneout_point[0],palneout_point[1],palneout_point[2]) << std::endl; 

    Eigen::Vector3f intersect = CalPlaneLineIntersectPoint(palnein_normal,palnein_point,palneout_normal,palneout_point);
    if ((intersect[2] - palnein_point[2]) >= 0){
      float distance = std::sqrt(std::pow(intersect[0]-palnein_point[0],2) + std::pow(intersect[1]-palnein_point[1],2) + std::pow(intersect[2]-palnein_point[2],2));
      // std::cout << "distance " << distance <<" point "<<palneout_point[0]<< "\n"<< std::endl;
      out<< distance << ","<<palneout_point[0]<<","<<palneout_point[1]<<","<<palneout_point[2]<< '\n';
    } else{
      float distance = - std::sqrt(std::pow(intersect[0]-palnein_point[0],2) + std::pow(intersect[1]-palnein_point[1],2) + std::pow(intersect[2]-palnein_point[2],2));
      // std::cout << "distance " << distance <<"point"<<palneout_point<< "\n"<< std::endl;
      out<< distance << ","<<palneout_point[0]<<","<<palneout_point[1]<<","<<palneout_point[2]<< '\n';
    }

    ////write
  }
  out.close();



  /* Note:
  Linear equation: Ax+By+Cz+D=0, 
  A=coefficient_vec[i]->values[0], B=coefficient_vec[i]->values[1], C=coefficient_vec[i]->values[2],
  bring in the centroid point in cloud_filtered to get the D,
  then, we can use the linear equation to get the point of intersection with a plane (Function: CalPlaneLineIntersectPoint).
  */ 

  // visualization file1
  pcl::visualization::PCLVisualizer viewer("demostrador_caliente_v3 Gout viewer");
  pcl::visualization::PointCloudColorHandlerCustom<pcl::PointXYZ> 
      singleColor(cloud_filtered, 255, 255, 255);
  viewer.addPointCloud<pcl::PointXYZ>(cloud_filtered, singleColor, "cloud");

  std::cout << "end " << std::endl;
  // visualization file2
  // pcl::visualization::PCLVisualizer viewer2("Gout viewer");
  // pcl::visualization::PointCloudColorHandlerCustom<pcl::PointXYZ> 
  //     singleColor2(cloud_filtered2, 255, 255, 255);
  // viewer2.addPointCloud<pcl::PointXYZ>(cloud_filtered2, singleColor2, "cloud2");



  in_file.close(); 
  in_file2.close(); 

  while (!viewer.wasStopped()) {
    viewer.spinOnce();
  }
  
  return 0;
}
