import com.wuzizhuo.GroovyExecutor;
import com.wuzizhuo.RuleEngine;
import com.wuzizhuo.jgit.GitConfig;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

/**
 * @author: wuzizhuo
 * @created: 2020/3/19.
 * @updater:
 * @description:
 */
public class test1 {
    public static void main(String[] args) {
        String uri = "git@10.0.50.100:wuzizhuo/xrisk-rule-file.git";
        String userName = "wuzizhuo";
        String password = "wuzizhuo";
        String currentLocalDir = "/rule-git-file/xrisk-rule-file";

        GitConfig gitConfig = new GitConfig(uri, userName, password, currentLocalDir);
        try {
            /** 第一次初始化*/
            GroovyExecutor groovyExecutor = RuleEngine.initGroovyExecutor(gitConfig);
            // 应用时应当将groovyExecutor缓存（JVM）
            String pbocType = (String) RuleEngine.runGroovyMethod(groovyExecutor, "rule1",
                    "choicePbocType", "CFT", "", "");
            String pbocType1 = (String) RuleEngine.runGroovyMethod(groovyExecutor, "rule1",
                    "choicePbocType", "CFT", "", "");
            System.out.println(pbocType);
            System.out.println(pbocType1);

            /** 更新gitlab后再执行测试*/
            RuleEngine.refreshGroovyExecutor(gitConfig, groovyExecutor,
                    "origin", "master");
            pbocType = (String) RuleEngine.runGroovyMethod(groovyExecutor, "rule1",
                    "choicePbocType", "CFT", "", "");
            System.out.println(pbocType);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
}
