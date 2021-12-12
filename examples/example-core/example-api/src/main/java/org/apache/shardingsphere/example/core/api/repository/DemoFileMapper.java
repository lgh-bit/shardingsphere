package org.apache.shardingsphere.example.core.api.repository;


import org.apache.shardingsphere.example.core.api.entity.DemoFile;

import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: liuguohong
 * @create: 2021/02/22 21:06
 */
public interface DemoFileMapper extends CommonRepository<DemoFile, Long> {
    int deleteByFileKey(String fileKey);

    DemoFile selectByFileKey(String fileKey);

    List<DemoFile> selectByFileKeys(Collection<String> list);
}
