node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'mvn';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify sonar:sonar -DskipTests -Dsonar.projectKey=java -Dsonar.login=squ_dab51f1eb02b32790237df27d9d1b28faaeda688" 
    }
  }
}
