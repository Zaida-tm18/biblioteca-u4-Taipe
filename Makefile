.PHONY: up down logs build test verify psql redis-keys clean checkpoint historial

up:            ## Levanta postgres, redis y la aplicacion
	docker compose up -d --build

down:          ## Detiene y elimina los contenedores
	docker compose down

logs:          ## Sigue los logs de la aplicacion
	docker compose logs -f app

build:         ## Compila el proyecto sin ejecutar pruebas
	mvn -B clean package -DskipTests

test:          ## Ejecuta las pruebas (requiere Docker para Testcontainers)
	mvn -B test

verify:        ## Compila, prueba y genera el reporte de cobertura JaCoCo
	mvn -B clean verify

psql:          ## Abre una consola SQL contra la base
	docker compose exec postgres psql -U biblioteca -d biblioteca

redis-keys:    ## Evidencia de la cache de la API externa (TODO-U4-4)
	docker compose exec redis redis-cli KEYS "openlibrary*"

checkpoint:    ## Commit de control cuando el docente lo indique: make checkpoint M="lo que hice"
	git add -A
	git commit -m "checkpoint: $(M)"
	@echo "Recuerde hacer git push"

historial:     ## Muestra el historial con el tamano de cada commit (lo que revisa el docente)
	git log --pretty=format:'%h %ad %s' --date=iso
	@echo ""
	git log --numstat --pretty=format:'--- %h %ad %s' --date=iso

clean:
	mvn -B clean
