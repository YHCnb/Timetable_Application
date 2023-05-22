# Timetable_Application

0.序
Github
调查
市面上已经存在着很多商业化课程表应用软件，由于选择课题时并没有特别好的思路，就想着不妨对已有的课表软件进行一个拙劣的模仿，毕竟假如能做到和成熟的软件有几分相似已经很不错了hhh。

不过随着对众多同类型产品进行调研后，我发现绝大多数课表软件都无法实现课表从学校教务直接导入课表的功能，大多数都是只支持Excel等文件形式导入，或者干脆只支持一个一个手动添加课程。

其实导致这个结果的原因也很容易理解——全国大学数量如此之多，使用的教务系统也不尽相同，要想适配所有的这些教务系统，并从中解析获得对应的课表信息，其工作量可想而知（极其恐怖），其付出与回报也不成正比。

不过我也发现了两款十分优秀的APP，“超级课程表”和“WakeUp课程表”，这两款课表都做到了对大部分学校教务系统的适配支持。但当我在“超级课程表”尝试导入北京理工大学课表时，发现会转到一个我们学生平时都不会使用的“智慧北理”页面，打开课表将会发现页面不兼容，根本导入无法咱学校的课表（悲）。
 ![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/05e4e406-b471-4efd-985b-6d517f795c13)

如此一番调研下来，我惊讶的发现，能够为我们北理工学生方便使用的课程表APP市面上可谓是凤毛麟角（强烈推荐“WakeUp课程表”！！！）。这就体现了个人开发APP的一个优势，只需要完成对咱自己的学校教务系统进行适配即可。但即便如此，在实际对学校网站进行信息抓取操作中，仍困难重重。这里要特别感谢同届的范大佬的无私帮助，大佬探索并总结了对学校官网API进行访问与信息获取的一整套流程，并应用在其主导开发的网站BIT101中（在此向大佬膜拜Orz）。

名字
然后聊聊软件的名字，本来是随便取了一个名字叫Class Timetable（现在才反应过来这名字是典型的中文直译错误）。后来觉得太过普通，于是突发奇想加了个y，改成Classy Timetable，突然就感觉高级了起来，私以为很不错。

logo
最后再扯一下应用的logo，一个有趣的事实是，这个logo从想法到图片都是由New Bing产生的。提问：“提到课表，你会想到什么动物”，回答：“我会想到一只猫，因为他们很喜欢按照自己的规律生活，不收别人的约束。而且，猫咪也很聪明，会找到自己感兴趣的事情去做。”于是我就让它帮我设计一个猫咪logo，不久就得到了满意的图片，便有了咱APP的logo，算是一段十分有趣的体验。
  ![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/d6eee131-53db-47f7-9383-e57d557af1e5)



1. 运行与开发环境
（一）版本控制：compileSdk=33，minSdk=29
（二）运行环境：10.0以上版本Android的Android手机/平板
（三）部署方法：直接安装ClassyTimetable.apk即可
（四）开发环境：Android Studio Electric Eel | 2022.1 + compileSdkSdk33/minSdk29 + Pixel2 API 32




2.项目细节一览
代码量统计
在Android studio中使用Statistic 插件进行统计，.kt文件46个，共4583行，其中code lines共4241行。还有一个.js脚本文件用于密码加密。

开发环境及依赖
开发环境：Android Studio Electric Eel | 2022.1 + compileSdkSdk33/minSdk29 + Pixel2 API 32

主要开发框架为利用Jetpack Compose + Material3，采用单Activity+多composable作为主要模式。

第三方组件：
Jsoup：用于解析从网站返回的json字符串；
numberpicker、colorpicker：用于快速创建美观的选择器；
sheets-compose-dialogs：用于日期选择框等组件；
js-evaluator：用于在kotlin中执行.js函数，完成密码加密；
cookie-store：用于管理cookie；
Splashscreen：用于显示启动画面。


3. 功能介绍
有什么优势？ 
清新简洁的外观 
简单直观的交互体验，避免繁杂的操作
支持高度自由的定义，打造独属于你的时间规划表！
实现BIT官网、ICS文件一键导入课表，1s效率神器


各主要板块功能介绍
整体风格
应用的整体色彩体系利用Android的Material Theme，用绿色作为主题色（养眼），整体偏浅色调。日程表存在的意义就是给予清晰的规划，所以此软件UI设计与排版的宗旨就是简洁与直观，在给足信息量的同时不显得凌乱，尽量提供一个良好的用户体验。



