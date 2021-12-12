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

package org.apache.shardingsphere.infra.executor.sql;

/**
 * Connection Mode.
 */

/**
 * 内存限制模式
 * 使⽤此模式的前提是，ShardingSphere 对⼀次操作所耗费的数据库连接数量不做限制。如果实际执⾏的
 * SQL 需要对某数据库实例中的 200 张表做操作，则对每张表创建⼀个新的数据库连接，并通过多线程的
 * ⽅式并发处理，以达成执⾏效率最⼤化。并且在 SQL 满⾜条件情况下，优先选择流式归并，以防⽌出现
 * 内存溢出或避免频繁垃圾回收情况。
 * 连接限制模式
 * 使⽤此模式的前提是，ShardingSphere 严格控制对⼀次操作所耗费的数据库连接数量。如果实际执⾏的
 * SQL 需要对某数据库实例中的 200 张表做操作，那么只会创建唯⼀的数据库连接，并对其 200 张表串⾏
 * 处理。如果⼀次操作中的分⽚散落在不同的数据库，仍然采⽤多线程处理对不同库的操作，但每个库的
 * 每次操作仍然只创建⼀个唯⼀的数据库连接。这样即可以防⽌对⼀次请求对数据库连接占⽤过多所带来
 * 的问题。该模式始终选择内存归并。
 * 内存限制模式适⽤于 OLAP 操作，可以通过放宽对数据库连接的限制提升系统吞吐量；连接限制模式适
 * ⽤于 OLTP 操作，OLTP 通常带有分⽚键，会路由到单⼀的分⽚，因此严格控制数据库连接，以保证在线
 * 系统数据库资源能够被更多的应⽤所使⽤，是明智的选择。
 */
public enum ConnectionMode {
    //内存限制模式
    MEMORY_STRICTLY,
    //连接限制模式
    CONNECTION_STRICTLY
}
