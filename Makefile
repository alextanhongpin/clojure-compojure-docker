docker:
	@docker build -t alextanhongpin/clojure .

start:
	@docker run -p 3000:3000 alextanhongpin/clojure
