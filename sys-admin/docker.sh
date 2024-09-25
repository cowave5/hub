## 执行时机：maven jar之前，工作目录为Target，环境变量定义在setenv.sh，
build_prepare(){
    ## 从pom.xml获取app_name和app_version，app_name优先取setenv.sh中的定义
    if [ -z "$app_name" ]; then
      app_name=$(grep -B 4 packaging ../pom.xml | grep artifactId | awk -F ">" '{print $2}' | awk -F "<" '{print $1}')
    fi
    buildTime=$(date "+%Y-%m-%d %H:%M:%S")
    app_version=$(grep -B 4 packaging ../pom.xml | grep version | awk -F ">" '{print $2}' | awk -F "<" '{print $1}')
    ## 获取代码版本信息
    commit=$(git log -n 1 --pretty=oneline | awk '{print $1}')
    branch=$(git name-rev --name-only HEAD)
    codeVersion="$branch $commit"
    commit_msg=$(git log --pretty=format:"%s" -1)
    commit_time=$(git log --pretty=format:"%cd" -1)
    commit_author=$(git log --pretty=format:"%an" -1)
    echo "${app_name} ${app_version}(${branch} ${commit} @${commit_author})"
    ## 将信息写到META-INF/info.yml（如果有的话），打到jar里面
    if [ -f classes/META-INF/info.yml ];then
        ## info.application
        replace classes/META-INF/info.yml name "$app_name" 1
        replace classes/META-INF/info.yml version "$app_version" 1
        replace classes/META-INF/info.yml build "$buildTime" 1
        ## info.commit
        replace classes/META-INF/info.yml version \""$codeVersion"\" 2
        replace classes/META-INF/info.yml Msg \""$commit_msg"\" 1
        replace classes/META-INF/info.yml Time "$commit_time" 1
        replace classes/META-INF/info.yml Author "$commit_author" 1
        ## spring.application.name
        replace classes/META-INF/info.yml name "$app_name" 2
    fi
}

## 执行时机：maven package之后，工作目录为Target，环境变量定义在setenv.sh
build_docker(){
    ## 从pom.xml获取app_name和app_version，app_name优先取setenv.sh中的定义
    jarName=$(grep -B 4 packaging ../pom.xml | grep artifactId | awk -F ">" '{print $2}' | awk -F "<" '{print $1}')
    if [ -z "$app_name" ]; then
        app_name=$jarName
    fi
    app_version=$(grep -B 4 packaging ../pom.xml | grep version | awk -F ">" '{print $2}' | awk -F "<" '{print $1}')
    ## 创建打包目录，拷贝：/bin、changelog.md
    app_source="$app_name"_"$app_version"
    mkdir -p "$app_source"/lib
    cp -rf bin "$app_source"
    if [ -f ../changelog.md ];then
        cp -f ../changelog.md "$app_source"
    else
        touch "$app_source"/changelog.md
    fi
    ## 将bin目录中的install.sh移到根目录下，因为只在安装时使用下，不包含在安装内容中
    mv "$app_source"/bin/install.sh "$app_source"
    ## 拷贝jar包，如果使用了classfinal加密，就重命名下
    if [ -f "$jarName"-"$app_version"-encrypted.jar ];then
        cp "$jarName"-"$app_version"-encrypted.jar "$app_source"/lib/"$app_name"-"$app_version".jar
    else
        cp "$jarName"-"$app_version".jar "$app_source"/lib/"$app_name"-"$app_version".jar
    fi
    ## 拷贝：/config，打jar的内容中排除了resources/config，放在jar包外面方便修改
    cp -rf classes/config "$app_source"
    ## 在setenv.sh中记录一些代码版本信息
    commit=$(git log -n 1 --pretty=oneline | awk '{print $1}')
    branch=$(git name-rev --name-only HEAD)
    codeVersion="$branch $commit"
    buildTime=$(date "+%Y-%m-%d %H:%M:%S")
    sed -i 's#export build_time=.*#export build_time="'"$buildTime"'"#' "$app_source"/bin/setenv.sh
    sed -i 's#export app_name=.*#export app_name="'"$app_name"'"#' "$app_source"/bin/setenv.sh
    sed -i 's#export app_version=.*#export app_version="'"$app_version"'"#' "$app_source"/bin/setenv.sh
    sed -i 's#export code_version=.*#export code_version="'"$codeVersion"'"#' "$app_source"/bin/setenv.sh
    ## 构建镜像
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