主页
主页呈现的就是课程表本身，初始化界面显示的是当前周的课表，你可以轻松地通过左右滑动切换到其他周的课表。可以通过点击查看对应课程的详细信息卡片，卡片中的编辑按钮将跳转到对应课程的信息管理页面。
主页面的上方显示当前日期与页面对应周数，如为课程表当前周将会显示“（本周）”字样。右上角三个按钮分别对应：添加课程、导入课程、设置栏。以下将会分别介绍。
![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/3685553b-c2d4-4f2b-a875-8bc5f00c09ab)









设置栏
设置栏以侧边菜单的形式显示，在设置栏中你可以对应用的信息进行全局的管理，这里分为3个基本部分：全局设置、基本设置、关于。
全局设置包括：课表管理，将跳转到课表管理页面，对所有已有课表进行查看与编辑；课程管理，将跳转到当前默认课表对应的课程管理界面；时间表（尚未完善），将跳转到编辑时间表页面，对不同节数的时间进行自定义调整。
基本设置主要是对当前课表的一些变量进行快捷地调整，可以设置学期开始时间、当前周、课程节数、学期周数。值得一提的是，在对应的选择框显示的同时，设置栏将会收起，而当用户取消或者dismiss时，设置栏又会自动打开，从而保证良好的用户体验。
关于将跳转到关于页面，可以看到应用信息以及我对应用的一些想法。
![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/d34e7128-d316-4bc7-8efc-787b9d9a66e4)

添加课程|课程编辑
这个页面既可以用于添加新课程，也可以用于编辑已有课程的信息。可以编辑课程名字，对应卡片颜色，其中颜色支持RGB调节，也可从预置的备选颜色中选择。下方为课程上课的时间段，每个时间段都包括周数、上课时间、老师、上课地点4个属性。你可以通过屏幕右下角按钮添加时间段，时间段右上角按钮删除对应时间段。
![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/0adbf6a7-850b-48bd-bebf-feee8007c8c9)

课程管理
呈现课表中所有的课程，点击对应的卡片即可进入对应的课程编辑页面，长按对应卡片将会删除对应课程。右下角按钮点击可以添加新的课程。
![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/929e5d2b-28f0-4a99-909a-9b63926ae753)


课表管理
呈现现有的所有的课表，点击对应的卡片即可进入对应的课表管理页面，长按对应卡片将会删除对应课表（默认课表无法删除）。右下角按钮点击可以添加新的课表。
课表卡片右下角有两个按钮，第一个爱心为设置默认课表，默认课表的爱心将是实心的；第二个按钮将进入课表数据页面，可以在此对课表基本信息进行编辑。
![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/0ce97da9-1b96-42dd-8625-686475f0e954) ![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/a396a81c-0c81-4001-b892-9d5acc8b95f1)
         
课表导入
课表导入目前支持两种形式：BIT导入与ICS导入。其中BIT导入将通过登陆学校统一身份认证，获取当前学期的课表；ICS导入目前只支持解析BIT101.cn（范大神的网站）中导出的.ics格式文件。
在BIT导入页面，仅需输入正确的学号与密码，即可一键获取当前学期的课表。正确登陆后一段时间内无需再次登录，可直接获取课表，也可选择退出登陆。
![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/71ea35b2-c3b0-409f-8ded-dea65d8045f8)  ![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/245c7ead-f610-4c5e-8abb-cab5f0c09918)


          
关于|About
呈现一些想法与相关链接。
![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/f77fd1e7-9e9d-428b-8d62-32c555673d8e)

4. 架构设计与技术实现
整体架构
整个安卓项目采用单activity+多compose的思路，主体框架为Jetpack Compose+Matreial3。同时在compose中集成协程与ViewModel,，实现MVVM的设计模式，使得对于课程表信息的呈现与修改的实现变得十分便捷。


数据库
数据的存储采用Room数据库，并通过.createFromAsset("default_data.db")初始化为默认数据库。数据库中有三张表：Timetable、Settings、OneClassTime，分别对应课表、应用设置（默认课表名）、时间表。

初始化时，ViewModel将通过从Settings中获取默认课表名，然后从Timetable中获取对应课表数据用于外部调用。


导航
不同compose之间通过navController进行导航，主要有8种不同的导航页面，其中课表编辑、课程编辑、课程管理由于需要针对不同的目标呈现不同的页面，采用加上后缀名进行定向导航。

如课程编辑页面将通过"CourseEditor/{courseName}"进行导航，将通过从vm中取出对应课程传入compose中实现。如果遇到添加新课程的情况，将courseName传入为“_”作为特殊标记。


