#!/bin/bash

BASE_PATH=/app
ORDS_PATH=$BASE_PATH/ords
ORDS_CONF=$ORDS_PATH/ords/ords
TEMPLATE_PATH=$BASE_PATH/templates


function configureOrds(){
  echo "==>> Cleanup ...."
  rm -f $ORDS_CONF/url-mapping.xml
  rm -f $ORDS_CONF/conf/*
  mkdir -p $ORDS_CONF/conf/
  
  for db in $(echo $ADDITIONAL_DBS,$DB_HOSTNAME:$DB_SID | tr -d '[[:space:]]' | tr "," "\n") ;
  do

   if [ "$db" != "skip" ];
   then
     echo "==>> Configuring $db"

     IFS=":" read host sid <<< "$db"
 
     sid_lc=$(echo $sid |tr  [:upper:] [:lower:] | tr "," "\n")
 
     cp -f $TEMPLATE_PATH/template_pu.xml $ORDS_CONF/conf/${sid_lc}_pu.xml
 
     echo "$ORDS_CONF/conf/${sid_lc}_pu.xml"
 
     sed -i "s/@db.hostname@/${host}/g" $ORDS_CONF/conf/${sid_lc}_pu.xml
     sed -i "s/@db.ords_public_user_password@/${ORDS_PUBLIC_USER_PASSWORD}/g" $ORDS_CONF/conf/${sid_lc}_pu.xml
     sed -i "s/@db.sid@/${sid}/g" $ORDS_CONF/conf/${sid_lc}_pu.xml
 
     if [ ${DB_SID} == ${sid} ]; 
     then
        java -jar $ORDS_PATH/ords.war map-url --type base-path / ${DB_SID}  > /dev/null 2>&1
     fi
 
     java -jar $ORDS_PATH/ords.war map-url --type base-path /${sid_lc} ${sid} > /dev/null 2>&1
   
   fi
  done

}

function startOrds() {
   java -jar -Djava.security.egd=file:///dev/urandom -Duser.timezone=CET $ORDS_PATH/ords.war standalone
}


########### SIGTERM handler ############
function _term() {
   echo "Stopping container."
   echo "SIGTERM received, shutting down ORDS!"
   pkill ords;
}

########### SIGKILL handler ############
function _kill() {
   echo "SIGKILL received, shutting down ORDS!"
   pkill -9 ords;
}

############# MAIN ################

# Set SIGTERM handler
trap _term SIGTERM

# Set SIGKILL handler
trap _kill SIGKILL

# Configure ORDS
configureOrds;

# Check whether ords is already setup
startOrds;

echo "#####################"
echo "ORDS IS READY TO USE!"
echo "#####################"


childPID=$!
wait $childPID
