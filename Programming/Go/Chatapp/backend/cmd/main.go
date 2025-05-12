package main

import (
	"log"
	"os"
	"github.com/batmanboxer/chatapp/api/server"
	"github.com/batmanboxer/chatapp/internal/postgres"
	"github.com/joho/godotenv"
)

func main() {
	err := godotenv.Load()

	if err != nil {
		log.Fatal("Error loading .env file")
	}

	connStr := os.Getenv("POSTGRESS_URL")

	if connStr == "" {
    log.Fatal("Postgress connection string is not set in .env")
	}

	postgress, err := postgress.NewPostGres("postgres",connStr)

	if err != nil {
		log.Fatal("Cound Not Establish a Connection to Database", err.Error())
	}

	Api := server.NewApi(":4000", postgress)

	Api.StartApi()
}
