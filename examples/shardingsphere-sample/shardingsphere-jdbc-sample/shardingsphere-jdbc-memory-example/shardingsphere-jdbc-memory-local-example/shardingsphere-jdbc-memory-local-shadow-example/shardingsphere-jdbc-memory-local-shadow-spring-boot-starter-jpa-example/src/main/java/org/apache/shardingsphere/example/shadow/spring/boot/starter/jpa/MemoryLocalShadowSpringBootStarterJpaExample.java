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

package org.apache.shardingsphere.example.shadow.spring.boot.starter.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import java.sql.SQLException;

@EntityScan(basePackages = "org.apache.shardingsphere.example.shadow.spring.boot.starter.jpa.entity")
@SpringBootApplication
public class MemoryLocalShadowSpringBootStarterJpaExample {
    
    public static void main(final String[] args) throws SQLException {
        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(MemoryLocalShadowSpringBootStarterJpaExample.class, args)) {
            MemoryLocalShadowSpringBootStarterJpaExampleService exampleService = applicationContext.getBean(MemoryLocalShadowSpringBootStarterJpaExampleService.class);
            exampleService.run();
        }
    }
}
