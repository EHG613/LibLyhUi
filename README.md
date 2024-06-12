# [制品库-updating](https://packages.aliyun.com/maven)

## 项目环境
- AGP(Android Gradle plugin): 7.0
- AS(Android Studio):4.0+
- maven-publish plugin:与AGP7.0之前版本配置不同

## 项目结构

- 整体结构
```text
.
|-- LocalRepo
|-- app
|-- gradle.properties
`-- lib_ywx
```

LocalRepo:由于AS新版本的原因导致引入aar/jar文件方式变更,如下所示:

1.在LocalRepo新建文件夹ywx_sdk_aar，将你的aar文件放入，然后在该目录下新建一个build.gradle文件
```gradle
configurations.maybeCreate("default")
artifacts.add("default", file('ywx-sdk-4.0.2.aar'))
```
2.在settings.gralde导入该工程
```gradle
include ':LocalRepo:ywx_sdk_aar'
```
3.在你需要依赖的工程里面的build.gradle中增加依赖
```gradle
api project(path: ':LocalRepo:ywx_sdk_aar')
//implementation project(path: ':LocalRepo:ywx_sdk_aar')
```

>如果有很多aar库，在LocalRepo创建不同文件夹放入aar，在setting.gradle分别导入

## 命名规则(暂定)
### 基础命名
- 包名必须小写
- 组件库都以lib_xx方式命名，都小写加下划线
- 导入的三方aar/jar都放在LocalRepo中，命名xxx_aar/jar，下划线

### 发布制品库命名

>group:artifactId:version-->net.liangyihui.android.third:ywx-sdk:4.0.2

- group:net.liangyihui.android.third/biz/base/ui,net.liangyihui.android固定，后缀根据具体情况划分,third：纯三方库的封装；biz:业务组件;base:基础组件;ui:自定义view，dialog等与业务无关
- version:分为release和SNAPSHOT，版本规则，release:4.0.2 单独版本号;SNAPSHOT:4.0.2-SNAPSHOT;在gradle.properties中定义版本号
- artifactId:如ywx-sdk,中间用中划线分隔；kotlin扩展增加后缀-ktx,如ywx-sdk-ktx;

## 参数配置-updating
## 发布脚本
```shell
//根据提示信息选择编译版本
./pub-ywx.sh
```
## 参考文献

- [https://www.jetbrains.com/help/space/publish-artifacts-from-a-gradle-project.html#publish-maven-artifacts-using-the-gradle-command-line-tool](https://www.jetbrains.com/help/space/publish-artifacts-from-a-gradle-project.html#publish-maven-artifacts-using-the-gradle-command-line-tool)
- [https://docs.gradle.org/7.0/userguide/publishing_maven.html#publishing_maven](https://docs.gradle.org/7.0/userguide/publishing_maven.html#publishing_maven)
- [https://www.cnblogs.com/baiyuas/p/14383723.html](https://www.cnblogs.com/baiyuas/p/14383723.html)