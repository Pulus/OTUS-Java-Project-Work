dependencies {
    runtimeOnly("net.solarnetwork.common:nifty-modbus-tcp:0.17.0")
    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
    implementation("com.digitalpetri.netty:netty-channel-fsm:1.0.0")
    implementation("com.digitalpetri.modbus:modbus:2.0.0")
    implementation("com.digitalpetri.modbus:modbus-serial:2.0.0")
    implementation("com.digitalpetri.modbus:modbus-master-tcp:1.2.2")
    implementation("io.netty:netty-all:4.1.112.Final")

    implementation ("ch.qos.logback:logback-classic")
//    implementation("org.slf4j:slf4j-api:2.0.16")
//    testImplementation("org.slf4j:slf4j-simple:2.0.16")

    implementation("org.apache.maven.plugins:maven-compiler-plugin:3.13.0")
//   implementation("org.apache.maven.plugins:maven-jar-plugin:3.4.2")
//    implementation("org.apache.maven.plugins:maven-deploy-plugin:3.1.3")
//    implementation("org.apache.maven.plugins:maven-gpg-plugin:3.2.4")
//    implementation("org.apache.maven.plugins:maven-install-plugin:3.1.3")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
}