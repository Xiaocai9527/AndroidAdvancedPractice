package com.xiaokun.advance_practive.im.database.bean.msgBody;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：文件body
 *      版本  ：1.0
 * </pre>
 */
public class PdFileMsgBody extends PdMsgBody {
    //文件名
    public String fileName;
    //本地路径
    public String localUrl;
    //远程路径
    public String remoteUrl;
    //文件大小
    public long fileLength;
    //文件下载状态
    public PmDownloadStatus pmDownloadStatus;

    public static enum PmDownloadStatus {
        //
        DOWNLOADING(1, "下载中"),
        SUCCESSED(2, "下载成功"),
        FAILED(3, "下载失败"),
        PENDING(4, "延迟下载");

        public int mStatus;
        public String mDesc;

        PmDownloadStatus(int status, String desc) {
            mStatus = status;
            mDesc = desc;
        }
    }

}
