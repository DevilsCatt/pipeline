import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.05"

project {

    vcsRoot(HttpsGithubComRomanowBackendTodoListGit)

    buildType(Run)
}

object Run : BuildType({
    name = "run"

    vcs {
        root(HttpsGithubComRomanowBackendTodoListGit)

        cleanCheckout = true
    }

    steps {
        gradle {
            name = "build"
            tasks = "clean build -x test"
            buildFile = "build.gradle"
        }
        script {
            name = "1"
            enabled = false
            scriptContent = "./graldew clean build"
        }
        dockerCommand {
            name = "docker build"
            enabled = false
            commandType = build {
                source = file {
                    path = "Dockerfile"
                }
                namesAndTags = "front:1.0"
            }
        }
        script {
            scriptContent = "docker ps"
        }
    }
})

object HttpsGithubComRomanowBackendTodoListGit : GitVcsRoot({
    name = "https://github.com/Romanow/backend-todo-list.git"
    url = "https://github.com/Romanow/backend-todo-list.git"
    branch = "refs/heads/master"
})
