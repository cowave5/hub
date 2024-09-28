## 已设置环境变量：
## app_name    默认从pom.xml获取，可以在env.properties中设置覆盖
## app_version 默认从pom.xml获取，可以在env.properties中设置覆盖
## app_source="$app_name"_"$app_version"

## app_source目录已创建，内容包括：
## target/app_source
##   ├─bin
##   │  └─env.properties
##   │  └─run.sh
##   │  └─setenv.sh
##   ├─lib
##   │  └─${app_name}_${app_version}.jar
##   ├─config
##   │  └─application.yml
##   │  └─...
##   ├─install.sh
##   └─changelog.md

## 工作目录为target
build(){
cp ../simsun.ttf .

cat <<EOF > Dockerfile
FROM ubuntu:20.04

ENV LANG C.UTF-8

WORKDIR /opt/cowave/${app_name}

RUN mkdir -p /usr/share/fonts/simsun

ADD simsun.ttf /usr/share/fonts/simsun
ADD ${app_source}/changelog.md /opt/cowave/${app_name}/
ADD ${app_source}/bin /opt/cowave/${app_name}/bin/
ADD ${app_source}/lib /opt/cowave/${app_name}/lib/
ADD ${app_source}/config /opt/cowave/${app_name}/config/

RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo 'Asia/Shanghai' > /etc/timezone && \
    apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get install -y ttf-mscorefonts-installer && \
    apt-get install -y fontconfig && \
    chmod -R 755 /usr/share/fonts/simsun && \
    mkfontscale && \
    mkfontdir && \
    fc-cache -fv

ENTRYPOINT ["bin/run.sh", "up"]
EOF

docker build -t cowave/$app_name:$app_version .
}

