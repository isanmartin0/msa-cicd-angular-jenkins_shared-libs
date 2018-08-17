#!/usr/bin/groovy
import com.evobanco.AngularConstants
import com.evobanco.AngularUtils

def call(body) {
    def utils = new com.evobanco.AngularUtils()
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()


    echo "angularDisplayInstalledDependencies parameters"
    echo "config.showGlobalInstalledDependencies: ${config.showGlobalInstalledDependencies}"
    echo "config.showGlobalInstalledDependenciesDepthLimit: ${config.showGlobalInstalledDependenciesDepthLimit}"
    echo "config.showGlobalInstalledDependenciesDepth: ${config.showGlobalInstalledDependenciesDepth}"
    echo "config.showLocalInstalledDependencies: ${config.showLocalInstalledDependencies}"
    echo "config.showLocalInstalledDependenciesDepthLimit: ${config.showLocalInstalledDependenciesDepthLimit}"
    echo "config.showLocalInstalledDependenciesDepth: ${config.showLocalInstalledDependenciesDepth}"
    echo "config.showLocalInstalledDependenciesOnlyType: ${config.showLocalInstalledDependenciesOnlyType}"
    echo "config.showLocalInstalledDependenciesType: ${config.showLocalInstalledDependenciesType}"


    Boolean showGlobalInstalledDependencies = false
    Boolean showLocalInstalledDependencies = false


    if (config.showGlobalInstalledDependencies) {
        showGlobalInstalledDependencies = config.showGlobalInstalledDependencies.toBoolean()
    }

    if (config.showLocalInstalledDependencies) {
        showLocalInstalledDependencies = config.showLocalInstalledDependencies.toBoolean()
    }

    if (showGlobalInstalledDependencies) {
        Boolean showGlobalInstalledDependenciesDepthLimit = false
        int showGlobalInstalledDependenciesDepth = -1
        String showGlobalInstalledDependenciesDepthFlags = ""

        if (config.showGlobalInstalledDependenciesDepthLimit) {
            showGlobalInstalledDependenciesDepthLimit = config.showGlobalInstalledDependenciesDepthLimit.toBoolean()
        }

        if (showGlobalInstalledDependenciesDepthLimit) {

            String showGlobalInstalledDependenciesDepthParam = config.showGlobalInstalledDependenciesDepth

            if (showGlobalInstalledDependenciesDepthParam != null && showGlobalInstalledDependenciesDepthParam.isInteger()) {
                showGlobalInstalledDependenciesDepth = showGlobalInstalledDependenciesDepthParam as Integer
            }
        }

        if (showGlobalInstalledDependenciesDepth >= 0) {
            showGlobalInstalledDependenciesDepthFlags = " --depth=${showGlobalInstalledDependenciesDepth}"
        }

        try {
            echo "List global dependencies ${showGlobalInstalledDependenciesDepthFlags}"
            sh "npm -g list ${showGlobalInstalledDependenciesDepthFlags}"
        } catch(err) {
            echo 'ERROR. There is an error retrieving NPM global dependencies'
        }

    }


    if (showLocalInstalledDependencies) {

        Boolean showLocalInstalledDependenciesDepthLimit = false
        int showLocalInstalledDependenciesDepth = -1
        Boolean showLocalInstalledDependenciesOnlyType = false
        String showLocalInstalledDependenciesType = ""
        String showLocalInstalledDependenciesDepthFlags = ""
        String showLocalInstalledDependenciesTypeFlags = ""


        if (config.showLocalInstalledDependenciesDepthLimit) {
            showLocalInstalledDependenciesDepthLimit = config.showLocalInstalledDependenciesDepthLimit.toBoolean()
        }

        if (config.showLocalInstalledDependenciesOnlyType) {
            showLocalInstalledDependenciesOnlyType = config.showLocalInstalledDependenciesOnlyType.toBoolean()
        }


        if (showLocalInstalledDependenciesDepthLimit) {

            String showLocalInstalledDependenciesDepthParam = config.showLocalInstalledDependenciesDepth

            if (showLocalInstalledDependenciesDepthParam != null && showLocalInstalledDependenciesDepthParam.isInteger()) {
                showLocalInstalledDependenciesDepth = showLocalInstalledDependenciesDepthParam as Integer
            }

            if (showLocalInstalledDependenciesDepth >= 0) {
                showLocalInstalledDependenciesDepthFlags = " --depth=${showLocalInstalledDependenciesDepth}"
            }
        }

        if (showLocalInstalledDependenciesOnlyType) {
            if (config.showLocalInstalledDependenciesType) {
                showLocalInstalledDependenciesType = config.showLocalInstalledDependenciesType
                showLocalInstalledDependenciesType = showLocalInstalledDependenciesType.trim()
            }

            if (!showLocalInstalledDependenciesType.equalsIgnoreCase("dev") && !showLocalInstalledDependenciesType.equalsIgnoreCase("prod")) {
                currentBuild.result = "FAILED"
                throw new hudson.AbortException("The parameter installedDependencies.showLocalInstalledDependenciesType has an incorrect value. Allowed values (dev, prod)") as Throwable
            }

            showLocalInstalledDependenciesTypeFlags = " --only=${showLocalInstalledDependenciesType}"
        }

        try {
            echo "List local dependencies ${showLocalInstalledDependenciesDepthFlags} ${showLocalInstalledDependenciesTypeFlags}"
/*
            def sout = new StringBuilder(), serr = new StringBuilder()
            def proc = "npm list ${showLocalInstalledDependenciesDepthFlags} ${showLocalInstalledDependenciesTypeFlags}".execute()
            proc.consumeProcessOutput(sout, serr)
            proc.waitForOrKill(30000)
            echo "out> ${sout}"
            echo "err> ${serr}"
 */
            sh "npm list ${showLocalInstalledDependenciesDepthFlags} ${showLocalInstalledDependenciesTypeFlags}"
        } catch(err) {
            echo 'ERROR. There is an error retrieving NPM local dependencies'
            def exc_message = exc.message
            echo "${exc_message}"
        }

    }

}
