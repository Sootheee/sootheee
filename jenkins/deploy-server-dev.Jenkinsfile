pipeline {
    agent any
    environment {
        TARGET_HOST = "opc@138.2.116.214"
        DEPLOY_PATH = "/home/opc/deploy/backend"
        RES_PATH = "/home/opc/deploy/sootheee/server/src/main/resources/"
        JAR_NAME = "backend-dev.jar"
    }
    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }
        stage('Build Backend') {
            steps {
                sh 'cd server && ./gradlew clean build -x test'
            }
        }
        stage('Deploy Configuration') {
            steps {
                sshagent(['soothee-linux-vm-ssh']) {
                    sh '''"🔄 Copying configuration files to development server..."
                    scp /var/lib/jenkins/config/application-prod.properties $TARGET_HOST:$RES_PATH
                    scp /var/lib/jenkins/config/application-oauth2.properties $TARGET_HOST:$RES_PATH
                    '''
                }
            }
        }
        stage('Deploy to Development') {
            steps {
                sshagent(['soothee-linux-vm-ssh']) {
                    sh '''
                    echo "🚀 Deploying Backend Development Server..."
                    scp server/build/libs/*.jar $TARGET_HOST:$DEPLOY_PATH/$JAR_NAME
                    ssh $TARGET_HOST "cd /home/opc/deploy/backend && docker-compose restart backend-dev"
                    '''
                }
            }
        }
    }
}
