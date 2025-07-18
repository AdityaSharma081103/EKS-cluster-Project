pipeline {
  agent any

  environment {
    DOCKER_HUB_CREDENTIALS = credentials('docker-hub-creds')
    IMAGE_NAME = "adiii007/${JOB_NAME.toLowerCase()}"
  }

  stages {
    stage('Clone') {
      steps {
        git branch: 'amazon', url: 'https://github.com/AdityaSharma081103/EKS-cluster-Project/'
      }
    }

    stage('Build Docker Image') {
      steps {
        sh 'docker build -t $IMAGE_NAME .'
      }
    }

    stage('Login & Push to DockerHub') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_HUB_USERNAME', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
          sh '''
            echo $DOCKER_HUB_PASSWORD | docker login -u $DOCKER_HUB_USERNAME --password-stdin
            docker push $IMAGE_NAME
          '''
        }
      }
    }

    stage('Deploy to GKE') {
      steps {
        withCredentials([file(credentialsId: 'gcloud-key', variable: 'GCLOUD_KEY')]) {
          sh '''
            gcloud auth activate-service-account --key-file=$GCLOUD_KEY
            gcloud container clusters get-credentials ecommerce-cluster --zone=asia-south1 --project=gke-cicd-466117
            kubectl apply -f Deployment.yaml
            kubectl apply -f Service.yaml
            
          '''
        }
      }
    }
  }
}
