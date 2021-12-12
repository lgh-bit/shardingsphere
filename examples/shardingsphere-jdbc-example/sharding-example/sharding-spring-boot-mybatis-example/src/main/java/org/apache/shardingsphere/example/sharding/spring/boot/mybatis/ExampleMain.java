/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.example.sharding.spring.boot.mybatis;

import org.apache.shardingsphere.example.core.api.ExampleExecuteTemplate;
import org.apache.shardingsphere.example.core.api.service.ExampleService;
import org.apache.shardingsphere.example.core.mybatis.service.DemoFileService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.SQLException;

@ComponentScan("org.apache.shardingsphere.example.core.mybatis")
@MapperScan(basePackages = "org.apache.shardingsphere.example.core.mybatis.repository")
@SpringBootApplication(exclude = JtaAutoConfiguration.class)
public class ExampleMain {
    
    public static void main(final String[] args) throws SQLException {
        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(ExampleMain.class, args)) {
            ExampleService bean = applicationContext.getBean(ExampleService.class);
            if (bean instanceof DemoFileService) {
                ExampleExecuteTemplate.run(bean);
            }

        }
    }
}

/**
 * [INFO ] 2021-02-22 22:42:43,911 --main-- [org.apache.shardingsphere.driver.jdbc.core.statement.ShardingSpherePreparedStatement] ShardingSpherePreparedStatement createExecutionContext
 * [INFO ] 2021-02-22 22:42:43,911 --main-- [org.apache.shardingsphere.infra.route.DataNodeRouter] DataNodeRouter route executeRoute className:DataNodeRouter
 * [INFO ] 2021-02-22 22:42:43,916 --main-- [org.apache.shardingsphere.sharding.route.engine.ShardingRouteDecorator] ShardingRouteDecorator decorate
 * [INFO ] 2021-02-22 22:42:44,009 --main-- [org.apache.shardingsphere.sharding.route.engine.type.standard.ShardingStandardRoutingEngine] ShardingStandardRoutingEngine routeTables availableTargetTables:[demo_file_0, demo_file_1, demo_file_2, demo_file_3]
 * [INFO ] 2021-02-22 22:42:44,026 --main-- [org.apache.shardingsphere.infra.rewrite.engine.RouteSQLRewriteEngine] RouteSQLRewriteEngine rewrite result:{RouteUnit(dataSourceMapper=RouteMapper(logicName=ds, actualName=ds), tableMappers=[RouteMapper(logicName=demo_file, actualName=demo_file_2)])=org.apache.shardingsphere.infra.rewrite.engine.result.SQLRewriteUnit@445bce9a}
 * [INFO ] 2021-02-22 22:42:44,031 --main-- [org.apache.shardingsphere.driver.jdbc.core.statement.ShardingSpherePreparedStatement] ShardingSpherePreparedStatement createExecutionContext
 * [INFO ] 2021-02-22 22:42:44,031 --main-- [org.apache.shardingsphere.infra.route.DataNodeRouter] DataNodeRouter route executeRoute className:DataNodeRouter
 * [INFO ] 2021-02-22 22:42:44,031 --main-- [org.apache.shardingsphere.sharding.route.engine.ShardingRouteDecorator] ShardingRouteDecorator decorate
 * [INFO ] 2021-02-22 22:42:44,031 --main-- [org.apache.shardingsphere.sharding.route.engine.type.standard.ShardingStandardRoutingEngine] ShardingStandardRoutingEngine routeTables availableTargetTables:[demo_file_0, demo_file_1, demo_file_2, demo_file_3]
 * [INFO ] 2021-02-22 22:42:44,032 --main-- [org.apache.shardingsphere.infra.rewrite.engine.RouteSQLRewriteEngine] RouteSQLRewriteEngine rewrite result:{RouteUnit(dataSourceMapper=RouteMapper(logicName=ds, actualName=ds), tableMappers=[RouteMapper(logicName=demo_file, actualName=demo_file_2)])=org.apache.shardingsphere.infra.rewrite.engine.result.SQLRewriteUnit@72557746}
 */
