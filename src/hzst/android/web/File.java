package hzst.android.web;

import android.content.Context;

import hzst.android.util.FileUtil;

/**
 * Created by Administrator on 2016/6/12.
 */
public class File extends BaseJsInterface{
    public File(Context context) {
        super(context);
        name = getClassName(this);
    }

    public void createFile(String filePath){
        FileUtil fileUtil = new FileUtil(context);
        if(fileUtil.createFile(filePath) != null){
            getWebExample().executeJsFunction(getWebExample().baseJsInterface.operation.getOnSuccess());
        }
    }

    public void writeToFile(String filePath,String content){
        FileUtil fileUtil = new FileUtil(context);
        fileUtil.write(filePath, content);
        getWebExample().executeJsFunction(getWebExample().baseJsInterface.operation.getOnSuccess());
    }

    public void readFromFile(String filePath){
        FileUtil fileUtil = new FileUtil(context);
        String content = fileUtil.read(filePath);
        if(content != null){
            getWebExample().executeJsFunction(getWebExample().baseJsInterface.operation.getOnSuccess(),content);
        }
    }
}
