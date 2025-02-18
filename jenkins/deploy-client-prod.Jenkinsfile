pipeline {
    agent any
    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }
        stage('Build Frontend') {
            steps {
                sh 'cd client && npm install && npm run build'
            }
        }
        stage('Deploy to Development') {
            steps {
                sshagent(['front-server-ssh']) {
                    sh '''
                    echo "🚀 Deploying Frontend Development Server..."
                    scp -r client/.next/ user@frontend-prod-server:/home/user/next-app
                    ssh user@frontend-prod-server "pm2 restart next-app || pm2 start npm --name next-app -- start"
                    '''
                }
            }
        }
    }
}
