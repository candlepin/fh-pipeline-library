#!/usr/bin/groovy
import org.feedhenry.GitHubUtils

/*
 * To be able to use the method exposed here, the following script signatures
 * must be approved:
 *
 * method org.kohsuke.github.GHObject getId
 * method org.kohsuke.github.GHUser isMemberOf org.kohsuke.github.GHOrganization
 * method org.kohsuke.github.GitHub getOrganization java.lang.String
 * method org.kohsuke.github.GitHub getUser java.lang.String
 * method org.kohsuke.github.GitHubBuilder build
 * method org.kohsuke.github.GitHubBuilder withOAuthToken java.lang.String java.lang.String
 * new org.kohsuke.github.GitHubBuilder
 *
 */
import org.kohsuke.github.GHOrganization
import org.kohsuke.github.GHUser
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitHubBuilder

final boolean isTrustedUser(String user, String gitHubCredentialsId) {
    node {
        withCredentials([usernamePassword(
                credentialsId: gitHubCredentialsId,
                passwordVariable: 'GITHUB_PASSWORD',
                usernameVariable: 'GITHUB_USERNAME')]) {
            final GitHub gitHub = new GitHubBuilder()
                    .withOAuthToken(env.GITHUB_PASSWORD, env.GITHUB_USERNAME)
                    .build()

            final GHUser ghUser = gitHub.getUser(user)
            final Set<Long> TRUSTED_USERS = [
                    // https://api.github.com/users/dependabot[bot]
                    49699333L,
                    // https://api.github.com/users/weblate
                    1607653L,
            ]
            return TRUSTED_USERS.contains(ghUser.getId())
        }
    }
}

final boolean isOrgMember(String user, String org, String gitHubCredentialsId) {
    node {
        withCredentials([usernamePassword(
                    credentialsId: gitHubCredentialsId,
                    passwordVariable: 'GITHUB_PASSWORD',
                    usernameVariable: 'GITHUB_USERNAME')]) {
            final GitHub gitHub = new GitHubBuilder()
                .withOAuthToken(env.GITHUB_PASSWORD, env.GITHUB_USERNAME)
                .build()

            final GHUser ghUser = gitHub.getUser(user)
            final GHOrganization ghOrganization = gitHub.getOrganization(org)
            return ghUser.isMemberOf(ghOrganization)
        }
    }
}

def call(String trustedOrg='candlepin', String gitHubCredentialsId='githubjenkins') {
    if (!env.CHANGE_AUTHOR) {
        println "This doesn't look like a GitHub PR, continuing"
    } else if (isTrustedUser(env.CHANGE_AUTHOR, gitHubCredentialsId)) {
        println "${env.CHANGE_AUTHOR} is trusted as an external contributor, continuing"
    } else if(isOrgMember(env.CHANGE_AUTHOR, trustedOrg, gitHubCredentialsId)) {
        println "${env.CHANGE_AUTHOR} is trusted as a member of the Candlepin org, continuing"
    } else {
        input(
            message: "Trusted approval needed for change from ${env.CHANGE_AUTHOR}",
            submitter: 'authenticated'
        )
    }
}
