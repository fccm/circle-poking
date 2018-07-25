make F="$1" && \
PATH="$PATH:/cygdrive/c/Program Files/Java/jdk1.7.0/bin" \
echo java `basename $1 .java`
     java `basename $1 .java`
