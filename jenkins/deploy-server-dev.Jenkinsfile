stage('Build Backend') {
    steps {
        sh 'cd server && ./gradlew clean build -x test'
    }
}

stage('Deploy to Development') {
    steps {
        sshagent(['soothee-linux-vm-ssh']) {
            sh '''
            echo "🚀 Deploying Backend Development Server..."

            TARGET_HOST=opc@138.2.116.214
            DEPLOY_PATH=/home/opc/deploy/backend
            JAR_NAME=backend-dev.jar

            # JAR 파일 전송
            scp server/build/libs/*.jar $TARGET_HOST:$DEPLOY_PATH/$JAR_NAME

            # docker-compose 로 검수 서버 재시작
            ssh $TARGET_HOST "cd $DEPLOY_PATH && docker-compose restart backend-dev"
            '''
        }
    }
}
