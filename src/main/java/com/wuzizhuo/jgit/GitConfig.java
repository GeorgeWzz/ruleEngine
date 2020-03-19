package com.wuzizhuo.jgit;


/**
 * @author: wuzizhuo
 * @created: 2019/12/2.
 * @updater:
 * @description: 给规则文件使用的git的配置
 */

public class GitConfig {

    private String uri;
    private String userName;
    private String password;

    private String currentLocalDir;

    public GitConfig(){
    }

    public GitConfig(String uri, String userName, String password, String currentLocalDir) {
        this.uri = uri;
        this.userName = userName;
        this.password = password;
        this.currentLocalDir = currentLocalDir;
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrentLocalDir() {
        return currentLocalDir;
    }

    public void setCurrentLocalDir(String currentLocalDir) {
        this.currentLocalDir = currentLocalDir;
    }
}
