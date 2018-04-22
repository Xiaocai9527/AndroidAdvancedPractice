# App网络请求实战三：下载进度以及断点续载

**小老板，多捞哦。**

## 原来的配方，无图言diao

![](..\pictures\\http_engi4.gif)



## 需求分析以及解题步骤

貌似rxjava和retrofit配合无法像下面这样同步返回：

```
Response<BaseResponse<ResEntity1.DataBean>> tokenRes = call.execute();
```

(我实在不知道怎么用rxjava做出这样的，有木有大佬知道的哦)

而异步回调又做不到既能提供下载进度，又能暂停下载，取消下载。所以必须得想另一个方法。提供一种okhttpi下载的代码(引自郭霖第一行代码)：

```
try
{
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
            .addHeader("RANGE", "bytes=" + downloadedLength + "-")
            .url(downloadUrl)
            .build();
    Response response = client.newCall(request).execute();
    if (response != null)
    {
        Log.e("DownLoadTask", "(DownLoadTask.java:78)" + response.body().contentLength());
        is = response.body().byteStream();
        savedFile = new RandomAccessFile(file, "rw");
        savedFile.seek(downloadedLength);
        byte[] bytes = new byte[1024];
        int total = 0;
        int len;
        while ((len = is.read(bytes)) != -1)
        {
            if (isCanceled)
            {
                return TYPE_CANCELED;
            } else if (isPaused)
            {
                return TYPE_PAUSE;
            } else
            {
                total += len;
                savedFile.write(bytes, 0, len);
                int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                publishProgress(progress);
            }
        }
    }
    response.body().close();
    return TYPE_SUCCESS;
} catch (IOException e)
{
    e.printStackTrace();
} finally
{
    try
    {
        if (is != null)
        {
            is.close();
        }
        if (savedFile != null)
        {
            savedFile.close();
        }
        if (isCanceled && file != null)
        {
            file.delete();
        }
    } catch (IOException e)
    {
        e.printStackTrace();
    }
}
```

其实这里涉及到两个问题：

* 下载进度
* 断点继续下载

### 下载进度

关键在于自定义ResponseBody，注意里面的source方法，我们可以在这里面对source进行提前操作，感觉跟拦截器的思想很像。

首先定义一个下载接口：

```
/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/20
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */
public interface DownLoadListener
{
    void onProgress(int progress, boolean downSuc, boolean downFailed);
}
```

3个参数分别是下载进度，是否下载完成，是否下载失败;

其次看下自定的ResponseBody代码如下：

```
@Override
public BufferedSource source()
{
    if (bufferedSource == null)
    {
        bufferedSource = Okio.buffer(source(mResponseBody.source()));
    }
    return bufferedSource;
}

private Source source(Source source)
{
    return new ForwardingSource(source)
    {
        @Override
        public long read(Buffer sink, long byteCount) throws IOException
        {
            long bytesRead = super.read(sink, byteCount);
            totalBytesRead += bytesRead != -1 ? bytesRead : 0;
            if (bytesRead == -1)
            {
            	//下载完成
                sHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mListener.onProgress(100, true, false);
                    }
                });
            } else
            {
            	//正在下载中，更新进度
                sHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int progress = (int) (totalBytesRead * 100 / contentLength);
                        mListener.onProgress(progress, false, false);
                    }
                });
            }
            saveToFile(sink);
            return bytesRead;
        }
    };
}

//写入文件
private void saveToFile(Buffer buffer)
    {
        InputStream inputStream = buffer.inputStream();
        RandomAccessFile saveFile = null;
        try
        {
            saveFile = new RandomAccessFile(file, "rw");
            saveFile.seek(getDownloadedLength());
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1)
            {
                saveFile.write(bytes, 0, len);
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
                if (saveFile != null)
                {
                    saveFile.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
```

