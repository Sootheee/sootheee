echo "📦 Build Front-end"
sh 'cd ../client && npm install && npm run build'

echo "🚀 Deploy Front-end Dev"
sshagent(['front-server-ssh']) {
    sh '''
    echo "🚀 Deploying Frontend Development Server..."

    TARGET_HOST=user@frontend-dev-server
    TARGET_DIR=/home/user/next-app

    # .next 디렉토리 복사
    scp -r client/.next/ $TARGET_HOST:$TARGET_DIR

    # PM2 앱 재시작 또는 새로 시작
    ssh $TARGET_HOST "pm2 restart next-app || pm2 start npm --name next-app -- start"
    '''
}
