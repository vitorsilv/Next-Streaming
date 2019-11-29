cd ./target

mv BestStreaming-1.0-SNAPSHOT-jar-with-dependencies.jar BestStreaming.jar

mkdir BestStreaming

mv BestStreaming.jar ./BestStreaming

mv BestStreaming ~

echo "cd ~/BestStreaming && java -jar BestStreaming.jar" > BestStreaming.sh

mv BestStreaming.sh /usr/local/bin

cd ~

echo "Digite a sua senha para dar permissao"

sudo chmod 754 -R BestStreaming

cd /usr/local/bin

sudo chmod 754 BestStreaming.sh

echo "Instalação Finalizada" 
