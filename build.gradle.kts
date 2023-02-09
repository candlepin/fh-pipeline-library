plugins {
    groovy
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.jenkins-ci.org/releases/")
    }
}

sourceSets {
    main {
        groovy {
            srcDirs("src","vars")
        }
    }
}

dependencies {
    implementation("org.codehaus.groovy:groovy-all:3.0.14")
//    implementation("org.codehaus.groovy:groovy-all:2.4.15")
//    implementation("org.codehaus.groovy:groovy-all:4.0.8")
    implementation("org.kohsuke:github-api:1.313")
    implementation("com.cloudbees:groovy-cps:1.23")
    implementation("org.codehaus.groovy:groovy-xmlrpc:0.8")
//    implementation("org.jvnet.hudson.plugins:ircbot:3.829.v12d4b_d1f7650")
//    implementation("org.jvnet.hudson.plugins:ircbot:2.9")
//    implementation("org.jvnet.hudson.plugins:ircbot:3.829.v12d4b_d1f7650")
//Thanks for using https://jar-download.com

//    implementation("org.apache.ivy:ivy:2.4.0")
//    implementation("com.lesfurets:jenkins-pipeline-unit:1.1")
//    implementation("org.jenkins-ci.main:jenkins-core:2.164.1")
// Defining it twice for Gradle 5 as some annotation processors live here as well
//    def staplerGAV = 'org.kohsuke.stapler:stapler:1.255'
//    compile staplerGAV
//            annotationProcessor staplerGAV
//            compile 'org.jenkins-ci.plugins.workflow:workflow-step-api:2.19@jar'
//    implementation ("org.jenkins-ci.plugins:pipeline-utility-steps:2.2.0@jar")
}
