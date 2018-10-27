# 适配平台接入流程

## 基本流程

- 获取开发者授权，开发者授权后会将其加入到授权列表中，未在授权列表中的非授权用户禁止接入该功能
- 在项目中增加[源码上传]、[学校查找]、[课程解析]三大模块即可

## 接入Android

### 1.复制相关类

- 复制`asset/parse.html`到你的项目中
- 复制`com.zhuangfei.hputimetable.adapter_apis`包下的类到你的项目中

package ;