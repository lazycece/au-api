# Au Api 

[![Maven Central](https://img.shields.io/maven-central/v/com.lazycece.au/au-api)](https://search.maven.org/search?q=au-api)
[![License](https://img.shields.io/badge/license-Apache--2.0-green)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![GitHub release](https://img.shields.io/badge/release-download-orange.svg)](https://github.com/lazycece/au-api/releases)

`Au api`是一个基于[Au框架](https://github.com/lazycece/au)实现的api安全框架，支持token认证、防拦截、参数签名以及数据加密等，适用于任何基于`servlet`的web项目。

## 如何使用？

> `spring boot`项目请移步[`au-api-spring-boot`](https://github.com/lazycece/au-api-spring-boot)

### Maven Dependency

```xml
<dependency>
  <groupId>com.lazycece.au</groupId>
  <artifactId>au-api</artifactId>
  <version>${au.core.version}</version>
</dependency>
```
### `Au api`集成

请直接查看使用样例 [au-api-example](https://github.com/lazycece/au-api/tree/master/au-api-example)

## 框架说明

### 请求参数

框架提供公共参数如下：

- `time`: 请求发起时间戳
- `salt`: 盐值
- `sign`: 参数签名值
- `data`: 具体接口参数的json字符串进行加密后的字符串

使用者可自行添加所需要的公共参数，如设备号`deviceId`、 版本号`version`、版本code等，只需继承`ApiParams`并告知框架自定义的参数类即可。

对于`Content-Type`，`POST`请求可支持`application/json`与`application/x-www-form-urlencoded`；而如果需要采用`multipart/form-data`协议上传文件时，文件参数可与公共参数同级。

### 返回参数

对于接口返回参数，框架只负责加密，而将决定权交给了使用者，方便更友好的集成使用。

### 用户鉴权

框架在接口调用时会通过校验`token`进行用户鉴权，用于判断用户是否登陆、会话是否过期以及是否是非法调用等等，所以接口调用均需在请求`Headers`中附上用户的`token`。
在会话控制中，`token`会默认在每次请求完成后刷新，当然`token`也会失效，默认其有效时间是30分钟。

使用者可自行更改`token`相关信息，如header名、有效时长以及是否在每次请求后进行刷新等。

### 防拦截

每次请求会对`time`参数进行校验，防止被请求被拦截篡改，从发起请求到收到请求之间时间间隔默认不超过3分钟。

### 签名验证

接口交互时会进行签名验证，签名`sign`动态生成方式伪代码如下：

```
str = param1=param1_value&param2=param2_value&param3=param3_value&key=secrt_key
sign = md5(str).toUpperCase()
```

伪代码关键说明如下：

- `secrt_key`为密钥，需要服务端提供；
- 签名串`str`中的参数`param`需要按参数名升序排列；
- 参数值为空的参数不参与签名；

### 数据加解密

接口交互中，请求和返回数据中`data`参数会进行加解密操作，可支持`AES-ECB`和`DES3-ECB`两种算法。

加密伪代码如下：

```
data = base64_encode(encrypt( md5(salt + secrt_key), data_json))
```

解密伪代码如下：

```
json_data = decrypt(md5(salt + secrt_key), base64_decode(data))
```

## Au Api 协议模板

提供基于[au-api-example](https://github.com/lazycece/au-api/tree/master/au-api-example)的[接口协议模板](au-api-template.md).

## License

[Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0.html)
