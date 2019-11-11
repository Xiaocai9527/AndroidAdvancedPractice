# Android音视频深入学习

## 1.TextureView播放视频







## 2.camera预览视频方法

> 这里介绍的是Camera的用法，谷歌在Android 5.0提供了一个新的Camera2 API

**开启camera进行视频预览必须提供一个surface，而提供surface有两种方式。**

```java
/**
 * Starts capturing and drawing preview frames to the screen.
 * Preview will not actually start until a surface is supplied
 * with {@link #setPreviewDisplay(SurfaceHolder)} or
 * {@link #setPreviewTexture(SurfaceTexture)}.
 *
 * <p>If {@link #setPreviewCallback(Camera.PreviewCallback)},
 * {@link #setOneShotPreviewCallback(Camera.PreviewCallback)}, or
 * {@link #setPreviewCallbackWithBuffer(Camera.PreviewCallback)} were
 * called, {@link Camera.PreviewCallback#onPreviewFrame(byte[], Camera)}
 * will be called when preview data becomes available.
 *
 * @throws RuntimeException if starting preview fails; usually this would be
 *    because of a hardware or other low-level error, or because release()
 *    has been called on this Camera instance.
 */
public native final void startPreview();
```

### a.使用camera.setPreviewDisplay(holder)

```java
//打开camera
Camera mCamera = Camera.open();
//配置camera，但是已经被废弃。谷歌推荐使用Camera2的API
Camera.Parameters param = mCamera.getParameters();
//设置格式
param.setPreviewFormat(format);
//设置帧率
param.setPreviewFramRate(framRate);
//设置宽高
para.setPreviewSize(width,height);
//设置给camera
mCamera.setParameters(param);

SurfaceHolder holder = surfaceView.getHolder();
mCamera.setPreviewDisplay(holder);

//开启预览
mCamera.startPreview();
```

## 

### b.使用camera.setPreviewTexture(surfaceTexture)

