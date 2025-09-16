pipeline {
    agent {
        kubernetes {
            label 'docker-kubectl-agent'
        }
    }

    stages {
        stage('clone') {
            steps {
                script {
                    sh """
                        rm -rf db-sample-schemas
                        git clone https://github.com/oracle-samples/db-sample-schemas.git
                    """
                }
            } 
        } 

        stage ('deploy db to k8s') {
            steps {
                container('kubectl') {
                    script {
                        sh """
                            kubectl apply -f db-deployment.yaml
                            kubectl apply -f db-persistentVolume.yaml
                            kubectl apply -f db-persistentVolumeClaim.yaml
                        """
                    }
                }
            }
        }
    }
}