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

package org.apache.shardingsphere.scaling.core.executor.engine;

import org.apache.shardingsphere.data.pipeline.core.util.ThreadUtil;
import org.apache.shardingsphere.schedule.core.executor.LifecycleExecutor;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public final class ExecuteEngineTest {
    
    @Test
    public void assertSubmitMoreThanMaxWorkerNumber() {
        ExecuteEngine executeEngine = ExecuteEngine.newFixedThreadInstance(2);
        try {
            for (int i = 0; i < 5; i++) {
                Future<?> submit = executeEngine.submit(mockScalingExecutor());
                assertFalse(submit.isCancelled());
            }
        } catch (final RejectedExecutionException ex) {
            fail();
        }
    }
    
    @Test
    public void assertSubmitAllMoreThanMaxWorkerNumber() {
        ExecuteEngine executeEngine = ExecuteEngine.newFixedThreadInstance(2);
        try {
            for (int i = 0; i < 5; i++) {
                Future<?> submit = executeEngine.submitAll(Collections.singletonList(mockScalingExecutor()), mockExecuteCallback());
                assertFalse(submit.isCancelled());
            }
        } catch (final RejectedExecutionException ex) {
            fail();
        }
    }
    
    private ExecuteCallback mockExecuteCallback() {
        return new ExecuteCallback() {
            @Override
            public void onSuccess() {
            
            }
            
            @Override
            public void onFailure(final Throwable throwable) {
            
            }
        };
    }
    
    private LifecycleExecutor mockScalingExecutor() {
        
        return new LifecycleExecutor() {
            @Override
            public void run() {
                ThreadUtil.sleep(100L);
            }
            
            @Override
            public void start() {
            }
            
            @Override
            public void stop() {
            }
        };
    }
}
