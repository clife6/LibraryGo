# LibraryGo 图书借阅管理系统

LibraryGo 是一个使用 Kotlin 和 Jetpack Compose 开发的 Android 图书借阅应用，作为移动应用开发综合实训的课程设计项目，提供用户登录注册、图书检索、收藏、借阅归还及记录查询等功能。

本仓库包含 Android 客户端。原课设后端使用 Flask + SQLite，客户端通过 HTTP 接口访问业务数据；运行完整功能需要另行部署配套后端。

## 主要功能

- **用户功能**：注册、登录、找回密码、查看个人信息及更换头像。
- **图书浏览**：首页轮播、随机图书展示，按书名或作者搜索图书，查看图书详情。
- **借阅管理**：借阅与归还图书，查看当前书架、借阅日期、应还日期和历史记录。
- **收藏管理**：收藏、取消收藏及查看收藏列表。
- **借阅提醒**：通过 Service、AlarmManager 和 BroadcastReceiver 查询当前借阅数量并展示本地通知。

## 技术栈

| 类别 | 技术与用途 |
| --- | --- |
| 开发语言 | Kotlin |
| 界面 | Jetpack Compose、Material 3 |
| 页面导航 | Navigation Compose，管理路由与参数传递 |
| 状态管理 | ViewModel、StateFlow，管理表单与页面数据 |
| 异步与事件 | Kotlin Coroutines、SharedFlow，处理网络请求与导航事件 |
| 网络通信 | Retrofit、OkHttp、Gson，定义接口并解析响应 |

## 项目结构

客户端按 UI、ViewModel 和网络访问划分职责。页面观察状态并响应用户操作，ViewModel 处理业务交互，通过 `RetrofitClient` 调用后端接口。

以下目录位于 `app/src/main/java/com/clife/librarygo/`：

| 路径 | 说明 |
| --- | --- |
| `MainActivity.kt` | 应用入口 |
| `ui/screen/`、`ui/theme/` | 页面与主题样式 |
| `navigation/` | 导航图、路由及页面参数 |
| `viewmodel/` | 表单、列表、借阅等页面状态与逻辑 |
| `data/` | Retrofit 配置、接口定义、请求与响应模型 |
| `entity/` | 用户、图书、收藏与借阅记录等数据模型 |
| `service/`、`broadcastreceiver/` | 借阅检查与本地通知 |



## 项目说明

- 借阅提醒目前展示当前借阅数量，逾期处理与后台提醒可靠性仍有待完善。
- 异常处理、多个请求的状态协调及自动化测试仍有改进空间。
