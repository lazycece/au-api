# Au Api 接口协议模板

## 修定历史

| 版本    | 时间       | 作者    | 备注                |
| ------- | ---------- | ------- | ------------------- |
| v1.0.0    | 2020-02-07 | lazycece    | 基于`au-api-example`完成接口协议模板 |

## 1 协议说明

### 1.1 接口交互 

#### 1.1.1 请求参数

公共请求参数定义如下：

- `time`: 请求发起时间戳
- `salt`: 盐值
- `sign`: 参数签名值
- `data`: 具体接口参数的json字符串进行加密后的字符串
- `deviceId`: 设备号
- `version`: 客户端版本号
- `versionCode`: 客户端版本code

`POST`请求请设置`Content-Type`为`application/json;charset=UTF-8`；采用`multipart/form-data`协议上传文件时，文件参数与公共参数同级。

#### 1.1.2 返回参数

接口返回的数据均`application/json`的形式展现，具体参数定义如下：

- `code`: 返回码
- `message`: 描述信息
- `salt`: 盐值
- `data`: 具体接口返回数据转json后加密值

### 1.2 用户鉴权

接口调用时会通过校验`token`进行用户鉴权，用于判断用户是否登陆、会话是否过期以及是否是非法调用等等。用于校验的`token`会在用户登陆成功后附在返回信息的`Headers`中，后续接口调用均需在请求`Headers`中附上用户的`token`。

`Headers`中`token`字段名如下：
```
Headers = [TOKEN-HEADER:""]
```

在会话控制中，`token`会默认在每次请求完成后刷新；若长时间未请求，`token`也会失效，默认其有效时间是30分钟，当失效后终端需主动重新登陆以刷洗`token`。

### 1.2 防拦截

每次请求会对`time`参数进行校验，防止被请求被拦截篡改，从发起请求到收到请求之间时间间隔默认不超过3分钟。

### 1.4 签名验证

接口交互时会进行签名验证，签名`sign`动态生成方式伪代码如下：

```
str = param1=param1_value&param2=param2_value&param3=param3_value&key=secrt_key
sign = md5(str).toUpperCase()
```

伪代码关键说明如下：

- `secrt_key`为密钥，需要服务端提供；
- 签名串`str`中的参数`param`需要按参数名升序排列；
- 参数值为空的参数不参与签名；

### 1.5 数据加解密

接口交互中，请求和返回数据中`data`参数会进行加解密操作，采用`DES3-ECB`对称加密算法。

加密伪代码如下：

```
data = base64_encode(des3_ecb_encode( md5(salt + secrt_key), data_json))
```

解密伪代码如下：

```
json_data = des3_ecb_decode(md5(salt + secrt_key), base64_decode(data))
```

## 2 接口定义

接口url前缀`/au-api`

### 2.1 用户模块

#### 2.1.1 用户登陆

- request data:

```json
Content-Type: application/json;charset=UTF-8
POST /u/login
{
    "username": "lazycece", 
    "password": "123456"
}
```

- response Headers：

```
Headers = [AUTH-TOKEN:"token值"]
```

## 3 附录

### 3.1 返回码定义

| 返回码 | 含义   |
| ------ | ------ |
| 0000 | success |
| 8000 | parameter error|
| 9990 | invalid token |
| 9999 | fail |






