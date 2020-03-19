package com.wuzizhuo.jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;

import java.io.IOException;

/**
 * @author: wuzizhuo
 * @created: 2020/3/19.
 * @updater:
 * @description:
 */
public class GitInstance {
    protected Git git;
    protected GitConfig gitConfig;
    protected CredentialsProvider credentialsProvider;

    public static GitInstance init(GitConfig gitConfig) throws IOException, GitAPIException {
        String uri = gitConfig.getUri();
        String username = gitConfig.getUserName();
        String password = gitConfig.getPassword();
        String localDir = gitConfig.getCurrentLocalDir();
        CredentialsProvider credentialsProvider = GitUtil.getCredentialsProvider(username, password);

        Git git = GitUtil.getGit(uri, credentialsProvider, localDir);
        GitInstance gitInstance = new GitInstance();
        gitInstance.setGit(git);
        gitInstance.setGitConfig(gitConfig);
        gitInstance.setCredentialsProvider(credentialsProvider);
        return gitInstance;
    }

    public void close() {
        GitUtil.close(this.git);
    }

    public Git getGit() {
        return git;
    }

    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    public void setGit(Git git) {
        this.git = git;
    }

    public GitConfig getGitConfig() {
        return gitConfig;
    }

    public void setGitConfig(GitConfig gitConfig) {
        this.gitConfig = gitConfig;
    }
}
