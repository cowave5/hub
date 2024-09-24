#! /bin/bash

pwd=$(cd "$(dirname "$0")" && pwd) || { echo "cd failed"; exit 1; }
if [ -f "$pwd/setenv.sh" ];then
    source "$pwd/setenv.sh"
fi
if [ -f "$pwd/bin/setenv.sh" ];then
    source "$pwd/bin/setenv.sh"
fi

install(){
    [ -n "${app_name}" ] || { LogError "app_name not set"; exit 1; }
    [ -n "${app_home}" ] || { LogError "app_home not set"; exit 1; }
    [ -n "${app_version}" ] || { LogError "app_version not set"; exit 1; }
    if [ -d "$app_home" ];then
        v_old=$(sed -n '/^app_version=.*/p' "$app_home/bin/setenv.sh" 2>/dev/null | sed 's/app_version=//g' 2>/dev/null)
        LogError "install terminated: $app_name was already installed, version=$v_old"
        exit 1
        #compare=`awk -v a=$v_old -v b=$v_new 'BEGIN{print(a>=b)?"0":"1"}'`
        #if [ $compare = "0" ]; then
        #    LogWarn "$app_name version $v_old was already installed, install cancelled."
        #    exit 1
        #fi
    fi

    LogInfo "prepare to install $app_name, version: $app_version"
    mkdir -p "$app_home/log"
    install_copy || {
        LogError "install terminated unexpectedly: copy failed."
        if [ -n "$app_home" ] && [ "$app_home" != "/" ]; then
            rm -rf "$app_home"
        fi
        exit 1
    }

    install_time=$(date "+%Y-%m-%d %H:%M:%S")
    sed -i 's/export install_time=.*/export install_time="'"$install_time"'"/' "$app_home/bin/setenv.sh"

    cd "$app_home" || { LogError "cd failed: $app_home"; exit 1; }
    find "$app_home" -type f -name "*.sh" -exec chmod 744 {} \;
    find "$app_home" -type f -name "*.sh" -exec dos2unix {} \; 2>/dev/null
    find "$app_home" -type f -name "*.xml" -exec dos2unix {} \; 2>/dev/null
    find "$app_home" -type f -name "*.properties" -exec dos2unix {} \; 2>/dev/null
    LogSuccess "$app_name install success."
    sh "$app_home/bin/run.sh" start
}

uninstall(){
    [ -n "${app_name}" ] || { LogError "app_name not set"; exit 1; }
    [ -n "${app_home}" ] || { LogError "app_home not set"; exit 1; }
    if [ -d "$app_home" ];then
        if [ -n "$app_home" ] && [ ! "$app_home" = "/" ];then
            sh "$app_home/bin/run.sh" stop
            uninstall_bak
            rm -rf "$app_home"
            LogSuccess "$app_name cleared[home=$app_home]."
        fi
    fi
    LogSuccess "$app_name uninstall success."
}

sources(){
    #git fetch --all
    #git reset --hard origin/master

    appName=$app_name
    if [ -z "$app_name" ]; then
      appName=$(grep -B 4 packaging pom.xml | grep artifactId | awk -F ">" '{print $2}' | awk -F "<" '{print $1}')
    fi
    buildTime=$(date "+%Y-%m-%d %H:%M:%S")
    appVersion=$(grep -B 4 packaging pom.xml | grep version | awk -F ">" '{print $2}' | awk -F "<" '{print $1}')

    #commit=`svn info|awk 'NR==9{print $4}'`
    commit=$(git log -n 1 --pretty=oneline | awk '{print $1}')
    branch=$(git name-rev --name-only HEAD)
    codeVersion="$branch $commit"

    commit_msg=$(git log --pretty=format:"%s" -1)
    commit_time=$(git log --pretty=format:"%cd" -1)
    commit_author=$(git log --pretty=format:"%an" -1)

    if [ -f target/classes/META-INF/info.yml ];then
        ## info.application
        replace target/classes/META-INF/info.yml name "$appName" 1
        replace target/classes/META-INF/info.yml version "$appVersion" 1
        replace target/classes/META-INF/info.yml build "$buildTime" 1
        ## info.commit
        replace target/classes/META-INF/info.yml version \""$codeVersion"\" 2
        replace target/classes/META-INF/info.yml Msg \""$commit_msg"\" 1
        replace target/classes/META-INF/info.yml Time "$commit_time" 1
        replace target/classes/META-INF/info.yml Author "$commit_author" 1
        ## spring.application.name
        replace target/classes/META-INF/info.yml name "$appName" 2
    fi
}

build(){
    jarName=$(grep -B 4 packaging pom.xml | grep artifactId | awk -F ">" '{print $2}' | awk -F "<" '{print $1}')
    appName=$app_name
    if [ -z "$app_name" ]; then
        appName=$jarName
    fi
    appVersion=$(grep -B 4 packaging pom.xml | grep version | awk -F ">" '{print $2}' | awk -F "<" '{print $1}')

    buildTime=$(date "+%Y-%m-%d %H:%M:%S")
    commit=$(git log -n 1 --pretty=oneline | awk '{print $1}')
    branch=$(git name-rev --name-only HEAD)
    codeVersion="$branch $commit"

    mkdir -p target/"$appName"_"$appVersion"/lib
    cp -rf bin target/"$appName"_"$appVersion"
    if [ -f changelog.md ];then
        cp -f changelog.md target/"$appName"_"$appVersion"
    else
        touch target/"$appName"_"$appVersion"/changelog.md
    fi
    mv target/"$appName"_"$appVersion"/bin/install.sh target/"$appName"_"$appVersion"

    cd target || { echo "build target not found"; exit 1; }
    if [ -f "$jarName"-"$appVersion"-encrypted.jar ];then
        cp "$jarName"-"$appVersion"-encrypted.jar "$appName"_"$appVersion"/lib/"$appName"-"$appVersion".jar
    else
        cp "$jarName"-"$appVersion".jar "$appName"_"$appVersion"/lib/"$appName"-"$appVersion".jar
    fi

    cp -rf classes/config "$appName"_"$appVersion"

    find "$appName"_"$appVersion" -type f -name "*.sh" -exec chmod 744 {} \;
    find "$appName"_"$appVersion" -type f -name "*.sh" -exec dos2unix {} \;

    sed -i 's#export app_name=.*#export app_name="'"$appName"'"#' "$appName"_"$appVersion"/bin/setenv.sh
    sed -i 's#export app_version=.*#export app_version="'"$appVersion"'"#' "$appName"_"$appVersion"/bin/setenv.sh
    sed -i 's#export code_version=.*#export code_version="'"$codeVersion"'"#' "$appName"_"$appVersion"/bin/setenv.sh
    sed -i 's#export build_time=.*#export build_time="'"$buildTime"'"#' "$appName"_"$appVersion"/bin/setenv.sh

    build_time=$(date "+%Y%m%d")
    build_tar="$appName"_"$appVersion"_"$build_time".tar.gz
    tar zcvf "$build_tar" "$appName"_"$appVersion"
    md5sum "$build_tar" > "$build_tar.md5"
}

case "$1" in
    sources)
        sources
        ;;
    build)
        build
        ;;
    install)
        install
        ;;
    uninstall)
        uninstall
        ;;
    reinstall)
        uninstall
        install
        ;;
    *)
    LogError $"usage: $0 {install|uninstall|reinstall}"
    exit 1
esac
exit 0
