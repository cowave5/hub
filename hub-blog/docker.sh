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

WORKDIR ${app_home}

RUN mkdir -p /usr/share/fonts/simsun

ADD simsun.ttf /usr/share/fonts/simsun
ADD bin ${app_home}/bin/
ADD classes/config ${app_home}/config/
ADD "$app_name"-"$app_version".jar ${app_home}/lib/

RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo 'Asia/Shanghai' > /etc/timezone && \
    apt-get update && \
    apt-get install -y curl && \
    apt-get install -y openjdk-17-jdk && \
    apt-get install -y ttf-mscorefonts-installer && \
    apt-get install -y fontconfig && \
    chmod -R 755 /usr/share/fonts/simsun && \
    mkfontscale && \
    mkfontdir && \
    fc-cache -fv

ENTRYPOINT ["bin/run.sh", "up"]
EOF

docker build -t $app_name:$app_version .
}

