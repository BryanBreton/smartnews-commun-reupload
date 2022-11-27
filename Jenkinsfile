#!groovy
@Library('gie') _

continuousIntegration(
        builder: 'mvn',
        imageForBuild: 'docker-registry-iris.groupement.systeme-u.fr/iris-factory/mvn:3.6.3-jdk11',
        deployer: 'none',
        authProvider: 'none',
        forceEligibilityForRelease: 'true'
)
