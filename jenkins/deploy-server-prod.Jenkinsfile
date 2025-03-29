echo "📦 Build Backend"
sh 'cd server && ./gradlew clean build -x test'

echo "🚀 Deploy Backend Prod"
sshagent(['soothee-linux-vm-ssh']) {
    sh '''
    echo "🔄 Rolling deployment started..."

    TARGET_HOST=opc@138.2.116.214
    DEPLOY_PATH=/home/opc/deploy/backend
    JAR_NAME=backend-prod.jar
    CONTAINER_1=backend-prod-1
    CONTAINER_2=backend-prod-2
    IMAGE_NAME=backend-prod:latest
    HEALTH_CHECK_URL_1=http://localhost:8081/actuator/health
    HEALTH_CHECK_URL_2=http://localhost:8082/actuator/health

    # JAR 전송
    scp server/build/libs/*.jar $TARGET_HOST:$DEPLOY_PATH/$JAR_NAME

    # 1번 컨테이너 롤링 배포
    ssh $TARGET_HOST << EOF
        echo "🚀 Deploying $CONTAINER_1..."
        docker stop $CONTAINER_1 || true
        docker rm $CONTAINER_1 || true
        docker run -d --name $CONTAINER_1 -p 8081:8080 \
            -v $DEPLOY_PATH/$JAR_NAME:/app/app.jar \
            --network=soothee_network \
            openjdk:17 java -jar /app/app.jar
    EOF

    sleep 10

    # Health Check 1
    ssh $TARGET_HOST << EOF
        echo "🔍 Checking $CONTAINER_1..."
        if ! curl --retry 5 --retry-connrefused --fail $HEALTH_CHECK_URL_1; then
            echo "❌ Health Check failed for $CONTAINER_1"
            docker stop $CONTAINER_1
            docker rm $CONTAINER_1
            exit 1
        fi
    EOF

    echo "✅ $CONTAINER_1 OK"

    # 2번 컨테이너 롤링 배포
    ssh $TARGET_HOST << EOF
        echo "🚀 Deploying $CONTAINER_2..."
        docker stop $CONTAINER_2 || true
        docker rm $CONTAINER_2 || true
        docker run -d --name $CONTAINER_2 -p 8082:8080 \
            -v $DEPLOY_PATH/$JAR_NAME:/app/app.jar \
            --network=soothee_network \
            openjdk:17 java -jar /app/app.jar
    EOF

    sleep 10

    # Health Check 2
    ssh $TARGET_HOST << EOF
        echo "🔍 Checking $CONTAINER_2..."
        if ! curl --retry 5 --retry-connrefused --fail $HEALTH_CHECK_URL_2; then
            echo "❌ Health Check failed for $CONTAINER_2"
            docker stop $CONTAINER_2
            docker rm $CONTAINER_2
            exit 1
        fi
    EOF

    echo "✅ $CONTAINER_2 OK"
    echo "🎉 Rolling deployment complete!"
    '''
}