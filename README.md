# Tourval
项目实现了一个基于LBS和混合推荐算法的智能旅游导游系统。项目系统包括Android app（客户端）与服务器后台（服务端），其中客户端包括地图、导览等功能，服务端为客户端提供数据存储、查询等功能的接口，以及为用户提供兴趣计算的能力。

项目中应用的关键技术：UI/UX及可用性设计、Android编程、Kotlin开发、Android特色开发基于位置的服务、数据库架构设计、服务器Node开发。



## 主要功能

1. 查看地图
2. 显示用户地理位置
3. 搜索景点
4. 智能推荐景点
5. 实时语音导览
6. 收藏自己喜欢的景点



## 截图

![](http://cloud.ddiu.site/18-6-14/59704928.jpg)

![](http://cloud.ddiu.site/18-6-14/74724950.jpg)



## 数据库构建

关于数据库的构建，我们构建了以下关系图。

![](http://cloud.ddiu.site/18-6-14/71189005.jpg)

### 用户表 User

用户表记录了每个用户的基本信息，包括手机号码（因为系统中没有要求密码，这里没有密码的记录）。同时userFav字段记录了用户相对应用户兴趣表记录的id。

| 字段              | 描述     | 类型   |
| ----------------- | -------- | ------ |
| mobilePhoneNumber | 手机号   | String |
| userFav           | 兴趣表id | String |

### 用户兴趣表 UserFav

用户兴趣表存储用户的兴趣信息。

| 字段         | 描述               | 类型   |
| ------------ | ------------------ | ------ |
| userObjectId | 用户id             | String |
| interest     | 兴趣向量           | Array  |
| favList      | 收藏总数           | Int    |
| favAmount    | 每个分类下的收藏数 | Array  |

### 标签列表 TagInfo

标签列表存储所有标签，便于在首页调取展示。

| 字段    | 描述     | 类型   |
| ------- | -------- | ------ |
| tagId   | 标签序号 | Int    |
| tagName | 标签名称 | String |

### 分类列表 Category

分类列表存储了所有分类，用于计算兴趣向量等。

| 字段         | 描述     | 类型   |
| ------------ | -------- | ------ |
| categoryId   | 分类序号 | Int    |
| categoryName | 分类名称 | String |

### 景点列表 PlaceInfo

景点列表存储了景点信息，用于数据的展示、查询。

| 字段     | 描述         | 类型        |
| -------- | ------------ | ----------- |
| id       | 景点id       | String      |
| name     | 景点名称     | String      |
| desc     | 景点描述     | String      |
| imgSrc   | 景点图片地址 | String      |
| tags     | 景点标签     | Array       |
| location | 景点坐标     | GeoLocation |
| category | 景点分类     | Array       |

### 兴趣点列表 POIInfo

兴趣点列表存储所有兴趣点的信息。

| 字段     | 描述             | 类型        |
| -------- | ---------------- | ----------- |
| id       | 兴趣点id         | String      |
| name     | 兴趣点名称       | String      |
| desc     | 兴趣点描述       | String      |
| imgSrc   | 兴趣点图片地址   | String      |
| category | 兴趣点分类       | Array       |
| location | 兴趣点坐标       | GeoLocation |
| placeId  | 兴趣点所在景点id | Array       |

### 用户收藏日志 FavoriteLog

用户收藏日志记录所有用户的收藏信息。

| 字段         | 描述                           | 类型   |
| ------------ | ------------------------------ | ------ |
| type         | 收藏类别（1为景点，2为兴趣点） | Int    |
| favId        | 收藏id                         | String |
| user         | 用户名                         | String |
| userObjectId | 用户id                         | String |
| userFav      | 用户所在兴趣表id               | String |

### 用户搜索日志 SearchLog

用户搜索日志记录所有用户的搜索信息。

| 字段       | 描述   | 类型   |
| ---------- | ------ | ------ |
| user       | 用户名 | String |
| searchText | 搜索词 | String |
