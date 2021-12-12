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

package org.apache.shardingsphere.sharding.route.engine.type;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.infra.config.properties.ConfigurationProperties;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.sharding.route.engine.condition.ShardingConditions;
import org.apache.shardingsphere.sharding.route.engine.type.broadcast.ShardingDataSourceGroupBroadcastRoutingEngine;
import org.apache.shardingsphere.sharding.route.engine.type.broadcast.ShardingDatabaseBroadcastRoutingEngine;
import org.apache.shardingsphere.sharding.route.engine.type.broadcast.ShardingInstanceBroadcastRoutingEngine;
import org.apache.shardingsphere.sharding.route.engine.type.broadcast.ShardingTableBroadcastRoutingEngine;
import org.apache.shardingsphere.sharding.route.engine.type.complex.ShardingComplexRoutingEngine;
import org.apache.shardingsphere.sharding.route.engine.type.ignore.ShardingIgnoreRoutingEngine;
import org.apache.shardingsphere.sharding.route.engine.type.standard.ShardingStandardRoutingEngine;
import org.apache.shardingsphere.sharding.route.engine.type.unconfigured.ShardingUnconfiguredTablesRoutingEngine;
import org.apache.shardingsphere.sharding.route.engine.type.unicast.ShardingUnicastRoutingEngine;
import org.apache.shardingsphere.sharding.rule.ShardingRule;
import org.apache.shardingsphere.sql.parser.binder.metadata.schema.SchemaMetaData;
import org.apache.shardingsphere.sql.parser.binder.statement.SQLStatementContext;
import org.apache.shardingsphere.sql.parser.binder.type.TableAvailable;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dal.DALStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dal.SetStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.mysql.dal.MySQLShowDatabasesStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.mysql.dal.MySQLUseStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.dal.PostgreSQLResetParameterStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dcl.DCLStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.DDLStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.DMLStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.SelectStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.tcl.TCLStatement;

import java.util.Collection;
import java.util.Map;

