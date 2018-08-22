#!/usr/bin/groovy

def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()


    echo "angularExecuteUnitTesting global variable parameters\n" +
            "----------------------------------------------------\n" +
            "config.useUnitTestingFlags: ${config.useUnitTestingFlags} \n" +
            "config.theUnitTestingDefaultFlags: ${config.theUnitTestingDefaultFlags} \n" +
            "config.theUnitTestingFlags: ${config.theUnitTestingFlags} \n" +
            "config.theAngularCliLocalPath: ${config.theAngularCliLocalPath} \n" +
            "config.theInstallGloballyAngularCli: ${config.theInstallGloballyAngularCli} \n"


    echo "Building unit test"

    def karmaConfJS = readFile file: 'karma.conf.js'

    echo "karma.conf.js content:\n" +
            "${karmaConfJS}"

    /***********************************************************
     ************* BUILD PRODUCTION PARAMETERS *****************
     ***********************************************************/

    Boolean useUnitTestingFlags = false
    def unitTestingFlags = config.theUnitTestingDefaultFlags
    Boolean installGloballyAngularCli = config.theInstallGloballyAngularCli
    def angularCliLocalPath = config.theAngularCliLocalPath

    if (config.useUnitTestingFlags) {
        useUnitTestingFlags = config.useUnitTestingFlags.toBoolean()
    }

    if (useUnitTestingFlags) {
        unitTestingFlags = config.theUnitTestingFlags
    } else {
        echo "Unit testing flags default: ${unitTestingFlags}"
    }

    echo "useUnitTestingFlags: ${useUnitTestingFlags}"
    echo "unitTestingFlags: ${unitTestingFlags}"



    if (installGloballyAngularCli) {
        sh "ng test ${unitTestingFlags}"
    } else {
        sh "${angularCliLocalPath}ng test ${unitTestingFlags}"
    }
}
