package com.wuzizhuo;

import com.wuzizhuo.jgit.GitConfig;
import com.wuzizhuo.jgit.GitInstance;
import com.wuzizhuo.jgit.GitUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wuzizhuo
 * @created: 2020/3/18.
 * @updater:
 * @description:
 */
public class RuleEngine {

    public static GroovyExecutor initGroovyExecutor(GitConfig gitConfig) throws IOException, GitAPIException {
        checkGitConfig(gitConfig);

        GitInstance gitInstance = GitInstance.init(gitConfig);
        /** fetch resetHard */
        refreshGitResource(gitInstance);
        /** 一个git对应一个GroovyExecutor 使得一个项目可以支持多个git工程的加载，
         * JVM的双亲委派模型决定不同GroovyClassLoader之间有重复的className也不会冲突*/
        GroovyExecutor groovyExecutor = new GroovyExecutor();
        /** 把groovy文件加载到 groovyExecutor*/
        initGroovyFile(gitInstance.getGitConfig().getCurrentLocalDir(), groovyExecutor);
        /** JGIT的函数，不确定需要一定需要close **/
        gitInstance.close();
        return groovyExecutor;
    }

    /**
     * 在gitlab更新完规则后，使用这个函数不停应用更新groovyExecutor
     *
     * @param gitConfig
     * @param groovyExecutor
     * @param remote
     * @param branchName
     * @throws IOException
     * @throws GitAPIException
     */
    public static void refreshGroovyExecutor(GitConfig gitConfig, GroovyExecutor groovyExecutor,
                                             String remote, String branchName) throws IOException, GitAPIException {
        GitInstance gitInstance = GitInstance.init(gitConfig);
        /** fetch resetHard */
        refreshGitResource(gitInstance, remote, branchName);
        initGroovyFile(gitInstance.getGitConfig().getCurrentLocalDir(), groovyExecutor);
        /** JGIT的函数，不确定需要一定需要close **/
        gitInstance.close();
    }

    public static void refreshGroovyExecutor(GitConfig gitConfig, GroovyExecutor groovyExecutor) throws IOException, GitAPIException {
        refreshGroovyExecutor(gitConfig, groovyExecutor, "origin", "master");
    }

    /**
     * 执行某groovy文件里的函数
     *
     * @param groovyExecutor
     * @param fileKey        默认是不带groovy后缀的文件名,可重写genFileCacheKey函数
     * @param methodName
     * @param arguments
     * @return
     */
    public static Object runGroovyMethod(GroovyExecutor groovyExecutor,
                                         String fileKey, String methodName, Object... arguments) {
        return groovyExecutor.runFunctionFromCache(fileKey, methodName, arguments);
    }

    public static void refreshGitResource(GitInstance gitInstance) throws GitAPIException {
        refreshGitResource(gitInstance, "origin", "master");
    }

    public static void refreshGitResource(GitInstance gitInstance, String remote, String branchName) throws GitAPIException {
        if (remote == null || remote.trim().length() == 0) {
            throw new RuntimeException("remote 为空");
        } else if (branchName == null || branchName.trim().length() == 0) {
            throw new RuntimeException("branchName 为空");
        }
        Git git = gitInstance.getGit();
        CredentialsProvider credentialsProvider = gitInstance.getCredentialsProvider();
        GitUtil.fetch(git, credentialsProvider, remote);
        GitUtil.resetHard(git, remote + "/" + branchName);
    }

    protected static List<String> initGroovyFile(String path, GroovyExecutor groovyExecutor) {
        //目录
        File dataDir = new File(path);
        //存放目录及其子目录下的所有文件对象
        List<File> myfile = new ArrayList<File>();
        //开始遍历
        listDirectory(dataDir, myfile);

        List<String> fileKeyList = new ArrayList<>();

        for (File file : myfile) {
            String fileName = file.getName();
            String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            String fileKey = genFileCacheKey(fileName);
            /** 只加载groovy文件 */
            if (!".groovy".equals(fileType)) {
                continue;
            }
            try {
                /**  文件不能同名 */
                if (fileKeyList.contains(fileKey)) {
                    System.out.println("出现了同名的groovy文件，以扫描的最后一个为准。同名fileKey：" + fileKey);
                }
                groovyExecutor.cacheClass(fileKey, file);
                fileKeyList.add(fileKey);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

        groovyExecutor.clearNotExistsKey(fileKeyList);

        return fileKeyList;
    }

//    public static Object runRule(String fileKey, String methodName, Object... arguments) {
//        return GroovyExecutor.runFunctionFromCache(fileKey, methodName, arguments);
//    }

    protected static String genFileCacheKey(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 遍历目录及其子目录下的所有文件并保存
     *
     * @param path   目录全路径
     * @param myfile 列表：保存文件对象
     */
    protected static void listDirectory(File path, List<File> myfile) {
        if (!path.exists()) {
            System.out.println("文件名称不存在!");
        } else if (!path.isHidden()) {
            if (path.isFile()) {
                myfile.add(path);
            } else if (path.isDirectory()) {
                File[] files = path.listFiles();
                for (int i = 0; i < files.length; i++) {
                    listDirectory(files[i], myfile);
                }
            }
        }
    }

    protected static void checkGitConfig(GitConfig gitConfig) {
        // todo
    }
}
