package com.github.developframework.kite.core.data;

/**
 * DataModel构造器
 * @author qiuzhenhao
 */
public class DataModelBuilder {

    private DataModel dataModel;

    public DataModelBuilder(Class<? extends DataModel> dataModelClass) {
        try {
            this.dataModel = dataModelClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataModelBuilder put(String dataName, Object data) {
        dataModel.putData(dataName, data);
        return this;
    }

    public DataModel build() {
        return dataModel;
    }
}