Dialogs/Pickers
为了提升与用户的交互性，此应用中会用到了很多的提示框、选择器，且功能不尽相同。这些UI实现思路类似，但重复编写不仅耗费精力，而且会增加很多没营养的代码，让整体的code显得十分得杂乱。我起初尝试寻找是否存在普世的选择，但很快发现并不能满足我个性化的需求。

于是我选择自己开发组件，为不同的使用场景编写了共11种dialogs和pickers模板，包括但不限于SaveOrLeaveDialog、DrawerNumberPicker等，它们可以轻松得通过几行代码进行重复调用。让整体代码更加清晰简洁。




BIT课表获取
这一部分对于学校官网的登录与信息获取的流程思路由范力文范同学提供。

首先为了信息的安全，需要将用户输入的密码通过特殊的加密脚本加密。其中这里的加密脚本是.js文件，而kotlin中是无法直接使用.js文件中的函数的，对此需要使用额外使用.js文件编译的依赖库来执行内部的函数完成加密。

之后通过将用户名、加密后的密码和其他信息组合构建为一个请求，并通过okhttp3发送到学校登录网址，验证通过即登录成功。之后接连进行一系列请求操作：课表初始化、课表语言设置、获取当前学期、课表数据获取、课表获取。即可得到对应课表的WebResponse。

这里存在一个细节，就是是否连接校园网，需要进行请求的网站是不同的。对此需要在获取连接的时候先验证一下当前设备是否支持连接校园网，再根据结果返回不同的网站，保证请求操作在不同网络下都能够实现。


解析导入
课表导入分为两种方式，网站提取与文件导入，但都需要对获取到的信息进行解析，转为符合要求的组织形式。对此分别写了parseICS和parselWebResponse两个解析函数，提取信息返回Timetable对象。

其中比较坑的一个点是对于体育课，导入的信息中课程名会加特殊的前缀“体育/”，这里的反斜杠刚好会让软件在导航的时候与"CourseEditor/{courseName}"中的反斜杠冲突（多一个反斜杠），导致程序崩溃。对此需要特地将反斜杠处理为竖线。（悲）


5. 问题及其解决方案
Material3迁移
此项目在创建的开始搭载的是Material2，但后来发现Material3的UI貌似更加得美观，而且添加了很多有用的组件。所以我遇到的第一个大的问题就是如何将项目从Material迁移到Material3中。

这里我主要参考了安卓developers的官方迁移指南：从 Material 2 迁移至Material 3

主要区别就是：某些M3 API被视为实验性API，需要添加注解；MaterialTheme的使用发生改变；一些组件弃用，大部分组件改名并发生微调。这里我迁移后就顺便把ColorScheme给设置好了（学习油管的视频），配置过程有点枯燥。但是配置之后你会发现应用的颜色终于不是那个单调难看的紫色了（不是），而且调用十分方便，有种豁然开朗的感觉。


应用初始化
问题就是为了提升效率，ViewModel的初始化是通过协程进行的，但是我的应用的主页课表的显示就直接需要用到内部的数据。这就导致ViewModel内的数据还没初始化就已经执行到UI部分，导致应用得到空值闪退。

这个问题我一开始还没发现，因为在初期编写调试阶段并没有把课表作为主页，而是另一个不相关的页面，转到课表页面的时候已经完成了初始化。所以问题一直没有暴露出来。

为了解决这个问题，首先的想法就是使用应用初始化界面，给ViewModel初始化缓冲的余地。一开始是想调用库，正好发现安卓官网新推出了splashScreen这个组件，可以通过创建一个简单的xml文件，轻松的在应用开始前短暂得显示初始界面。但是很快发现这并不会阻止程序继续执行到UI部分，仍然会闪退。

后来是通过delay 1s来改变一个可变状态值，用这个bool值来决定应用是显示初始化界面还是课程表主页。实现了显示1s自定义的splashScreen，并且保证ViewModel初始化完毕。


显示UI不改变
此应用中有许多支持用户编辑了信息的地方，一般情况下，对应的UI只需通过与ViewModel中的数据进行双向绑定就可以实现实时改变。但是也会出现一些特殊的情况无法解决，下面将列出其中两个。

第一个就是在进行课表导入的时候，对于新同名覆盖的课表，导航将不会进行changeTimetable操作（用于将数据更新为当前默认课表），导致显示的还是原课表数据。就算执行change操作也会因为并没有执行完就已经转到对应编辑页面而显示错误。最终使用runBlocknig通过阻塞保证UI显示正确，虽然牺牲了一点性能，但最后应用起来很丝滑，并没有卡顿的感觉。
![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/32bed896-f332-4b31-8e65-99fd975eb8a6)

