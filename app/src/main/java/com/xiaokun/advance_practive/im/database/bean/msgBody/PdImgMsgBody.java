package com.xiaokun.advance_practive.im.database.bean.msgBody;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/18
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class PdImgMsgBody extends PdFileMsgBody {
    //缩略图远程url
    public String thumbnailRemoteUrl;
    //缩略图本地url
    public String thumbnailLocalUrl;
    //缩略图下载状态
    public PmDownloadStatus thumbnailDownloadStatus;

}
