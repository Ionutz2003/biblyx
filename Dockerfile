FROM ubuntu:latest
LABEL authors="flori"

ENTRYPOINT ["top", "-b"]