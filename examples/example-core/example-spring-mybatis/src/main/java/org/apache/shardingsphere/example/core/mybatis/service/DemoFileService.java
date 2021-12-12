package org.apache.shardingsphere.example.core.mybatis.service;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.example.core.api.entity.DemoFile;
import org.apache.shardingsphere.example.core.api.repository.DemoFileMapper;
import org.apache.shardingsphere.example.core.api.service.ExampleService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @description:
 * @author: liuguohong
 * @create: 2021/02/22 21:11
 */
@Service
@Primary
@Slf4j
public class DemoFileService implements ExampleService {
    @Resource
    private DemoFileMapper demoFileMapper;
    /**
     * Initialize environment.
     *
     * @throws SQLException SQL exception
     */
    @Override
    public void initEnvironment() throws SQLException {
        //demoFileMapper.createTableIfNotExists();
        demoFileMapper.truncateTable();
    }

    /**
     * Clean environment.
     *
     * @throws SQLException SQL exception
     */
    @Override
    public void cleanEnvironment() throws SQLException {
        //demoFileMapper.dropTable();
    }

    @Override
    @Transactional
    public void processSuccess() throws SQLException {
        System.out.println("-------------- Process Success Begin ---------------");
        List<String> ids = insertData();
        log.info("ids:{}", ids);
        printData();
        selectByKeyTest(ids);
        selectByKeysTest(ids);
        deleteData(ids);
        printData();
        System.out.println("-------------- Process Success Finish --------------");
    }

    private void selectByKeyTest(List<String> ids) {
        System.out.println("-------------- Process selectByKeyTest Begin ---------------");
        if (ids.size() > 2) {
            String randomKey = ids.get(1);
            log.info("randomKey:{}", randomKey);
            DemoFile file = demoFileMapper.selectByFileKey(randomKey);
            log.info("random file:{}", file);
        }
        System.out.println("-------------- Process selectByKeyTest End ---------------");
    }

    private void selectByKeysTest(List<String> ids) {
        System.out.println("-------------- Process selectByKeysTest Begin ---------------");
        if (ids.size() > 3) {
            String randomKey1 = ids.get(1);
            String randomKey2 = ids.get(3);
            log.info("randomKey1:{}, randomKey2:{}", randomKey1, randomKey2);
            List<DemoFile> demoFiles = demoFileMapper.selectByFileKeys(Lists.newArrayList(randomKey1, randomKey2));
            log.info("random files:{}", demoFiles);
        }
        System.out.println("-------------- Process selectByKeysTest End ---------------");
    }

    @Override
    @Transactional
    public void processFailure() throws SQLException {
        System.out.println("-------------- Process Failure Begin ---------------");
        insertData();
        System.out.println("-------------- Process Failure Finish --------------");
        throw new RuntimeException("Exception occur for transaction test.");
    }

    private List<String> insertData() throws SQLException {
        System.out.println("---------------------------- Insert Data ----------------------------");
        List<String> result = new ArrayList<>(10);
        for (int i = 1; i <= 10; i++) {
            DemoFile file = new DemoFile();
            file.setCreateTime(new Date());
            file.setFileKey(UUID.randomUUID().toString().replaceAll("-", ""));
            file.setFileUrl(UUID.randomUUID().toString().replaceAll("-", ""));
            file.setType(i);
            demoFileMapper.insert(file);
            result.add(file.getFileKey());
        }
        return result;
    }

    private void deleteData(final List<String> ids) throws SQLException {
        System.out.println("---------------------------- Delete Data ----------------------------");
        for (String each : ids) {
            demoFileMapper.deleteByFileKey(each);
        }
    }

    @Override
    public void printData() throws SQLException {
        System.out.println("---------------------------- Print File Data -----------------------");
        for (Object each : demoFileMapper.selectAll()) {
            System.out.println(each);
        }
    }
}