就是那个ForwardingSource类，真的感觉这个东西有点像rxjava中的拦截器，就是把那个source拦下来，然后就可以做一些不可描述的事情，嘿嘿嘿。这里为了后面的断点续载功能，用到了RandomAccessFile。其实也是可以用FileOutputStream的两个参数的构造函数，如下

```
//if <code>true</code>, then bytes will be written to the end of the file rather than the //beginning
public FileOutputStream(String name, boolean append)
    throws FileNotFoundException
{
    this(name != null ? new File(name) : null, append);
}
```

第二个参数true也是可以继续写入的。总体来说下载进度还是比较简单的。

### 断点续载

其实关键就是，玛德文字说不清楚。看图：

![](..\pictures\duandianxiazai.png)

Http请求头中有一个属性如下：

```
request = request.newBuilder()
        .header("RANGE", "bytes=" + downloadedLength + "-")
        .build();
```

这个属性的作用就是可以更改请求域，比如说我想从第500个字节处开始下载。那么如何动态修改它呢，没错还是用到Interceptor拦截器：

```
//下载文件拦截器
static Interceptor downloadInterceptor = new Interceptor()
{
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        //记录已写入的文件的长度
        long downloadedLength = DownloadManager.dSp.getLong(downloadEntity.getFileName(), 0);

        Response proceed = chain.proceed(request);
        //保存源文件的总长度(关键)
        DownloadManager.dSp.edit().putLong(downloadEntity.getFileName() + "content_length", proceed.body().contentLength()).commit();
        request = request.newBuilder()
                .header("RANGE", "bytes=" + downloadedLength + "-")
                .build();
        Response response = chain.proceed(request);
        response = response.newBuilder()
                .body(new ProgressResponseBody(response.body(), downloadEntity))
                .build();
        return response;
    }
};
```

所有进度progress = (已写入文件的长度+正在下载的长度)/源文件的总长度，代码再项目中有我就不贴了。还有一个要注意就是暂停下载和取消下载，我利用的是rxjava中的Disposable，我特意写了一个管理类如下：

```
/**
 * Created by 肖坤 on 2018/4/22.
 *
 * @author 肖坤
 * @date 2018/4/22
 */

public class DownloadManager
{
    public static SharedPreferences dSp;

    /**
     * 初始化DownloadManager
     *
     * @param context
     */
    public static void initDownManager(Context context)
    {
        dSp = context.getSharedPreferences("download_file", Context.MODE_PRIVATE);
    }

    /**
     * 暂停下载
     *
     * @param disposable 控制rxjava的开关
     * @param fileName   下载的文件名,必须包含后缀
     */
    public static void pauseDownload(Disposable disposable, String fileName)
    {
        if (disposable == null || TextUtils.isEmpty(fileName))
        {
            return;
        }
        if (!disposable.isDisposed())
        {
            disposable.dispose();
        }
        if (dSp == null)
        {
            throw new NullPointerException("必须首先初始化DownloadManager");
        }
        File file = initFile(fileName);
        if (file.exists() && dSp != null)
        {
            dSp.edit().putLong(file.getName(), file.length()).commit();
        }
    }

    /**
     * 取消下载
     *
     * @param disposable 控制rxjava的开关
     * @param fileName   下载的文件名,必须包含后缀
     */
    public static void cancelDownload(Disposable disposable, String fileName)
    {
        if (disposable == null || TextUtils.isEmpty(fileName))
        {
            return;
        }
        if (!disposable.isDisposed())
        {
            disposable.dispose();
        }
        File file = initFile(fileName);
        if (file.exists() && dSp != null)
        {
            dSp.edit().putLong(file.getName(), 0).commit();
        }
        //取消下载，最后一步记得删除掉已经下载的文件
        if (file.exists())
        {
            file.delete();
        }
    }

    public static File initFile(String fileName)
    {
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(directory + File.separator + fileName);
        return file;
    }
}
```

注意哦，我在取消下载的时候删除了写入的文件，没毛病。

github链接：<a href="https://github.com/xiaokun19931126/HttpExceptionDemo">Demo</a>

**以上。**



