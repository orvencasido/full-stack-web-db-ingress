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

        stage ('run') {
            steps {
                container('kubectl') {
                    script {
                        sh """
                            sudo docker run -d --name oracle-xe \
                            -p 1521:1521 \
                            -e ORACLE_PASSWORD="1234" \
                            -v $HOME/db-sample-schemas/human_resources:/opt/human_resources:ro \
                            gvenzl/oracle-xe:21.3.0-slim
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