第二个是主页显示的周数可能不正确。在切换默认课表或者导入全新课表后，主页显示的周数将仍是上个课表的周数（虽然课表内容改变了，但是周数没对应上了，不是当前周）。这是由于对与课表不同周数的滑动，是通过rememberPagerState这个特殊的状态进行控制。而这个值只会初始化一次，并不会因为vm中curweek的改变而改变。于是我通过LaunchedEffect将currenWeek与curWeek绑定起来，每次curWeek改变currenWeek就需要滑动到对应周的页面，完成UI的正确显示。
![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/f9443bfe-c77a-4b82-8490-043c38d278f9)


解析错误
对于导入信息的解析存在许多小坑，都不复杂，但是会让人很drama。

对于体育课，导入的信息中课程名会加特殊的前缀“体育/”，刚好会与导航的时候"CourseEditor/{courseName}"中的反斜杠冲突（多一个反斜杠），导致无法正常导航。对此需要特地将反斜杠处理为竖线。还有ICS导入的时候，周数信息可能会有“1周”、“1-3周”、“1周，3-8周”等不同的格式，都需要考虑，否则无法正常解析（悲）。


failed lock verification
这提示其实就是compose相关的依赖版本没及时更新，更新为最新版本即可。
![image](https://github.com/YHCnb/Timetable_Application/assets/112797916/fae8458a-1c50-4d05-942d-b554329dca76)


数据库版本迁移
Room数据库十分好用，但是当你一开始考虑的并不全面，需要改变数据库的组成时（添加新表），直接添加新表运行就会直接报错崩溃。因为不同版本间的数据库是无法直接互通的，需要编写迁移代码才能转到新版本的数据库。

这里我选择直接偷懒（不是），我把原数据库删除，直接重新安装，并执行一些代码直接手动创造一个符合要求的db。并把这个db的文件保存下来，作为应用初始化时使用的默认db，完成迁移。


应用旋转错误
之前设置了一个初始化界面，导致应用旋转后再旋转回来将会一直停在初始化界面（不知道是什么奇怪的原因）。考虑到这是一个课程表应用，并没有横屏的需求，旋转还会影响观感，所以觉得直接锁定不支持旋转。

具体操作只需要在manifest文件找到主activity，并加入android:screenOrientation="portrait"这行命令即可锁定应用旋转。


6.开发过程 
2023.4.7 完成应用调研
2023.4.8 设计一些重要的实体
2023.4.10 构建应用基本框架
2023.4.12 基本完成主页UI
2023.4.13 添加room数据库
2023.4.18 构建MVVM架构
2023.4.21 添加课程编辑页面
2023.4.23 添加课程管理页面
2023.4.24 添加课表管理页面
2023.5.8 添加主页侧边栏设置
2023.4.13 增加时间表编辑功能
2023.4.14 完成ICS导入功能
2023.4.16 完成登录页面
2023.4.17 完成BIT导入功能
2023.4.19 增加关于页面，完善细节
2023.4.20 发布测试
2023.4.21 制作介绍视频
可以发现中间有很长一段时间搁置了开发，那是因为跑去赶知识工程大作业了（难绷）。


7.后记
这次安卓开发过程的体验还是很不错的。有了上学期JAVA的前车之鉴，我早早地就开始思考最后的应用应该开发什么了。再加上这学期的课程安排与上学期比轻松了不少，让我拥有了相对充裕的时间去一步步开发我的应用，最后也确实得到了一个功能较为完善的APP，令人满意。

与上学期JAVA开发不同的感受就是，网络上关于安卓的资源与教程实在太丰富了，你可以十分轻松得获取几乎任何你想要的组件库。即便是最新的compose技术，或者material3，你也可以在油管上找到一堆教学视频，对于初学者十分友好。安卓这样丰富与活跃的社区生态，真的给予了我们很大的自由度，让开发过程十分愉悦。

还有就是特别感谢范力文范同学在网络通信技术上的支持Orz。这个应用的初衷就是想要能够连接学校网站，将学校课表导入到手机APP上。而由于对网络通信并无基础，让开发进度一度停滞。后来偶然通过github了解到大神开发了BIT1010这个网站，并探索出了对学校不同API的访问流程。经过交流，大神很热心地就给予了相关的帮助。这个过程，不仅让我惊叹于别人在开发那么酷的东西，也让我看到了大神热情与分享的精神。再次表达感谢！

总的来说，一次有趣的开发体验。