stage('Build Frontend') {
    steps {
        sh 'cd client && npm install && npm run build'
    }
}

stage('Deploy to Production') {
    steps {
        sshagent(['front-server-ssh']) {
            sh '''
            echo "🚀 Deploying Frontend Production Server..."

            # 배포할 경로 정보
            TARGET_HOST=user@frontend-prod-server
            TARGET_DIR=/home/user/next-app

            # 빌드된 결과 업로드
            scp -r client/.next/ $TARGET_HOST:$TARGET_DIR

            # PM2로 재시작 또는 실행
            ssh $TARGET_HOST "pm2 restart next-app || pm2 start npm --name next-app -- start"
            '''
        }
    }
}
