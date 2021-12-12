package org.apache.shardingsphere.example.core.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: liuguohong
 * @create: 2021/02/22 21:08
 */

public class DemoFile implements Serializable {
    private static final long serialVersionUID = 661434701950670676L;
    private Long id;
    private String fileKey;
    private String fileUrl;
    private Integer type;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "DemoFile{" +
                "id=" + id +
                ", fileKey='" + fileKey + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", type=" + type +
                ", createTime=" + createTime +
                '}';
    }
}
