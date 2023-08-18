pipeline
{
agent any



stages {


stage("Build Modules & Build Docker Images") {

steps {

script {

// def modules = findFiles(glob: '**/pom.xml')

def modules = ['gymapplication1', 'reportapplication','notificationapplication','GymAuthenticationService-1','eurekaserver','GatewayServer','GymCommons']

for (def module in modules) {

dir("${module}") {

echo "Building ${module}..."

bat "mvn clean install"
}
}
}
}
}
}
}