/**
 * Sharding routing engine factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShardingRouteEngineFactory {
    
    /**
     * 广播（broadcast）路由、混合（complex）路由、默认数据库（defaultdb）路由、无效（ignore）路由、
     * 标准（standard）路由以及单播（unicast）路由
     * Create new instance of routing engine.
     * 
     * @param shardingRule sharding rule
     * @param metaData meta data of ShardingSphere
     * @param sqlStatementContext SQL statement context
     * @param shardingConditions shardingConditions
     * @param props ShardingSphere properties
     * @return new instance of routing engine
     */
    public static ShardingRouteEngine newInstance(final ShardingRule shardingRule, final ShardingSphereMetaData metaData, 
                                                  final SQLStatementContext sqlStatementContext, final ShardingConditions shardingConditions, final ConfigurationProperties props) {
        SQLStatement sqlStatement = sqlStatementContext.getSqlStatement();
        Collection<String> tableNames = sqlStatementContext.getTablesContext().getTableNames();
        //全库路由
        if (sqlStatement instanceof TCLStatement) {
            return new ShardingDatabaseBroadcastRoutingEngine();
        }
        //全库表路由
        if (sqlStatement instanceof DDLStatement) {
            return new ShardingTableBroadcastRoutingEngine(metaData.getSchema().getConfiguredSchemaMetaData(), sqlStatementContext);
        }
        //阻断路由
        if (sqlStatement instanceof DALStatement) {
            return getDALRoutingEngine(shardingRule, metaData.getSchema().getUnconfiguredSchemaMetaDataMap(), sqlStatement, tableNames);
        }
        //全实例路由
        if (sqlStatement instanceof DCLStatement) {
            return getDCLRoutingEngine(sqlStatementContext, metaData);
        }
        //全库路由
        if (shardingRule.isAllBroadcastTables(tableNames)) {
            return sqlStatement instanceof SelectStatement ? new ShardingUnicastRoutingEngine(tableNames) : new ShardingDatabaseBroadcastRoutingEngine();
        }
        //单播路由
        if (sqlStatementContext.getSqlStatement() instanceof DMLStatement && shardingConditions.isAlwaysFalse() || tableNames.isEmpty()) {
            return new ShardingUnicastRoutingEngine(tableNames);
        }
        //默认库路由
        if (!shardingRule.tableRuleExists(tableNames)) {
            return new ShardingUnconfiguredTablesRoutingEngine(tableNames, metaData.getSchema().getUnconfiguredSchemaMetaDataMap());
        }
        //分片路由
        return getShardingRoutingEngine(shardingRule, shardingConditions, tableNames, props);
    }
    
    private static ShardingRouteEngine getDALRoutingEngine(final ShardingRule shardingRule,
                                                           final Map<String, SchemaMetaData> unconfiguredSchemaMetaDataMap, final SQLStatement sqlStatement, final Collection<String> tableNames) {
        //如果是Use语句，则什么也不做
        if (sqlStatement instanceof MySQLUseStatement) {
            return new ShardingIgnoreRoutingEngine();
        }
        //如果是Set或ResetParameter语句，则进行全数据库广播
        if (sqlStatement instanceof SetStatement || sqlStatement instanceof PostgreSQLResetParameterStatement || sqlStatement instanceof MySQLShowDatabasesStatement) {
            return new ShardingDatabaseBroadcastRoutingEngine();
        }
        //如果存在默认数据库，则执行默认数据库路由
        if (!tableNames.isEmpty() && !shardingRule.tableRuleExists(tableNames)) {
            return new ShardingUnconfiguredTablesRoutingEngine(tableNames, unconfiguredSchemaMetaDataMap);
        }
        //如果表列表不为空，则执行单播路由
        if (!tableNames.isEmpty()) {
            return new ShardingUnicastRoutingEngine(tableNames);
        }
        return new ShardingDataSourceGroupBroadcastRoutingEngine();
    }
    
    private static ShardingRouteEngine getDCLRoutingEngine(final SQLStatementContext sqlStatementContext, final ShardingSphereMetaData metaData) {
        return isDCLForSingleTable(sqlStatementContext) 
                ? new ShardingTableBroadcastRoutingEngine(metaData.getSchema().getConfiguredSchemaMetaData(), sqlStatementContext)
                : new ShardingInstanceBroadcastRoutingEngine(metaData.getDataSources());
    }
    
    private static boolean isDCLForSingleTable(final SQLStatementContext sqlStatementContext) {
        if (sqlStatementContext instanceof TableAvailable) {
            TableAvailable tableSegmentsAvailable = (TableAvailable) sqlStatementContext;
            return 1 == tableSegmentsAvailable.getAllTables().size() && !"*".equals(tableSegmentsAvailable.getAllTables().iterator().next().getTableName().getIdentifier().getValue());
        }
        return false;
    }

    /**
     * 如果分片操作只涉及一张表，或者涉及多张表，但这些表是互为绑定表的关系时，则使用 StandardRoutingEngine 进行路由。
     *
     * 基于绑定表的概念，当多表互为绑定表关系时，每张表的路由结果是相同的，所以只要计算第一张表的分片即可；
     * 反之，如果不满足这一条件，则构建一个 ComplexRoutingEngine 进行路由.
     *
     * @param shardingRule
     * @param shardingConditions
     * @param tableNames
     * @param props
     * @return
     */
    private static ShardingRouteEngine getShardingRoutingEngine(final ShardingRule shardingRule, final ShardingConditions shardingConditions, 
                                                                final Collection<String> tableNames, final ConfigurationProperties props) {
        Collection<String> shardingTableNames = shardingRule.getShardingLogicTableNames(tableNames);
        /**
         * 如果分片操作只涉及一张表，或者涉及多张表，但这些表是互为绑定表的关系时，则使用 StandardRoutingEngine 进行路由。
         */
        if (1 == shardingTableNames.size() || shardingRule.isAllBindingTables(shardingTableNames)) {
            return new ShardingStandardRoutingEngine(shardingTableNames.iterator().next(), shardingConditions, props);
        }
        // TODO config for cartesian set
        return new ShardingComplexRoutingEngine(tableNames, shardingConditions, props);
    }
}
