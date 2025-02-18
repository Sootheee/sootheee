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
                    scp -r client/.next/ user@frontend-dev-server:/home/user/next-app
                    ssh user@frontend-dev-server "pm2 restart next-app || pm2 start npm --name next-app -- start"
                    '''
                }
            }
        }
    }
}
