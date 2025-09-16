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
                            sudo kubectl apply -f db-deployment.yaml
                        """
                    }
                }
            }
        }

        stage('install'){
            steps {
                container('kubectl') {
                    script {
                        sh """
                            sudo docker exec -it oracle-xe sqlplus system/1234@XEPDB1 <<EOF
                                ALTER USER hr IDENTIFIED BY hr1234 ACCOUNT UNLOCK;
                                @/opt/human_resources/hr_install.sql
                                EXIT;
                            EOF
                        """
                    }
                }
            }
        }
    }
}