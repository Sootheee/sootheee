pipeline {
    agent any
    environment {
        TARGET_HOST = "opc@138.2.116.214"
        DEPLOY_PATH = "/home/opc/deploy/backend"
        JAR_NAME = "backend-prod.jar"
        CONTAINER_1 = "backend-prod-1"
        CONTAINER_2 = "backend-prod-2"
        IMAGE_NAME = "backend-prod:latest"
        HEALTH_CHECK_URL_1 = "http://localhost:8081/actuator/health"
        HEALTH_CHECK_URL_2 = "http://localhost:8082/actuator/health"
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
                    sh '''
                    scp /var/lib/jenkins/config/application-prod.properties $TARGET_HOST:$DEPLOY_PATH/src/main/resources/
                    scp /var/lib/jenkins/config/application-oauth2.properties $TARGET_HOST:$DEPLOY_PATH/src/main/resources/
                    '''
                }
            }
        }
        stage('Deploy to Production') {
            steps {
                sshagent(['soothee-linux-vm-ssh']) {
                    sh '''
                    echo "🔄 Rolling deployment started..."

                    # 서버로 JAR 파일 전송
                    scp server/build/libs/*.jar $TARGET_HOST:$DEPLOY_PATH/$JAR_NAME

                    # 첫 번째 컨테이너 롤링 배포
                    ssh $TARGET_HOST << EOF
                        echo "  Deploying $CONTAINER_1..."
                        docker stop $CONTAINER_1 || true
                        docker rm $CONTAINER_1 || true
                        docker run -d --name $CONTAINER_1 -p 8081:8080 \
                            -v $DEPLOY_PATH/$JAR_NAME:/app/app.jar \
                            --network=soothee_network \
                            openjdk:17 java -jar /app/app.jar
                    EOF

                    sleep 10
                    # 컨테이너 기동 후 안정화 대기

                    # 첫 번째 컨테이너 Health Check
                    ssh $TARGET_HOST << EOF
                        echo "🔍 Checking $CONTAINER_1 health..."
                        if ! curl --retry 5 --retry-connrefused --fail $HEALTH_CHECK_URL_1; then
                            echo "❌ Health Check failed! Rolling back..."
                            docker stop $CONTAINER_1
                            docker rm $CONTAINER_1
                            exit 1
                        fi
                    EOF

                    echo "✅ $CONTAINER_1 deployed successfully!"

                    # 두 번째 컨테이너 롤링 배포
                    ssh $TARGET_HOST << EOF
                        echo "  Deploying $CONTAINER_2..."
                        docker stop $CONTAINER_2 || true
                        docker rm $CONTAINER_2 || true
                        docker run -d --name $CONTAINER_2 -p 8082:8080 \
                            -v $DEPLOY_PATH/$JAR_NAME:/app/app.jar \
                            --network=soothee_network \
                            openjdk:17 java -jar /app/app.jar
                    EOF

                    sleep 10 # 컨테이너 기동 후 안정화 대기

                    # 두 번째 컨테이너 Health Check
                    ssh $TARGET_HOST << EOF
                        echo "🔍 Checking $CONTAINER_2 health..."
                        if ! curl --retry 5 --retry-connrefused --fail $HEALTH_CHECK_URL_2; then
                            echo "❌ Health Check failed! Rolling back..."
                            docker stop $CONTAINER_2
                            docker rm $CONTAINER_2
                            exit 1
                        fi
                    EOF

                    echo "✅ $CONTAINER_2 deployed successfully!"

                    echo "🎉 Rolling deployment completed successfully!"
                    '''
                }
            }
	    }
    }
}


