#原始的分布式自动化测试镜像是有缺点的：没有安装中文字符集。这样跑自动化的时候，浏览器显示中文都是乱码。
#该Dockerfile文件用于对镜像进行改造--安装中文字符集
FROM selenium/node-chrome:4.0.0-20211102

USER root

RUN apt-get update && apt-get -y install ttf-wqy-microhei ttf-wqy-zenhei && apt-get clean

