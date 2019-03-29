# wlmedia
android 音视频播放SDK，几句代码即可实现音视频播放功能~

## 1、Usage

### Gradle: [ ![Download](https://api.bintray.com/packages/ywl5320/maven/wlmedia/images/download.svg?version=1.0.0-beta16) ](https://bintray.com/ywl5320/maven/wlmedia/1.0.0-beta16/link)

	implementation 'ywl.ywl5320:wlmedia:1.0.0-beta16'


## 2、实例图片

### 播放视频
<img width="360" height="640" src="https://github.com/wanliyang1990/wlmedia/blob/master/img/4.gif"/>

### 播放音乐
<img width="360" height="640" src="https://github.com/wanliyang1990/wlmedia/blob/master/img/3.png"/>

### 竖屏播放（自动保持宽高比）
<img width="360" height="640" src="https://github.com/wanliyang1990/wlmedia/blob/master/img/1.png"/>

### 横屏播放（自动保持宽高比）
<img width="640" height="360" src="https://github.com/wanliyang1990/wlmedia/blob/master/img/2.png"/>

## 3、调用方式

### 配置NDK编译平台:

	defaultConfig {
		...
		ndk {
		    abiFilter("arm64-v8a")
		    abiFilter("armeabi-v7a")
		    abiFilter("x86")
		    abiFilter("x86_64")
		}
		...
	}
	
### 基本权限

	<uses-permission android:name="android.permission.INTERNET"/>
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

### 接入代码

	// WlSurfaceView 一般播放使用
	<com.ywl5320.wlmedia.widget.WlSurfaceView
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
    
	// WlTextureView 需要做透明、移动、旋转等使用
    <com.ywl5320.wlmedia.widget.WlTextureView
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
	
	WlMedia wlMedia = new WlMedia();// 可支持多实例播放（主要对于音频，视频实际验证效果不佳）
	wlMedia.setPlayModel(WlPlayModel.PLAYMODEL_AUDIO_VIDEO);//声音视频都播放
	wlMedia.setCodecType(WlCodecType.CODEC_MEDIACODEC);//优先使用硬解码
	wlMedia.setMute(WlMute.MUTE_CENTER);//立体声
	wlMedia.setVolume(80);//80%音量
	wlMedia.setPlayPitch(1.0f);//正常速度
	wlMedia.setPlaySpeed(1.0f);//正常音调
	wlMedia.setRtspTimeOut(30);//网络流超时时间
	wlMedia.setShowPcmData(true);//回调返回音频pcm数据
	wlSurfaceView.setWlMedia(wlMedia);//给视频surface设置播放器
	
	//异步准备完成后开始播放
	wlMedia.setOnPreparedListener(new WlOnPreparedListener() {
            @Override
            public void onPrepared() {
	    	// wlMedia.setVideoScale(WlScaleType.SCALE_16_9);//设置16:9的视频比例
                wlMedia.start();//开始播放
                double duration = wlMedia.getDuration();//获取时长
            }
        });
	
	//设置url源
	wlMedia.setSource("/storage/sdcard1/精灵宝可梦：就决定是你了.720p.国日粤三语.BD中字[最新电影www.66ys.tv].mp4");
	wlMedia.prepared();//异步准备
	
## 4、其他API 可看类：WlMedia.java

## [我的视频课程（基础）：《（NDK）FFmpeg打造Android万能音频播放器》](https://edu.csdn.net/course/detail/6842)
## [我的视频课程（进阶）：《（NDK）FFmpeg打造Android视频播放器》](https://edu.csdn.net/course/detail/8036)
## [我的视频课程（编码直播推流）：《Android视频编码和直播推流》](https://edu.csdn.net/course/detail/8942)

##### create By：ywl5320 2019-01-01





