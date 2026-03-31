$mavenCmd = 'c:\Users\HP\CraveCart\apache-maven\apache-maven-3.9.10\bin\mvn.cmd'
if (-Not (Test-Path $mavenCmd)) {
    Write-Error "Maven binary not found at $mavenCmd"
    exit 1
}

$env:MAVEN_OPTS = '-Xms32m -Xmx64m -XX:MaxRAM=128m -XX:MaxMetaspaceSize=64m -XX:CompressedClassSpaceSize=32m -XX:+UseSerialGC -Djava.awt.headless=true'
& $mavenCmd -f 'c:\Users\HP\CraveCart\backend\pom.xml' -DskipTests -T1 compile
if ($LASTEXITCODE -ne 0) {
    Write-Error "Backend build failed with exit code $LASTEXITCODE"
    exit $LASTEXITCODE
}
Write-Host "Backend build succeeded." -ForegroundColor Green
