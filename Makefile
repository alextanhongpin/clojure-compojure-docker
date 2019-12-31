-include .env
export

server:
	@lein ring server

install:
	@lein install

docker:
	@docker build -t alextanhongpin/clojure .

start:
	@docker run -p 3000:3000 alextanhongpin/clojure


up:
	@docker-compose up -d

down:
	@docker-compose down
