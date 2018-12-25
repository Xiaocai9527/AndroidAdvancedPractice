package com.xiaokun.baselib.rx.upload;

import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/12/25
 *      描述  ：okhttputils zhanghongyang
 *      版本  ：1.0
 * </pre>
 */
public class ProgressRequestBody extends RequestBody {

    protected RequestBody mRequestBody;
    protected Listener mListener;
    protected CountingSink mCountingSink;

    public ProgressRequestBody(RequestBody requestBody, Listener listener) {
        mRequestBody = requestBody;
        mListener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        try {
            return mRequestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        mCountingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(mCountingSink);
        mRequestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink {
        private long bytesWritten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            bytesWritten += byteCount;
            mListener.onProgress((int) (100F * bytesWritten / contentLength()));
        }
    }

    public interface Listener {
        void onProgress(int progress);
    }
}
