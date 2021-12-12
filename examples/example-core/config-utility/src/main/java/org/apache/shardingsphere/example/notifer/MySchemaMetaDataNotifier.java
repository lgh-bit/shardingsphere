package org.apache.shardingsphere.example.notifer;

import org.apache.shardingsphere.governance.core.event.persist.MetaDataPersistEvent;
import org.apache.shardingsphere.governance.core.eventbus.ShardingSphereEventBus;
import org.apache.shardingsphere.infra.metadata.schema.RuleSchemaMetaData;
import org.apache.shardingsphere.infra.metadata.schema.spi.RuleMetaDataNotifier;

import java.util.Properties;

/**
 * @description:
 * @author: liuguohong
 * @create: 2020/10/28 17:27
 */

public class MySchemaMetaDataNotifier implements RuleMetaDataNotifier {

    @Override
    public void notify(final String schemaName, final RuleSchemaMetaData metaData) {
        ShardingSphereEventBus.getInstance().post(new MetaDataPersistEvent(schemaName, metaData));
    }

    @Override
    public String getType() {
        return "metadata-notifier";
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void setProps(final Properties props) {
    }
}
