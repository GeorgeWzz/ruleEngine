package com.wuzizhuo.jgit;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * @author: wuzizhuo
 * @created: 2019/12/2.
 * @updater:
 * @description:
 */
public class GitUtil {

    public static Git getGit(String uri, CredentialsProvider credentialsProvider, String localDir) throws IOException, GitAPIException {
        Git git = null;
        if (new File(localDir).exists()) {
            git = Git.open(new File(localDir));
        } else {
            git = Git.cloneRepository().setCredentialsProvider(credentialsProvider).setURI(uri)
                    .setDirectory(new File(localDir)).call();
        }
        //设置一下post内存，否则可能会报错Error writing request body to server
        // git.getRepository().getConfig().setInt(HttpConfig.HTTP, null, HttpConfig.POST_BUFFER_KEY, 512*1024*1024);
        return git;
    }

    public static void close(Git git) {
        git.close();
    }

    public static CredentialsProvider getCredentialsProvider(String username, String password) {
        return new UsernamePasswordCredentialsProvider(username, password);
    }

    public static Repository getRepository(Git git) {
        return git.getRepository();
    }

    public static void checkout_b(Git git, String branchName) throws GitAPIException {
        git.checkout().setName(branchName).setCreateBranch(true).call();
    }

    public static void checkout(Git git, String branchName) throws GitAPIException {
        git.checkout().setName(branchName).setCreateBranch(false).call();
    }

    public static void resetHard(Git git, String branchName) throws GitAPIException {
        git.reset().setMode(ResetCommand.ResetType.HARD).setRef(branchName).call();
    }

    public static void fetch(Git git, CredentialsProvider credentialsProvider, String remote) throws GitAPIException {
        git.fetch().setCredentialsProvider(credentialsProvider).setRemote(remote).call();
    }

    public static void pull(Git git, CredentialsProvider credentialsProvider, String remote) throws GitAPIException {
        git.pull().setRemote(remote).setCredentialsProvider(credentialsProvider).call();
    }

    public static void push(Git git, CredentialsProvider credentialsProvider, String filepattern, String message)
            throws GitAPIException {

        git.add().addFilepattern(filepattern).call();
        git.add().setUpdate(true);
        git.commit().setMessage(message).call();
        git.push().setCredentialsProvider(credentialsProvider).call();

    }

    public static void main(String[] args) throws Exception {
        String uri = "git@10.0.50.100:wuzizhuo/xrisk-rule-file.git";
        String username = "wuzizhuo";
        String password = "wuzizhuo";
        CredentialsProvider credentialsProvider = getCredentialsProvider(username, password);

        String localDir = "/rule-git-file/xrisk-rule-file";
        Git git = getGit(uri, credentialsProvider, localDir);
        fetch(git, credentialsProvider, "origin");
        resetHard(git, "origin/master");
//        push(git, credentialsProvider, ".", "提交文件");

    }
}
