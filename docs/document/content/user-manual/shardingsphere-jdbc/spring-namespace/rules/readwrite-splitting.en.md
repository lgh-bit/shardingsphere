+++
title = "Readwrite-splitting"
weight = 2
+++

## Configuration Item Explanation

Namespace: [http://shardingsphere.apache.org/schema/shardingsphere/readwrite-splitting/readwrite-splitting-5.0.0.xsd](http://shardingsphere.apache.org/schema/shardingsphere/readwrite-splitting/readwrite-splitting-5.0.0.xsd)

\<readwrite-splitting:rule />

| *Name*               | *Type*    | *Description*                                |
| -------------------- | --------- | -------------------------------------------- |
| id                   | Attribute | Spring Bean Id                               |
| data-source-rule (+) | Tag       | Readwrite-splitting data source rule configuration |

\<readwrite-splitting:data-source-rule />

| *Name*                     | *Type*     | *Description*                                                           |
| -------------------------- | ---------- | ----------------------------------------------------------------------- |
| id                         | Attribute  | Readwrite-splitting data source rule name                               |
| write-data-source-name     | Attribute  | Write data source name                                                  |
| read-data-source-names     | Attribute  | Read data source names, multiple data source names separated with comma |
| load-balance-algorithm-ref | Attribute  | Load balance algorithm name                                             |

\<readwrite-splitting:load-balance-algorithm />

| *Name*    | *Type*     | *Description*                     |
| --------- | ---------- | --------------------------------- |
| id        | Attribute  | Load balance algorithm name       |
| type      | Attribute  | Load balance algorithm type       |
| props (?) | Tag        | Load balance algorithm properties |

Please refer to [Built-in Load Balance Algorithm List](/en/user-manual/shardingsphere-jdbc/builtin-algorithm/load-balance) for more details about type of algorithm.
Please refer to [Use Norms](/en/features/readwrite-splitting/use-norms) for more details about query consistent routing.
